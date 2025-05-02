package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.Newspaper;
import ru.shibanov.postal_point.repositories.NewspaperRepository;

import java.util.List;

@Service
@Transactional
public class NewspaperService {
    private final NewspaperRepository newspaperRepository;

    @Autowired
    public NewspaperService(NewspaperRepository newspaperRepository) {
        this.newspaperRepository = newspaperRepository;
    }

    @Transactional
    public Newspaper save(Newspaper newspaper) {
        newspaperRepository.save(newspaper);
        return newspaper;
    }

    @Transactional(readOnly = true)
    public List<Newspaper> findAll() {
        return newspaperRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Newspaper findById(int id) {
        return newspaperRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existsById(int id) {
        return newspaperRepository.existsById(id);
    }

    @Transactional
    public void deleteById(int id) {
        newspaperRepository.deleteById(id);
    }


}
