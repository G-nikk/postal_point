package ru.shibanov.postal_point.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shibanov.postal_point.entities.*;
import ru.shibanov.postal_point.services.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            int totalQuantity = deliveryService.findByPostOffice(postOffice).stream()
                    .mapToInt(Delivery::getQuantity)
                    .sum();
            if (totalQuantity > maxQuantity) {
                maxQuantity = totalQuantity;
                mostReceivedOffice = postOffice;
            }
        }

        return mostReceivedOffice != null ? new ResponseEntity<>(mostReceivedOffice, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // GET /post-offices/newspaper-in-printing-house?newspaperId={newspaperId}&printingHouseId={printingHouseId} - Get post offices receiving a newspaper from a printing house
    @GetMapping("/newspaper-in-printing-house")
    public ResponseEntity<List<PostOffice>> getPostOfficesForNewspaperInPrintingHouse(@RequestParam Integer newspaperId, @RequestParam Integer printingHouseId) {
        Optional<Newspaper> newspaperOptional = Optional.ofNullable(newspaperService.findById(newspaperId));
        Optional<PrintingHouse> printingHouseOptional = Optional.ofNullable(printingHouseService.findById(printingHouseId));

        if (!newspaperOptional.isPresent() || !printingHouseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Newspaper newspaper = newspaperOptional.get();
        PrintingHouse printingHouse = printingHouseOptional.get();

        List<PrintRun> printRuns = printRunService.findPrintRunsByNewspaperAndPrintingHouse(newspaper, printingHouse);

        List<PostOffice> postOffices = printRuns.stream()
                .flatMap(printRun -> deliveryService.findDeliveriesByPrintRun(printRun).stream())
                .map(Delivery::getPostOffice)
                .distinct()
                .collect(Collectors.toList());

        return new ResponseEntity<>(postOffices, HttpStatus.OK);
    }

    // GET /post-offices/max-cost - Get the post office with the maximum total cost of received newspapers
    @GetMapping("/max-cost")
    public ResponseEntity<PostOffice> getPostOfficeWithMaxCost() {
        List<PostOffice> postOffices = postOfficeService.findAll();
        PostOffice maxCostOffice = null;
        BigDecimal maxCost = BigDecimal.ZERO;

        for (PostOffice postOffice : postOffices) {
            BigDecimal totalCostForOffice = deliveryService.findByPostOffice(postOffice).stream()
                    .map(delivery -> {
                        Optional<PrintRun> printRunOptional = printRunService.findById(delivery.getPrintRun().getPrintRunID());
                        if (printRunOptional.isPresent()) {
                            PrintRun printRun = printRunOptional.get();
                            Newspaper newspaper = printRun.getNewspaper();
                            return newspaper.getPrice().multiply(BigDecimal.valueOf(delivery.getQuantity()));
                        }
                        return BigDecimal.ZERO;
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (totalCostForOffice.compareTo(maxCost) > 0) {
                maxCost = totalCostForOffice;
                maxCostOffice = postOffice;
            }
        }

        return maxCostOffice != null ? new ResponseEntity<>(maxCostOffice, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
