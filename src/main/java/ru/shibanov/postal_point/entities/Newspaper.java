package ru.shibanov.postal_point.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Newspaper")
public class Newspaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NewspaperID")
    private Integer newspaperID;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "IndexEdition", nullable = false, unique = true)
    private String indexEdition;

    @Column(name = "Editor", nullable = false)
    private String editor;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "newspaper")
    private List<PrintRun> printRuns;


    public Integer getNewspaperID() {
        return newspaperID;
    }

    public void setNewspaperID(Integer newspaperID) {
        this.newspaperID = newspaperID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndexEdition() {
        return indexEdition;
    }

    public void setIndexEdition(String indexEdition) {
        this.indexEdition = indexEdition;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<PrintRun> getPrintRuns() {
        return printRuns;
    }

    public void setPrintRuns(List<PrintRun> printRuns) {
        this.printRuns = printRuns;
    }

    @Override
    public String toString() {
        return "Newspaper{" +
                "newspaperID=" + newspaperID +
                ", name='" + name + '\'' +
                ", indexEdition='" + indexEdition + '\'' +
                ", editor='" + editor + '\'' +
                ", price=" + price +
                '}';
    }
}
