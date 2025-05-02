package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.repositories.PrintRunRepository;

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
}
