package main.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Projects")
public class Projects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "projectName")
    private String projectName;
    @Column(name = "area")
    private String area;
    @Column(name = "client")
    private String client;
    @Column(name = "jobNumber")
    private String jobNumber;
    @Column(name = "type")
    private String type;
    @Column(name = "status")
    private String status;
    @Column(name = "vessel")
    private String vessel;

    @OneToMany(mappedBy = "projects")
    private List<Ballasting> ballasting = new ArrayList<>();

    @OneToMany(mappedBy = "projects")
    private List<Batteries> batteries = new ArrayList<>();

    @OneToMany(mappedBy = "projects")
    private List<InsTestRes> insTestRes = new ArrayList<>();

    @OneToMany(mappedBy = "projects")
    private List<InsTestLimits> insTestLimits = new ArrayList<>();

    @OneToMany(mappedBy = "projects")
    private List<ImportLog> importLog = new ArrayList<>();

    public Projects() {
    }

    public Projects(long id, String projectName, String area, String client, String jobNumber, String type, String status, String vessel) {
        this.id = id;
        this.projectName = projectName;
        this.area = area;
        this.client = client;
        this.jobNumber = jobNumber;
        this.type = type;
        this.status = status;
        this.vessel = vessel;
    }

    public Projects(String projectName, String area, String client, String jobNumber, String type, String status, String vessel) {
        this.projectName = projectName;
        this.area = area;
        this.client = client;
        this.jobNumber = jobNumber;
        this.type = type;
        this.status = status;
        this.vessel = vessel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVessel() {
        return vessel;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public List<Ballasting> getBallasting() {
        return ballasting;
    }

    public void setBallasting(List<Ballasting> ballasting) {
        this.ballasting = ballasting;
    }
//
//    public List<Batteries> getBatteries() {
//        return batteries;
//    }
//
//    public void setBatteries(List<Batteries> batteries) {
//        this.batteries = batteries;
//    }
//
//    public List<InsTestRes> getInsTestRes() {
//        return insTestRes;
//    }
//
//    public void setInsTestRes(List<InsTestRes> insTestRes) {
//        this.insTestRes = insTestRes;
//    }
//
//    public List<InsTestLimits> getInsTestLimits() {
//        return insTestLimits;
//    }
//
//    public void setInsTestLimits(List<InsTestLimits> insTestLimits) {
//        this.insTestLimits = insTestLimits;
//    }
//
//    public List<ImportLog> getImportLog() {
//        return importLog;
//    }
//
//    public void setImportLog(List<ImportLog> importLog) {
//        this.importLog = importLog;
//    }

    @Override
    public String toString() {
        return "Projects{" +
                "id=" + id +
                ", projectName='" + projectName + '\'' +
                ", area='" + area + '\'' +
                ", client='" + client + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", vessel='" + vessel + '\'' +
                '}';
    }
}
