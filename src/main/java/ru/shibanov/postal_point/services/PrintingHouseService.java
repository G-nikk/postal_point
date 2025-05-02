package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.PrintingHouse;
import ru.shibanov.postal_point.repositories.PrintingHouseRepository;

import java.util.List;

@Service
@Transactional
public class PrintingHouseService {
    private final PrintingHouseRepository printingHouseRepository;

    @Autowired
    public PrintingHouseService(PrintingHouseRepository printingHouseRepository) {
        this.printingHouseRepository = printingHouseRepository;
    }

    @Transactional
    public PrintingHouse save(PrintingHouse printingHouse) {
        printingHouseRepository.save(printingHouse);
        return printingHouse;
    }

    @Transactional
    public PrintingHouse findById(int id) {
        return printingHouseRepository.findById(id).orElse(null);
    }

    public List<PrintingHouse> findAll() {
        return printingHouseRepository.findAll();
    }

    public boolean existsById(Integer id) {
        return printingHouseRepository.existsById(id);
    }

    public void deleteById(Integer id) {
        printingHouseRepository.deleteById(id);
    }
}
