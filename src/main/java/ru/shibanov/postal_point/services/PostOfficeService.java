package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.PostOffice;
import ru.shibanov.postal_point.repositories.PostOfficeRepository;

import java.util.List;

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
    public void save(PostOffice postOffice) {
        postOfficeRepository.save(postOffice);
    }
}
