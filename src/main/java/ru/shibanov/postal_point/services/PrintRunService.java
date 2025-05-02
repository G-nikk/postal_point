package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.Newspaper;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.entities.PrintingHouse;
import ru.shibanov.postal_point.repositories.PrintRunRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PrintRunService {
    private final PrintRunRepository printRunRepository;

    @Autowired
    public PrintRunService(PrintRunRepository printRunRepository) {
        this.printRunRepository = printRunRepository;
    }

    @Transactional
    public void save(PrintRun printRun) {
        printRunRepository.save(printRun);
    }

    @Transactional(readOnly = true)
    public PrintRun findById(int id) {
        return printRunRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PrintRun> findByNewspaper(Newspaper newspaper) {
        return printRunRepository.findPrintRunsByNewspaper(newspaper);
    }

    public Optional<PrintRun> findTopByPrintingHouseAndNewspaperOrderByQuantityDesc(PrintingHouse printingHouse, Newspaper newspaper) {
     return Optional.ofNullable(printRunRepository.findTopByPrintingHouseAndNewspaperOrderByQuantityDesc(printingHouse, newspaper));
    }

    public List<PrintRun> findPrintRunsByNewspaperAndPrintingHouse(Newspaper newspaper, PrintingHouse printingHouse) {
        return printRunRepository.findPrintRunsByNewspaperAndPrintingHouse(newspaper, printingHouse);
    }
}
