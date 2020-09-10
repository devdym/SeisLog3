package main.entities;

import javax.persistence.*;

@Entity
@Table(name = "ballasting")
public class Ballasting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name="name")
    private String name;
    @Column(name="min")
    private double min;
    @Column(name="max")
    private double max;
    @Column(name="mean")
    private double mean;
    @Column(name="sd")
    private double sd;
    @Column(name="obs")
    private int obs;
    @Column(name="rej")
    private int rej;
    @Column(name="streamer")
    private int streamer;
    @Column(name="compass")
    private int compass;
    @Column(name="linename")
    private String lineName;
    @Column(name="seq")
    private int seq;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Projects projects;

    public Ballasting() {
    }

    public Ballasting(String name, double min, double max, double mean, double sd, int obs, int rej, int streamer, int compass, String lineName, int seq, Projects projects) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.sd = sd;
        this.obs = obs;
        this.rej = rej;
        this.streamer = streamer;
        this.compass = compass;
        this.lineName = lineName;
        this.seq = seq;
        this.projects = projects;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getSd() {
        return sd;
    }

    public void setSd(double sd) {
        this.sd = sd;
    }

    public int getObs() {
        return obs;
    }

    public void setObs(int obs) {
        this.obs = obs;
    }

    public int getRej() {
        return rej;
    }

    public void setRej(int rej) {
        this.rej = rej;
    }

    public int getStreamer() {
        return streamer;
    }

    public void setStreamer(int streamer) {
        this.streamer = streamer;
    }

    public int getCompass() {
        return compass;
    }

    public void setCompass(int compass) {
        this.compass = compass;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }



    @Override
    public String toString() {
        return "Ballasting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", mean=" + mean +
                ", sd=" + sd +
                ", obs=" + obs +
                ", rej=" + rej +
                ", streamer=" + streamer +
                ", compass=" + compass +
                ", lineName='" + lineName + '\'' +
                ", seq=" + seq +
                ", projects=" + projects +
                '}';
    }
}
