package main.insTest;

public class resultTable {

    public int streamer;
    public int cap;
    public int cutoff;
    public int noise;
    public int leakage;
    public int total;

    public resultTable(int streamer, int cap, int cutoff, int noise, int leakage, int total) {
        this.streamer = streamer;
        this.cap = cap;
        this.cutoff = cutoff;
        this.noise = noise;
        this.leakage = leakage;
        this.total = total;
    }

    public int getStreamer() {
        return streamer;
    }

    public void setStreamer(int streamer) {
        this.streamer = streamer;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public int getCutoff() {
        return cutoff;
    }

    public void setCutoff(int cutoff) {
        this.cutoff = cutoff;
    }

    public int getNoise() {
        return noise;
    }

    public void setNoise(int noise) {
        this.noise = noise;
    }

    public int getLeakage() {
        return leakage;
    }

    public void setLeakage(int leakage) {
        this.leakage = leakage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
