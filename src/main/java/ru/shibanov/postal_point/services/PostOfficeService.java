package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.PostOffice;
import ru.shibanov.postal_point.repositories.PostOfficeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostOfficeService {
    private final PostOfficeRepository postOfficeRepository;

    @Autowired
    public PostOfficeService(PostOfficeRepository postOfficeRepository) {
        this.postOfficeRepository = postOfficeRepository;
    }

    @Transactional(readOnly = true)
    public List<PostOffice> getAllPostOffices() {
        return postOfficeRepository.findAll();
    }

    @Transactional
    public PostOffice save(PostOffice postOffice) {
        postOfficeRepository.save(postOffice);
        return postOffice;
    }

    public List<PostOffice> findAll() {
        return postOfficeRepository.findAll();
    }

    public Optional<PostOffice> findById(Integer id) {
        return postOfficeRepository.findById(id);
    }

    public boolean existsById(Integer id) {
        return postOfficeRepository.existsById(id);
    }

    public void deleteById(Integer id) {
        postOfficeRepository.deleteById(id);
    }
}
