package ru.shibanov.postal_point.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "printinghouse")
public class PrintingHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "printinghouseid")
    private Integer printingHouseID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "printingHouse", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PrintRun> printRuns;

    public Integer getPrintingHouseID() {
        return printingHouseID;
    }

    public void setPrintingHouseID(Integer printingHouseID) {
        this.printingHouseID = printingHouseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PrintRun> getPrintRuns() {
        return printRuns;
    }

    public void setPrintRuns(List<PrintRun> printRuns) {
        this.printRuns = printRuns;
    }

    @Override
    public String toString() {
        return "PrintingHouse{" +
                "printingHouseID=" + printingHouseID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
