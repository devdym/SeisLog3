package main.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "batteries")
public class Batteries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "unit")
    private String unit;
    @Column(name="streamerNumber")
    private int streamerNumber;
    @Column(name="unitName")
    private String unitName;
    @Column(name="unitNumber")
    private int unitNumber;
    @Column(name = "bankA")
    private double bankA;
    @Column(name = "bankB")
    private double bankB;
    @Column(name = "activeBank")
    private String activeBank;
    @Column(name = "date_")
    private LocalDate date_;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Projects projects;

    public Batteries(){}

    public Batteries(int streamerNumber, String unitName, int unitNumber, double bankA, double bankB, String activeBank) {
        this.streamerNumber = streamerNumber;
        this.unitName = unitName;
        this.unitNumber = unitNumber;
        this.bankA = bankA;
        this.bankB = bankB;
        this.activeBank = activeBank;
    }

    public Batteries(String unit, int streamerNumber, String unitName, int unitNumber, double bankA, double bankB, String activeBank, LocalDate date_, Projects projects) {
        this.unit = unit;
        this.streamerNumber = streamerNumber;
        this.unitName = unitName;
        this.unitNumber = unitNumber;
        this.bankA = bankA;
        this.bankB = bankB;
        this.activeBank = activeBank;
        this.date_ = date_;
        this.projects = projects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStreamerNumber() {
        return streamerNumber;
    }

    public void setStreamerNumber(int streamerNumber) {
        this.streamerNumber = streamerNumber;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(int unitNumber) {
        this.unitNumber = unitNumber;
    }

    public double getBankA() {
        return bankA;
    }

    public void setBankA(double bankA) {
        this.bankA = bankA;
    }

    public double getBankB() {
        return bankB;
    }

    public void setBankB(double bankB) {
        this.bankB = bankB;
    }

    public String getActiveBank() {
        return activeBank;
    }

    public void setActiveBank(String activeBank) {
        this.activeBank = activeBank;
    }

    public Projects getProjects() {
        return projects;
    }

    public void setProjects(Projects projects) {
        this.projects = projects;
    }

    public LocalDate getDate_() {
        return date_;
    }

    public void setDate_(LocalDate date_) {
        this.date_ = date_;
    }

    @Override
    public String toString() {
        return "Batteries{" +
                "id=" + id +
                ", unit='" + unit + '\'' +
                ", streamerNumber=" + streamerNumber +
                ", unitName='" + unitName + '\'' +
                ", unitNumber=" + unitNumber +
                ", bankA=" + bankA +
                ", bankB=" + bankB +
                ", activeBank='" + activeBank + '\'' +
                ", date_=" + date_ +
                ", projects=" + projects +
                '}';
    }
}
