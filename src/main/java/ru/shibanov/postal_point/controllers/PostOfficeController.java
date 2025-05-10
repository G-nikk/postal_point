package ru.shibanov.postal_point.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shibanov.postal_point.entities.*;
import ru.shibanov.postal_point.services.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/post-offices")
public class PostOfficeController {

    private final PostOfficeService postOfficeService;
    private final DeliveryService deliveryService;
    private final PrintRunService printRunService;
    private final NewspaperService newspaperService;
    private final PrintingHouseService printingHouseService;

    public PostOfficeController(PostOfficeService postOfficeService, DeliveryService deliveryService,
                                PrintRunService printRunService, NewspaperService newspaperService, PrintingHouseService printingHouseService) {
        this.postOfficeService = postOfficeService;
        this.deliveryService = deliveryService;
        this.printRunService = printRunService;
        this.newspaperService = newspaperService;
        this.printingHouseService = printingHouseService;
    }

    // GET /post-offices - Get all post offices
    @GetMapping
    public List<PostOffice> getAllPostOffices() {
        return postOfficeService.findAll();
    }

    // GET /post-offices/{id} - Get a specific post office by ID
    @GetMapping("/{id}")
    public ResponseEntity<PostOffice> getPostOfficeById(@PathVariable Integer id) {
        Optional<PostOffice> postOffice = postOfficeService.findById(id);
        return postOffice.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST /post-offices - Create a new post office
    @PostMapping
    public ResponseEntity<PostOffice> createPostOffice(@RequestBody PostOffice postOffice) {
        PostOffice savedPostOffice = postOfficeService.save(postOffice);
        return new ResponseEntity<>(savedPostOffice, HttpStatus.CREATED);
    }

    // PUT /post-offices/{id} - Update an existing post office
    @PutMapping("/{id}")
    public ResponseEntity<PostOffice> updatePostOffice(@PathVariable Integer id, @RequestBody PostOffice updatedPostOffice) {
        if (!postOfficeService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedPostOffice.setPostOfficeID(id); // Ensure the ID is set for the update
        PostOffice savedPostOffice = postOfficeService.save(updatedPostOffice);
        return new ResponseEntity<>(savedPostOffice, HttpStatus.OK);
    }

    // DELETE /post-offices/{id} - Delete a post office
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostOffice(@PathVariable Integer id) {
        if (!postOfficeService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        postOfficeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET /post-offices/most-received - Get the post office receiving the most newspapers
    @GetMapping("/most-received")
    public ResponseEntity<PostOffice> getPostOfficeWithMostNewspapers() {
        List<PostOffice> postOffices = postOfficeService.findAll();
        PostOffice mostReceivedOffice = null;
        int maxQuantity = 0;

        for (PostOffice postOffice : postOffices) {
            List<Delivery> deliveries = deliveryService.findByPostOffice(postOffice);
            int totalQuantity = 0;

            // Обычный цикл для подсчета суммы количеств поставок
            for (Delivery delivery : deliveries) {
                totalQuantity += delivery.getQuantity();
            }

            if (totalQuantity > maxQuantity) {
                maxQuantity = totalQuantity;
                mostReceivedOffice = postOffice;
            }
        }

        return mostReceivedOffice != null ?
                new ResponseEntity<>(mostReceivedOffice, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // GET /post-offices/newspaper-in-printing-house?newspaperId={newspaperId}&printingHouseId={printingHouseId} - Get post offices receiving a newspaper from a printing house
    @GetMapping("/newspaper-in-printing-house")
    public ResponseEntity<List<PostOffice>> getPostOfficesForNewspaperInPrintingHouse(
            @RequestParam Integer newspaperId,
            @RequestParam Integer printingHouseId) {

        // Проверка существования объектов
        Newspaper newspaper = newspaperService.findById(newspaperId);
        PrintingHouse printingHouse = printingHouseService.findById(printingHouseId);

        if (newspaper == null || printingHouse == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Получаем печать (тиражи) для данной газеты и типографии
        List<PrintRun> printRuns = printRunService.findPrintRunsByNewspaperAndPrintingHouse(newspaper, printingHouse);

        Set<PostOffice> uniquePostOffices = new HashSet<>();

        // Проходим по каждому PrintRun'у и собираем уникальные почтовые отделения
        for (PrintRun printRun : printRuns) {
            List<Delivery> deliveries = deliveryService.findDeliveriesByPrintRun(printRun);
            for (Delivery delivery : deliveries) {
                uniquePostOffices.add(delivery.getPostOffice());
            }
        }

        // Возвращаем список уникальных почтовых отделений
        return new ResponseEntity<>(new ArrayList<>(uniquePostOffices), HttpStatus.OK);
    }

    // GET /post-offices/max-cost - Get the post office with the maximum total cost of received newspapers
    @GetMapping("/max-cost")
    public ResponseEntity<PostOffice> getPostOfficeWithMaxCost() {
        List<PostOffice> postOffices = postOfficeService.findAll();
        PostOffice maxCostOffice = null;
        BigDecimal maxCost = BigDecimal.ZERO;

        for (PostOffice postOffice : postOffices) {
            List<Delivery> deliveries = deliveryService.findByPostOffice(postOffice);
            BigDecimal totalCostForOffice = BigDecimal.ZERO;

            for (Delivery delivery : deliveries) {
                // Предполагается, что PrintRun обязательно существует
                PrintRun printRun = printRunService.findById(delivery.getPrintRun().getPrintRunID());
                Newspaper newspaper = printRun.getNewspaper();
                BigDecimal pricePerUnit = newspaper.getPrice();
                long quantity = delivery.getQuantity();

                totalCostForOffice = totalCostForOffice.add(pricePerUnit.multiply(BigDecimal.valueOf(quantity)));
            }

            if (totalCostForOffice.compareTo(maxCost) > 0) {
                maxCost = totalCostForOffice;
                maxCostOffice = postOffice;
            }
        }

        return maxCostOffice != null ? new ResponseEntity<>(maxCostOffice, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
