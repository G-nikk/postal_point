package ru.shibanov.postal_point.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "PrintRun")
public class PrintRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PrintRunID")
    private Integer printRunID;

    @ManyToOne // Many PrintRuns to one Newspaper
    @JoinColumn(name = "NewspaperID", nullable = false)
    private Newspaper newspaper;

    @ManyToOne // Many PrintRuns to one PrintingHouse
    @JoinColumn(name = "PrintingHouseID", nullable = false)
    private PrintingHouse printingHouse;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "printRun")  // One PrintRun can have many Deliveries
    private List<Delivery> deliveries;

    public Integer getPrintRunID() {
        return printRunID;
    }

    public void setPrintRunID(Integer printRunID) {
        this.printRunID = printRunID;
    }

    public Newspaper getNewspaper() {
        return newspaper;
    }

    public void setNewspaper(Newspaper newspaper) {
        this.newspaper = newspaper;
    }

    public PrintingHouse getPrintingHouse() {
        return printingHouse;
    }

    public void setPrintingHouse(PrintingHouse printingHouse) {
        this.printingHouse = printingHouse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PrintRun{" +
                "printRunID=" + printRunID +
                ", newspaper=" + newspaper.getName() +
                ", printingHouse=" + printingHouse.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
