package ru.shibanov.postal_point.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shibanov.postal_point.entities.PrintingHouse;

@Repository
public interface PrintingHouseRepository extends JpaRepository<PrintingHouse, Integer> {
}