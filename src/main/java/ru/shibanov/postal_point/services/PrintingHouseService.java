package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.PrintingHouse;
import ru.shibanov.postal_point.repositories.PrintingHouseRepository;

@Service
@Transactional
public class PrintingHouseService {
    private final PrintingHouseRepository printingHouseRepository;

    @Autowired
    public PrintingHouseService(PrintingHouseRepository printingHouseRepository) {
        this.printingHouseRepository = printingHouseRepository;
    }

    @Transactional
    public void save(PrintingHouse printingHouse) {
        printingHouseRepository.save(printingHouse);
    }

    @Transactional
    public PrintingHouse findById(int id) {
        return printingHouseRepository.findById(id).orElse(null);
    }

}
