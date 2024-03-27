import java.util.ArrayList;
import java.util.List;

public class Metric {
    private List<Float> voiceEnergies;
    private List<Float> nonVoiceEnergies;

    public Metric() {
        this.voiceEnergies = new ArrayList<>();
        this.nonVoiceEnergies = new ArrayList<>();
    }

    public List<Float> getVoiceEnergies() {
        return voiceEnergies;
    }

    public void setVoiceEnergies(List<Float> voiceEnergies) {
        this.voiceEnergies = voiceEnergies;
    }

    public List<Float> getNonVoiceEnergies() {
        return nonVoiceEnergies;
    }

    public void setNonVoiceEnergies(List<Float> nonVoiceEnergies) {
        this.nonVoiceEnergies = nonVoiceEnergies;
    }
}
