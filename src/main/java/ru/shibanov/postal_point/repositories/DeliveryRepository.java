package ru.shibanov.postal_point.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shibanov.postal_point.entities.Delivery;
import ru.shibanov.postal_point.entities.PostOffice;
import ru.shibanov.postal_point.entities.PrintRun;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    List<Delivery> findDeliveriesByPrintRun(PrintRun printRun);

    List<Delivery> findDeliveriesByPostOffice(PostOffice postOffice);
}