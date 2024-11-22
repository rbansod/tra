import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Service;

@Service
public class NoiseReductionService {

    private static final double NOISE_THRESHOLD = 0.01; // Adjusted for G.711 RTP packets

    public double[] reduceNoise(double[] samples, int frameSize) {
        int numFrames = samples.length / frameSize;
        double[] denoisedSamples = new double[samples.length];

        // Estimate noise spectrum
        double[] noiseSpectrum = estimateNoiseSpectrum(samples, frameSize);

        // Apply Wiener filter
        for (int i = 0; i < numFrames; i++) {
            double[] frame = extractFrame(samples, i * frameSize, frameSize);
            double[] spectrum = calculateMagnitudeSpectrum(frame);

            for (int j = 0; j < spectrum.length; j++) {
                spectrum[j] = spectrum[j] * spectrum[j] / (spectrum[j] * spectrum[j] + noiseSpectrum[j]);
            }

            double[] filteredFrame = inverseMagnitudeSpectrum(spectrum);
            System.arraycopy(filteredFrame, 0, denoisedSamples, i * frameSize, frameSize);
        }

        return denoisedSamples;
    }

    private double[] estimateNoiseSpectrum(double[] samples, int frameSize) {
        int numFrames = samples.length / frameSize;
        double[] noiseSpectrum = new double[frameSize];

        // Use first few frames for noise estimation
        int noiseFrames = Math.min(10, numFrames);
        for (int i = 0; i < noiseFrames; i++) {
            double[] frame = extractFrame(samples, i * frameSize, frameSize);
            double[] spectrum = calculateMagnitudeSpectrum(frame);

            for (int j = 0; j < spectrum.length; j++) {
                noiseSpectrum[j] += spectrum[j];
            }
        }

        // Average noise spectrum
        for (int j = 0; j < noiseSpectrum.length; j++) {
            noiseSpectrum[j] /= noiseFrames;
        }

        return noiseSpectrum;
    }

    private double[] calculateMagnitudeSpectrum(double[] frame) {
        DoubleFFT_1D fft = new DoubleFFT_1D(frame.length);
        double[] complex = new double[frame.length * 2];
        System.arraycopy(frame, 0, complex, 0, frame.length);

        fft.realForwardFull(complex);
        double[] magnitude = new double[frame.length];
        for (int i = 0; i < frame.length; i++) {
            magnitude[i] = Math.sqrt(complex[2 * i] * complex[2 * i] + complex[2 * i + 1] * complex[2 * i + 1]);
        }
        return magnitude;
    }

    private double[] inverseMagnitudeSpectrum(double[] spectrum) {
        DoubleFFT_1D fft = new DoubleFFT_1D(spectrum.length);
        double[] complex = new double[spectrum.length * 2];
        for (int i = 0; i < spectrum.length; i++) {
            complex[2 * i] = spectrum[i]; // Real part
            complex[2 * i + 1] = 0;      // Imaginary part
        }

        fft.complexInverse(complex, true);
        double[] result = new double[spectrum.length];
        for (int i = 0; i < spectrum.length; i++) {
            result[i] = complex[2 * i];
        }
        return result;
    }

    private double[] extractFrame(double[] samples, int start, int frameSize) {
        double[] frame = new double[frameSize];
        System.arraycopy(samples, start, frame, 0, frameSize);
        return frame;
    }
}
