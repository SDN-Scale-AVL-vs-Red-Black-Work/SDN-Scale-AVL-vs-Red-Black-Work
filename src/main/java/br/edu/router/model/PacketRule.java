package br.edu.router.model;

public class PacketRule implements Comparable<PacketRule> {

    private final int id;
    private final String sourceIp;
    private final String destinationIp;
    private final int priority;

    public PacketRule(int id, String sourceIp, String destinationIp, int priority) {
        this.id = id;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PacketRule other) {
        int priorityComparison = Integer.compare(other.priority, this.priority);

        if (priorityComparison != 0) {
            return priorityComparison;
        }

        return Integer.compare(this.id, other.id);
    }

    @Override
    public String toString() {
        return "PacketRule{" +
                "id=" + id +
                ", sourceIp='" + sourceIp + '\'' +
                ", destinationIp='" + destinationIp + '\'' +
                ", priority=" + priority +
                '}';
    }
}
