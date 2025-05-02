package ru.shibanov.postal_point.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shibanov.postal_point.entities.Delivery;
import ru.shibanov.postal_point.entities.PostOffice;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.repositories.DeliveryRepository;

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

    public Optional<Object> findByPostOffice(PostOffice postOffice) {
        return Optional.ofNullable(deliveryRepository.findDeliveriesByPostOffice(postOffice));
    }

    public Optional<Object> findDeliveriesByPrintRun(PrintRun printRun) {
        return Optional.ofNullable(deliveryRepository.findDeliveriesByPrintRun(printRun));
    }
}
