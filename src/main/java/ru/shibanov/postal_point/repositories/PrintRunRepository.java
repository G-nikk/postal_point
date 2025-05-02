package ru.shibanov.postal_point.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shibanov.postal_point.entities.Newspaper;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.entities.PrintingHouse;

import java.util.List;

@Repository
public interface PrintRunRepository extends JpaRepository<PrintRun, Integer> {
    List<PrintRun> findPrintRunsByNewspaper(Newspaper newspaper);
    PrintRun findTopByPrintingHouseAndNewspaperOrderByQuantityDesc(PrintingHouse printingHouse, Newspaper newspaper);
    List<PrintRun> findPrintRunsByNewspaperAndPrintingHouse(Newspaper newspaper, PrintingHouse printingHouse);
}