package ru.shibanov.postal_point.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class PrintingHousesPage {
    private final SelenideElement createPrintRunButton = $x("//button[@class=\"MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButton-colorPrimary MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButton-colorPrimary css-p6fm8p-MuiButtonBase-root-MuiButton-root\"]");
    private final SelenideElement nameInput = $x("//input[@id=\"r8g\"]");
    private final SelenideElement addressInput = $x("//input[@id=\"r8h\"]");
    private final SelenideElement saveButton = $x("//button[text()=\"Сохранить\"]");
    private final SelenideElement openFiltersButton = $x("//span[@class=\"MuiAccordionSummary-expandIconWrapper css-1wqf3nl-MuiAccordionSummary-expandIconWrapper\"]");
    private final SelenideElement searchByNameInput = $x("//label[text()=\"Поиск по названию\"]");
    private final SelenideElement editButton = $x("//button[@class=\"MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-53g0n7-MuiButtonBase-root-MuiIconButton-root\"][2]");
    private final SelenideElement deleteButton = $x("//button[@class=\"MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeMedium css-53g0n7-MuiButtonBase-root-MuiIconButton-root\"][3]");

    public void createNewPrintingHouse(String name, String address) {
        createPrintRunButton.click();
        Selenide.sleep(500);
        nameInput.click();
        nameInput.setValue(name);
        addressInput.setValue(address);
        saveButton.click();
    }

    public boolean findPrintingHouseByName(String name) {
        openFiltersButton.click();
        searchByNameInput.setValue(name);
        SelenideElement visibleName = $x("//td[text()=\"" + name + "\"]");
        return visibleName.isDisplayed();
    }

    public void editPrintingHouse(String name) {
        editButton.click();
        nameInput.setValue(name);
    }

    public void deletePrintingHouse(String name) {
        deleteButton.click();
    }
}
