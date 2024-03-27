public class Span {
    private int startIdx;
    private int endIdx;
    private float startDb;
    private float endDb;
    private int numVoiceChunks;
    private int numNonVoiceChunks;
    private String speaker;

    public Span(int startIdx, int endIdx, float startDb, float endDb, int numVoiceChunks, int numNonVoiceChunks, String speaker) {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
        this.startDb = startDb;
        this.endDb = endDb;
        this.numVoiceChunks = numVoiceChunks;
        this.numNonVoiceChunks = numNonVoiceChunks;
        this.speaker = speaker;
    }

    public int getStartIdx() {
        return startIdx;
    }

    public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public void setEndIdx(int endIdx) {
        this.endIdx = endIdx;
    }

    public float getStartDb() {
        return startDb;
    }

    public void setStartDb(float startDb) {
        this.startDb = startDb;
    }

    public float getEndDb() {
        return endDb;
    }

    public void setEndDb(float endDb) {
        this.endDb = endDb;
    }

    public int getNumVoiceChunks() {
        return numVoiceChunks;
    }

    public void setNumVoiceChunks(int numVoiceChunks) {
        this.numVoiceChunks = numVoiceChunks;
    }

    public int getNumNonVoiceChunks() {
        return numNonVoiceChunks;
    }

    public void setNumNonVoiceChunks(int numNonVoiceChunks) {
        this.numNonVoiceChunks = numNonVoiceChunks;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }
}
