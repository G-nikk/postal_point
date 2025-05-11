package ru.shibanov.postal_point.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "postoffice")
public class PostOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postofficeid")
    private Integer postOfficeID;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "postOffice")
    private List<Delivery> deliveries;

    public Integer getPostOfficeID() {
        return postOfficeID;
    }

    public void setPostOfficeID(Integer postOfficeID) {
        this.postOfficeID = postOfficeID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    @Override
    public String toString() {
        return "PostOffice{" +
                "postOfficeID=" + postOfficeID +
                ", number='" + number + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
