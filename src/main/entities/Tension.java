package main.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tension")
public class Tension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_")
    private LocalDate date_;
    @Column(name = "streamer")
    private int streamer;
    @Column(name = "tension")
    private int tension;

    @ManyToOne
    @JoinColumn(name = "Project_id", nullable = false)
    private Projects projects;

    public Tension() {
    }

    public Tension(LocalDate date_, int streamer, int tension, Projects projects) {
        this.date_ = date_;
        this.streamer = streamer;
        this.tension = tension;
        this.projects = projects;
    }

    public LocalDate getDate_() {
        return date_;
    }

    public void setDate_(LocalDate date_) {
        this.date_ = date_;
    }

    public int getStreamer() {
        return streamer;
    }

    public void setStreamer(int streamer) {
        this.streamer = streamer;
    }

    public int getTension() {
        return tension;
    }

    public void setTension(int tension) {
        this.tension = tension;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "Tension{" +
                "date_=" + date_ +
                ", streamer=" + streamer +
                ", tension=" + tension +
                ", projects=" + projects +
                '}';
    }
}
