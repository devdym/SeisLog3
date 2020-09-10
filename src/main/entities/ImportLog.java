package main.entities;

import javax.persistence.*;

@Entity
@Table(name="importLog")
public class ImportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "file")
    private String file;

    @ManyToOne
    @JoinColumn(name = "projects_id", nullable = false)
    private Projects projects;

    public ImportLog() {
    }

    public ImportLog(String file, Projects projects) {
        this.file = file;
        this.projects = projects;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public main.entities.Projects getProjects() {
        return projects;
    }

    public void setProjects(main.entities.Projects projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return "ImportLog{" +
                "id=" + id +
                ", file='" + file + '\'' +
                ", Projects=" + projects +
                '}';
    }
}
