package ru.shibanov.postal_point.frontend_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.shibanov.postal_point.pages.PrintingHousesPage;

public class PrintingHouseE2ETest extends BaseTest {
    private final String name = "Новая типография";
    private final String address = "Москва, ул.Стромынка, 20";
    private final PrintingHousesPage printingHousesPage = new PrintingHousesPage();

    @Test
    public void printRunE2ETest() {
        printingHousesPage.createNewPrintingHouse(name, address);
        Assertions.assertTrue(printingHousesPage.findPrintingHouseByName(name));
        printingHousesPage.editPrintingHouse(name);
        Assertions.assertTrue(printingHousesPage.findPrintingHouseByName(name));
        printingHousesPage.deletePrintingHouse(name);
        Assertions.assertFalse(printingHousesPage.findPrintingHouseByName(name));
    }

}
