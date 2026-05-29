package br.edu.router.benchmark;

public class BenchmarkResult {
    private String treeName;
    private int volume;
    private long insertionTime;
    private long searchTime;
    private long deletionTime;
    private int rotations;

    public BenchmarkResult(String treeName, int volume, long insertionTime, long searchTime, long deletionTime, int rotations) {
        this.treeName = treeName;
        this.volume = volume;
        this.insertionTime = insertionTime;
        this.searchTime = searchTime;
        this.deletionTime = deletionTime;
        this.rotations = rotations;   
    }
    public String getTreeName() {
        return treeName;
    }
    public int getVolume() {
        return volume;
    }
    public long getInsertionTime() {
        return insertionTime;
    }
    public long getSearchTime() {
        return searchTime;
    }
    public long getDeletionTime() {
        return deletionTime;
    }
    public int getRotations() {
        return rotations;
    }
}
