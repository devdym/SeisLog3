package main.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "insTestLimits")
public class InsTestLimits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "noise")
    private double noise;

    @Column(name = "cap_min")
    private double cap_min;

    @Column(name = "cap_max")
    private double cap_max;

    @Column(name = "leakage")
    private double leakage;

    @Column(name = "cutoff_min")
    private double cutoff_min;

    @Column(name = "cutoff_max")
    private double cutoff_max;

    @Column(name = "sensor_nb")
    private int sensor_nb;

    @Column(name = "updated")
    private LocalDate updated;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Projects projects;

    public InsTestLimits() {
    }

    public InsTestLimits(double noise, double cap_min, double cap_max, double leakage, double cutoff_min, double cutoff_max, int sensor_nb, LocalDate updated, Projects projects) {
        this.noise = noise;
        this.cap_min = cap_min;
        this.cap_max = cap_max;
        this.leakage = leakage;
        this.cutoff_min = cutoff_min;
        this.cutoff_max = cutoff_max;
        this.sensor_nb = sensor_nb;
        this.updated = updated;
        this.projects = projects;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public double getCap_min() {
        return cap_min;
    }

    public void setCap_min(double cap_min) {
        this.cap_min = cap_min;
    }

    public double getCap_max() {
        return cap_max;
    }

    public void setCap_max(double cap_max) {
        this.cap_max = cap_max;
    }

    public double getLeakage() {
        return leakage;
    }

    public void setLeakage(double leakage) {
        this.leakage = leakage;
    }

    public double getCutoff_min() {
        return cutoff_min;
    }

    public void setCutoff_min(double cutoff_min) {
        this.cutoff_min = cutoff_min;
    }

    public double getCutoff_max() {
        return cutoff_max;
    }

    public void setCutoff_max(double cutoff_max) {
        this.cutoff_max = cutoff_max;
    }

    public int getSensor_nb() {
        return sensor_nb;
    }

    public void setSensor_nb(int sensor_nb) {
        this.sensor_nb = sensor_nb;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "InsTestLimits{" +
                "noise=" + noise +
                ", cap_min=" + cap_min +
                ", cap_max=" + cap_max +
                ", leakage=" + leakage +
                ", cutoff_min=" + cutoff_min +
                ", cutoff_max=" + cutoff_max +
                ", sensor_nb=" + sensor_nb +
                ", updated=" + updated +
                ", projects=" + projects +
                '}';
    }
}
