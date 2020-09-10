package main.ballasting;

public class SeqData {

    private int Streamer;
    private int Unit;
    private double seq1_mean;
    private double seq2_mean;
    private double seq3_mean;
    private double seq4_mean;
    private double seq5_mean;
    private double seq6_mean;
    private double seq7_mean;
    private double seq8_mean;
    private double seq9_mean;
    private double seq10_mean;

    public SeqData(int streamer, int unit, double seq1_mean, double seq2_mean, double seq3_mean, double seq4_mean, double seq5_mean, double seq6_mean, double seq7_mean, double seq8_mean, double seq9_mean, double seq10_mean) {
        this.Streamer = streamer;
        this.Unit = unit;
        this.seq1_mean = seq1_mean;
        this.seq2_mean = seq2_mean;
        this.seq3_mean = seq3_mean;
        this.seq4_mean = seq4_mean;
        this.seq5_mean = seq5_mean;
        this.seq6_mean = seq6_mean;
        this.seq7_mean = seq7_mean;
        this.seq8_mean = seq8_mean;
        this.seq9_mean = seq9_mean;
        this.seq10_mean = seq10_mean;
    }

    public int getStreamer() {
        return Streamer;
    }

    public void setStreamer(int streamer) {
        this.Streamer = streamer;
    }

    public int getUnit() {
        return Unit;
    }

    public void setUnit(int unit) {
        this.Unit = unit;
    }

    public double getSeq1_mean() {
        return seq1_mean;
    }

    public void setSeq1_mean(double seq1_mean) {
        this.seq1_mean = seq1_mean;
    }

    public double getSeq2_mean() {
        return seq2_mean;
    }

    public void setSeq2_mean(double seq2_mean) {
        this.seq2_mean = seq2_mean;
    }

    public double getSeq3_mean() {
        return seq3_mean;
    }

    public void setSeq3_mean(double seq3_mean) {
        this.seq3_mean = seq3_mean;
    }

    public double getSeq4_mean() {
        return seq4_mean;
    }

    public void setSeq4_mean(double seq4_mean) {
        this.seq4_mean = seq4_mean;
    }

    public double getSeq5_mean() {
        return seq5_mean;
    }

    public void setSeq5_mean(double seq5_mean) {
        this.seq5_mean = seq5_mean;
    }

    public double getSeq6_mean() {
        return seq6_mean;
    }

    public void setSeq6_mean(double seq6_mean) {
        this.seq6_mean = seq6_mean;
    }

    public double getSeq7_mean() {
        return seq7_mean;
    }

    public void setSeq7_mean(double seq7_mean) {
        this.seq7_mean = seq7_mean;
    }

    public double getSeq8_mean() {
        return seq8_mean;
    }

    public void setSeq8_mean(double seq8_mean) {
        this.seq8_mean = seq8_mean;
    }

    public double getSeq9_mean() {
        return seq9_mean;
    }

    public void setSeq9_mean(double seq9_mean) {
        this.seq9_mean = seq9_mean;
    }

    public double getSeq10_mean() {
        return seq10_mean;
    }

    public void setSeq10_mean(double seq10_mean) {
        this.seq10_mean = seq10_mean;
    }

    @Override
    public String toString() {
        return "SeqData{" +
                "Streamer=" + Streamer +
                ", Unit=" + Unit +
                ", seq1_mean=" + seq1_mean +
                ", seq2_mean=" + seq2_mean +
                ", seq3_mean=" + seq3_mean +
                ", seq4_mean=" + seq4_mean +
                ", seq5_mean=" + seq5_mean +
                ", seq6_mean=" + seq6_mean +
                ", seq7_mean=" + seq7_mean +
                ", seq8_mean=" + seq8_mean +
                ", seq9_mean=" + seq9_mean +
                ", seq10_mean=" + seq10_mean +
                '}';
    }
}
