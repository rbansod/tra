import org.springframework.stereotype.Service;

@Service
public class FeatureExtractionService {

    private static final double MIN_PITCH = 80.0;
    private static final double MAX_PITCH = 300.0;

    public double calculateFrameEnergy(double[] frame) {
        double energy = 0;
        for (double sample : frame) {
            energy += sample * sample;
        }
        return energy / frame.length;
    }

    public double calculatePitch(double[] frame) {
        int n = frame.length;
        double[] autocorrelation = new double[n];

        for (int lag = 0; lag < n; lag++) {
            for (int i = 0; i < n - lag; i++) {
                autocorrelation[lag] += frame[i] * frame[i + lag];
            }
        }

        int pitchPeriod = 0;
        double maxAutocorrelation = 0;
        for (int i = (int) (8000 / MAX_PITCH); i < (int) (8000 / MIN_PITCH); i++) {
            if (autocorrelation[i] > maxAutocorrelation) {
                maxAutocorrelation = autocorrelation[i];
                pitchPeriod = i;
            }
        }

        return pitchPeriod > 0 ? 8000.0 / pitchPeriod : 0.0;
    }

    public double calculateSpectralCentroid(double[] frame, int sampleRate) {
        double sumWeightedFrequency = 0;
        double sumMagnitude = 0;

        for (int i = 0; i < frame.length; i++) {
            double magnitude = Math.abs(frame[i]);
            double frequency = (double) i * sampleRate / frame.length;

            sumWeightedFrequency += magnitude * frequency;
            sumMagnitude += magnitude;
        }

        return sumMagnitude > 0 ? sumWeightedFrequency / sumMagnitude : 0.0;
    }
}
