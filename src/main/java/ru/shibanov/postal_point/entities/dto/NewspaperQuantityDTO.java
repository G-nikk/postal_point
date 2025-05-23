package ru.shibanov.postal_point.entities.dto;

public class NewspaperQuantityDTO {
    private String name;
    private int totalQuantity;

    public NewspaperQuantityDTO() {
    }

    public NewspaperQuantityDTO(String name, int totalQuantity) {
        this.name = name;
        this.totalQuantity = totalQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
