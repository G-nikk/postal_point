package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.Delivery;
import ru.shibanov.postal_point.entities.PostOffice;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.repositories.DeliveryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Transactional
    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    public List<Delivery> findByPostOffice(PostOffice postOffice) {
        return deliveryRepository.findDeliveriesByPostOffice(postOffice);
    }

    public List<Delivery> findDeliveriesByPrintRun(PrintRun printRun) {
        return deliveryRepository.findDeliveriesByPrintRun(printRun);
    }
}
