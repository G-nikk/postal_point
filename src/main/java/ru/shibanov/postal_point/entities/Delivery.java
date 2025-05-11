package ru.shibanov.postal_point.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deliveryid")
    private Integer deliveryID;

    @ManyToOne // Many Deliveries to one PrintRun
    @JoinColumn(name = "printrunid", nullable = false)
    private PrintRun printRun;

    @ManyToOne // Many Deliveries to one PostOffice
    @JoinColumn(name = "postofficeid", nullable = false)
    private PostOffice postOffice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    public Integer getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(Integer deliveryID) {
        this.deliveryID = deliveryID;
    }

    public PrintRun getPrintRun() {
        return printRun;
    }

    public void setPrintRun(PrintRun printRun) {
        this.printRun = printRun;
    }

    public PostOffice getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(PostOffice postOffice) {
        this.postOffice = postOffice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "deliveryID=" + deliveryID +
                ", PrintRun=" + printRun.getPrintRunID() +
                ", postOffice=" + postOffice.getNumber() +
                ", quantity=" + quantity +
                '}';
    }


}