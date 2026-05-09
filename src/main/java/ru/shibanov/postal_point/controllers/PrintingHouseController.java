package ru.shibanov.postal_point.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.entities.PrintingHouse;
import ru.shibanov.postal_point.services.PrintingHouseService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/printing-houses")
@CrossOrigin(origins = "http://localhost:3000")
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Printing house with id " + id + " not found"));
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Printing house with id " + id + " not found");
        }
        updatedPrintingHouse.setPrintingHouseID(id); // Ensure the ID is set for the update
        PrintingHouse savedPrintingHouse = printingHouseService.save(updatedPrintingHouse);
        return new ResponseEntity<>(savedPrintingHouse, HttpStatus.OK);
    }

    // DELETE /printing-houses/{id} - Delete a printing house
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrintingHouse(@PathVariable Integer id) {
        if (!printingHouseService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Printing house with id " + id + " not found");
        }
        printingHouseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/newspapers")
    public ResponseEntity<List<PrintRun>> getNewspapersForPrintingHouse(@PathVariable Integer id) {
        Optional<PrintingHouse> ph = Optional.ofNullable(printingHouseService.findById(id));
        PrintingHouse printingHouse = ph.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Printing house with id " + id + " not found"));
        return new ResponseEntity<>(printingHouse.getPrintRuns(), HttpStatus.OK);
    }

    @GetMapping("/{id}/max-editor")
    public ResponseEntity<String> getEditorWithMaxPrintRun(@PathVariable Integer id) {
        Optional<PrintingHouse> ph = Optional.ofNullable(printingHouseService.findById(id));
        PrintingHouse printingHouse = ph.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Printing house with id " + id + " not found"));

        Optional<PrintRun> maxRun = printingHouse.getPrintRuns().stream()
                .max(Comparator.comparingInt(PrintRun::getQuantity));

        return maxRun.map(run ->
                        new ResponseEntity<>(run.getNewspaper().getEditor(), HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No print runs found for printing house with id " + id));
    }
}

