public class Candidate {
    private int startIdx;
    private double startDB;
    private int numVoiceChunks;
    private int numNonVoiceChunks;

    public Candidate(int startIdx, double startDB) {
        this.startIdx = startIdx;
        this.startDB = startDB;
        this.numVoiceChunks = 0;
        this.numNonVoiceChunks = 0;
    }
  
    public Candidate(int startIdx, double startDB, int numVoiceChunks, int numNonVoiceChunks) {
        this.startIdx = startIdx;
        this.startDB = startDB;
        this.numVoiceChunks = numVoiceChunks;
        this.numNonVoiceChunks = numNonVoiceChunks;
    }
    
    public int getStartIdx() {
        return startIdx;
    }

    public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }

    public double getStartDB() {
        return startDB;
    }

    public void setStartDB(double startDB) {
        this.startDB = startDB;
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

    public void addVoice() {
        this.numVoiceChunks++;
    }
    
    public void addNonVoice() {
        this.numNonVoiceChunks++;
    }
}
