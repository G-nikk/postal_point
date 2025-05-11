package ru.shibanov.postal_point.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shibanov.postal_point.entities.PrintingHouse;
import ru.shibanov.postal_point.services.PrintingHouseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/printing-houses")
@CrossOrigin(origins = "http://localhost:5173")
public class PrintingHouseController {

    private final PrintingHouseService printingHouseService;

    @Autowired
    public PrintingHouseController(PrintingHouseService printingHouseService) {
        this.printingHouseService = printingHouseService;
    }

    // GET /printing-houses - Get all printing houses
    @GetMapping
    public List<PrintingHouse> getAllPrintingHouses() {
        return printingHouseService.findAll();
    }

    // GET /printing-houses/{id} - Get a specific printing house by ID
    @GetMapping("/{id}")
    public ResponseEntity<PrintingHouse> getPrintingHouseById(@PathVariable Integer id) {
        Optional<PrintingHouse> printingHouse = Optional.ofNullable(printingHouseService.findById(id));
        return printingHouse.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST /printing-houses - Create a new printing house
    @PostMapping
    public ResponseEntity<PrintingHouse> createPrintingHouse(@RequestBody PrintingHouse printingHouse) {
        PrintingHouse savedPrintingHouse = printingHouseService.save(printingHouse);
        return new ResponseEntity<>(savedPrintingHouse, HttpStatus.CREATED);
    }

    // PUT /printing-houses/{id} - Update an existing printing house
    @PutMapping("/{id}")
    public ResponseEntity<PrintingHouse> updatePrintingHouse(@PathVariable Integer id, @RequestBody PrintingHouse updatedPrintingHouse) {
        if (!printingHouseService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedPrintingHouse.setPrintingHouseID(id); // Ensure the ID is set for the update
        PrintingHouse savedPrintingHouse = printingHouseService.save(updatedPrintingHouse);
        return new ResponseEntity<>(savedPrintingHouse, HttpStatus.OK);
    }

    // DELETE /printing-houses/{id} - Delete a printing house
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrintingHouse(@PathVariable Integer id) {
        if (!printingHouseService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        printingHouseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

