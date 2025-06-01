package ru.shibanov.postal_point.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class NewspapersPage {
    private final SelenideElement addNewspaperButton = $x("//button[@class=\"MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButton-colorPrimary MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButton-colorPrimary css-p6fm8p-MuiButtonBase-root-MuiButton-root\"]");
    private final SelenideElement nameInput = $x("//label[text()=\"Название\"]");
    private final SelenideElement editorInput = $x("//label[text()=\"Редактор\"]");
    private final SelenideElement indexInput = $x("//label[text()=\"Индекс издания\"]");
    private final SelenideElement priceInput = $x("//label[text()=\"Цена\"]");
    private final SelenideElement createButton = $x("//button[text()=\"Создать\"]");

}
