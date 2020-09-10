package main.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "insTestRes")
public class InsTestRes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "streamer")
    private int streamer;
    @Column(name = "trace")
    private int trace;
    @Column(name = "type")
    private int type;
    @Column(name = "ass_sn")
    private int ass_sn;
    @Column(name = "section_rank")
    private Integer section_rank;
    @Column(name = "fdu_sn")
    private int fdu_sn;
    @Column(name = "ch_nb")
    private Integer ch_nb;
    @Column(name = "cap")
    private double cap;
    @Column(name = "cutoff")
    private double cutoff;
    @Column(name = "noise")
    private double noise;
    @Column(name = "leakage")
    private double leakage;
    @Column(name = "updated")
    private LocalDate updated;
    @Column(name = "failure")
    private String failure;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Projects projects;

    public InsTestRes() {
    }

    public InsTestRes(int streamer, int trace, int type, int ass_sn, double cap, double cutoff, double noise, double leakage) {
        this.streamer = streamer;
        this.trace = trace;
        this.type = type;
        this.ass_sn = ass_sn;
        this.cap = cap;
        this.cutoff = cutoff;
        this.noise = noise;
        this.leakage = leakage;
    }



    public InsTestRes(long id, int streamer, int trace, int type, int ass_sn, Integer section_rank, int fdu_sn, Integer ch_nb, double cap, double cutoff, double noise, double leakage, LocalDate updated, String failure, Projects projects) {
        this.id = id;
        this.streamer = streamer;
        this.trace = trace;
        this.type = type;
        this.ass_sn = ass_sn;
        this.section_rank = section_rank;
        this.fdu_sn = fdu_sn;
        this.ch_nb = ch_nb;
        this.cap = cap;
        this.cutoff = cutoff;
        this.noise = noise;
        this.leakage = leakage;
        this.updated = updated;
        this.failure = failure;
        this.projects = projects;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStreamer() {
        return streamer;
    }

    public void setStreamer(int streamer) {
        this.streamer = streamer;
    }

    public int getTrace() {
        return trace;
    }

    public void setTrace(int trace) {
        this.trace = trace;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAss_sn() {
        return ass_sn;
    }

    public void setAss_sn(int ass_sn) {
        this.ass_sn = ass_sn;
    }

    public Integer getSection_rank() {
        return section_rank;
    }

    public void setSection_rank(Integer section_rank) {
        this.section_rank = section_rank;
    }

    public int getFdu_sn() {
        return fdu_sn;
    }

    public void setFdu_sn(int fdu_sn) {
        this.fdu_sn = fdu_sn;
    }

    public Integer getCh_nb() {
        return ch_nb;
    }

    public void setCh_nb(Integer ch_nb) {
        this.ch_nb = ch_nb;
    }

    public double getCap() {
        return cap;
    }

    public void setCap(double cap) {
        this.cap = cap;
    }

    public double getCutoff() {
        return cutoff;
    }

    public void setCutoff(double cutoff) {
        this.cutoff = cutoff;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public double getLeakage() {
        return leakage;
    }

    public void setLeakage(double leakage) {
        this.leakage = leakage;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }
}
