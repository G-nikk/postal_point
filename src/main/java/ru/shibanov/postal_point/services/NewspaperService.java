package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.Newspaper;
import ru.shibanov.postal_point.repositories.NewspaperRepository;

@Service
@Transactional
public class NewspaperService {
    private final NewspaperRepository newspaperRepository;

    @Autowired
    public NewspaperService(NewspaperRepository newspaperRepository) {
        this.newspaperRepository = newspaperRepository;
    }

    @Transactional
    public void save(Newspaper newspaper) {
        newspaperRepository.save(newspaper);
    }
}
