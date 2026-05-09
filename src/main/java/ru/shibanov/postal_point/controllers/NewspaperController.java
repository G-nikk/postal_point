package ru.shibanov.postal_point.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shibanov.postal_point.entities.Newspaper;
import ru.shibanov.postal_point.entities.PrintRun;
import ru.shibanov.postal_point.entities.PrintingHouse;
import ru.shibanov.postal_point.services.NewspaperService;
import ru.shibanov.postal_point.services.PrintRunService;
import ru.shibanov.postal_point.services.PrintingHouseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/newspapers") //feature1 изменение
public class NewspaperController {
    private final NewspaperService newspaperService;
    private final PrintRunService printRunService;
    private final PrintingHouseService printingHouseService;

    @Autowired
    public NewspaperController(final NewspaperService newspaperService, PrintRunService printRunService, PrintingHouseService printingHouseService) {
        this.newspaperService = newspaperService;
        this.printRunService = printRunService;
        this.printingHouseService = printingHouseService;
    }

    // GET /newspapers - Get all newspapers
    @GetMapping
    public List<Newspaper> getAllNewspapers() {
        return newspaperService.findAll();
    }

    // GET /newspapers/{id} - Get a specific newspaper by ID
    @GetMapping("/{id}")
    public ResponseEntity<Newspaper> getNewspaperById(@PathVariable Integer id) {
        Optional<Newspaper> newspaper = Optional.ofNullable(newspaperService.findById(id));
        return newspaper.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Newspaper with id " + id + " not found"));
    }

    // POST /newspapers - Create a new newspaper
    @PostMapping
    public ResponseEntity<Newspaper> createNewspaper(@RequestBody Newspaper newspaper) {
        try {
            Newspaper savedNewspaper = newspaperService.save(newspaper);
            return new ResponseEntity<>(savedNewspaper, HttpStatus.CREATED);
        } catch (Exception e) {
            String msg = (e.getMessage() == null || e.getMessage().isBlank()) ? "Bad request" : e.getMessage();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
        }

    }

    // PUT /newspapers/{id} - Update an existing newspaper
    @PutMapping("/{id}")
    public ResponseEntity<Newspaper> updateNewspaper(@PathVariable Integer id, @RequestBody Newspaper updatedNewspaper) {
        if (!newspaperService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Newspaper with id " + id + " not found");
        }
        updatedNewspaper.setNewspaperID(id); // Ensure the ID is set for the update
        Newspaper savedNewspaper = newspaperService.save(updatedNewspaper);
        return new ResponseEntity<>(savedNewspaper, HttpStatus.OK);
    }

    // DELETE /newspapers/{id} - Delete a newspaper
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewspaper(@PathVariable Integer id) {
        if (!newspaperService.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Newspaper with id " + id + " not found");
        }
        newspaperService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET /newspapers/{newspaperId}/printing-houses - Get printing houses for a given newspaper
    @GetMapping("/{newspaperId}/printing-houses")
    public ResponseEntity<List<PrintingHouse>> getPrintingHousesForNewspaper(@PathVariable Integer newspaperId) {
        Optional<Newspaper> newspaper = Optional.ofNullable(newspaperService.findById(newspaperId));
        Newspaper newspaperEntity = newspaper.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Newspaper with id " + newspaperId + " not found"));

        List<PrintRun> printRuns = printRunService.findByNewspaper(newspaperEntity);
        List<PrintingHouse> printingHouses = printRuns.stream()
                .map(PrintRun::getPrintingHouse)
                .distinct()
                .collect(Collectors.toList());

        return new ResponseEntity<>(printingHouses, HttpStatus.OK);
    }

    // GET /newspapers/editor?printingHouseId={printingHouseId}&newspaperId={newspaperId} - Get the editor for a newspaper in a specific printing house
    @GetMapping("/editor")
    public ResponseEntity<String> getEditorForLargestPrintRun(@RequestParam Integer printingHouseId, @RequestParam Integer newspaperId) {
        Optional<PrintingHouse> printingHouseOptional = Optional.ofNullable(printingHouseService.findById(printingHouseId));
        Optional<Newspaper> newspaperOptional = Optional.ofNullable(newspaperService.findById(newspaperId));

        PrintingHouse printingHouse = printingHouseOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Printing house with id " + printingHouseId + " not found"));
        Newspaper newspaper = newspaperOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Newspaper with id " + newspaperId + " not found"));

        Optional<PrintRun> largestPrintRun = printRunService.findTopByPrintingHouseAndNewspaperOrderByQuantityDesc(printingHouse, newspaper);

        if (largestPrintRun.isPresent()) {
            return new ResponseEntity<>(newspaper.getEditor(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No print runs found for the given printing house and newspaper");
        }
    }

    // GET /newspapers/total-cost?newspaperId={newspaperId} - Get total cost for a newspaper's print runs
    @GetMapping("/total-cost")
    public ResponseEntity<BigDecimal> getTotalCostOfPrintRuns(@RequestParam Integer newspaperId) {
        Optional<Newspaper> newspaperOptional = Optional.ofNullable(newspaperService.findById(newspaperId));
        Newspaper newspaper = newspaperOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Newspaper with id " + newspaperId + " not found"));
        List<PrintRun> printRuns = printRunService.findByNewspaper(newspaper);

        BigDecimal totalCost = printRuns.stream()
                .map(printRun -> newspaper.getPrice().multiply(BigDecimal.valueOf(printRun.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResponseEntity<>(totalCost, HttpStatus.OK);
    }
}

