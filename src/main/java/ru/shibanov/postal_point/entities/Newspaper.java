package ru.shibanov.postal_point.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "newspaper")
public class Newspaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newspaperid")
    private Integer newspaperID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "indexedition", nullable = false, unique = true)
    private String indexEdition;

    @Column(name = "editor", nullable = false)
    private String editor;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

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
