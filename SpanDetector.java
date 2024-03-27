import java.util.ArrayList;
import java.util.List;

public class SpanDetector {
    private final int sr;
    private final int chunksPerSec;
    private final double energyThreshold0;
    private final double energyThreshold1;
    private final int minVoiceChunks;
    private final int minNonVoiceChunks;
    private int currIdx;
    private Candidate currCandidate;
    private final List<Span> spans;
    private final Metric metric;

    public SpanDetector(int sr, int chunksPerSec, double energyThreshold0, double energyThreshold1,
                        int minVoiceChunks, int minNonVoiceChunks) {
        this.sr = sr;
        this.chunksPerSec = chunksPerSec;
        this.energyThreshold0 = energyThreshold0;
        this.energyThreshold1 = energyThreshold1;
        this.minVoiceChunks = minVoiceChunks;
        this.minNonVoiceChunks = minNonVoiceChunks;
        this.currIdx = 0;
        this.currCandidate = null;
        this.spans = new ArrayList<>();
        this.metric = new Metric();
    }

    public List<Span> addToStream(double[] audio, String speaker) {
        long currTime = System.nanoTime();
        int nSamples = audio.length;

        int chunkSize = nSamples / chunksPerSec;
        List<Span> spans = new ArrayList<>();
        for (int i = 0; i < nSamples - 1; i += chunkSize) {
            int start = i + currIdx;
            int end = start + chunkSize;

            double[] audioB = new double[chunkSize];
            System.arraycopy(audio, i, audioB, 0, chunkSize);

            double energy = getAverageEnergy(audioB);
            double avgNonVoiceEnergy = getAverageNonVoiceEnergy();

            double voiceThreshold = (currCandidate == null) ?
                    energyThreshold0 :
                    ((energyThreshold1 + avgNonVoiceEnergy) / 2);

            if (energy >= voiceThreshold) {
                if (currCandidate == null) {
                    currCandidate = new Candidate(start, energy);
                }
                currCandidate.addVoice();
                metric.getVoiceEnergies().add(energy);
            } else if (currCandidate != null) {
                currCandidate.addNonVoice();
                metric.getNonVoiceEnergies().add(energy);
            } else {
                metric.getNonVoiceEnergies().add(energy);
                continue;
            }

            if (isBreakDetected()) {
                if (isCandidateEligible()) {
                    int shortEnd = start - 1;
                    Span span = new Span(currCandidate.getStartIdx(), shortEnd, currCandidate.getStartDb(), energy,
                            currCandidate.getNumVoiceChunks(), currCandidate.getNumNonVoiceChunks(), speaker);
                    spans.add(span);
                    this.spans.add(span);
                } else {
                    // Candidate not eligible
                }
                currCandidate = null;
            }
        }
        long runtime = (System.nanoTime() - currTime) / 1000000;
        currIdx += nSamples;
        return spans;
    }

    private double getAverageEnergy(double[] audio) {
        double sum = 0;
        for (double sample : audio) {
            sum += Math.abs(sample);
        }
        return sum / audio.length;
    }

    private double getAverageNonVoiceEnergy() {
        List<Double> nonVoiceEnergies = metric.getNonVoiceEnergies();
        double sum = 0;
        for (Double energy : nonVoiceEnergies) {
            sum += energy;
        }
        return sum / nonVoiceEnergies.size();
    }

    public Complex[] getFFT(double[] audio) {
        int N = audio.length;
        double[] w = hammingWindow(N);
        double[] windowedAudio = applyWindow(audio, w);
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        return transformer.transform(windowedAudio, TransformType.FORWARD);
    }

    private double[] hammingWindow(int N) {
        double[] w = new double[N];
        for (int i = 0; i < N; i++) {
            w[i] = 0.54 - 0.46 * Math.cos(2 * Math.PI * i / (N - 1));
        }
        return w;
    }

    private double[] applyWindow(double[] audio, double[] window) {
        double[] windowedAudio = new double[audio.length];
        for (int i = 0; i < audio.length; i++) {
            windowedAudio[i] = audio[i] * window[i];
        }
        return windowedAudio;
    }
  
    private boolean isCandidateEligible() {
        return currCandidate.getNumVoiceChunks() > minVoiceChunks;
    }

    private boolean isBreakDetected() {
        return currCandidate.getNumNonVoiceChunks() > minNonVoiceChunks;
    }
}
