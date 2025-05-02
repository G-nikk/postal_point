package ru.shibanov.postal_point.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shibanov.postal_point.entities.Newspaper;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.entities.PrintingHouse;

@Repository
public interface PrintRunRepository extends JpaRepository<PrintRun, Integer> {
    PrintRun findPrintRunByNewspaper(Newspaper newspaper);
    PrintRun findTopByPrintingHouseAndNewspaperOrderByQuantityDesc(PrintingHouse printingHouse, Newspaper newspaper);
}