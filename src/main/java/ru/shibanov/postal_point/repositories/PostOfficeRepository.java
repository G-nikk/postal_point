package ru.shibanov.postal_point.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shibanov.postal_point.entities.PostOffice;

@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Integer> {
}