package ru.shibanov.postal_point.newspaper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/newspapers")
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
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST /newspapers - Create a new newspaper
    @PostMapping
    public ResponseEntity<Newspaper> createNewspaper(@RequestBody Newspaper newspaper) {
        try {
            Newspaper savedNewspaper = newspaperService.save(newspaper);
            return new ResponseEntity<>(savedNewspaper, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Or handle the specific exception (e.g., unique constraint violation)
        }

    }

    // PUT /newspapers/{id} - Update an existing newspaper
    @PutMapping("/{id}")
    public ResponseEntity<Newspaper> updateNewspaper(@PathVariable Integer id, @RequestBody Newspaper updatedNewspaper) {
        if (!newspaperService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedNewspaper.setNewspaperID(id); // Ensure the ID is set for the update
        Newspaper savedNewspaper = newspaperService.save(updatedNewspaper);
        return new ResponseEntity<>(savedNewspaper, HttpStatus.OK);
    }

    // DELETE /newspapers/{id} - Delete a newspaper
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewspaper(@PathVariable Integer id) {
        if (!newspaperService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newspaperService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // GET /newspapers/{newspaperId}/printing-houses - Get printing houses for a given newspaper
    @GetMapping("/{newspaperId}/printing-houses")
    public ResponseEntity<List<PrintingHouse>> getPrintingHousesForNewspaper(@PathVariable Integer newspaperId) {
        Optional<Newspaper> newspaper = Optional.ofNullable(newspaperService.findById(newspaperId));
        if (!newspaper.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<PrintRun> printRuns = printRunService.findByNewspaper(newspaper.get());
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

        if (!printingHouseOptional.isPresent() || !newspaperOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PrintingHouse printingHouse = printingHouseOptional.get();
        Newspaper newspaper = newspaperOptional.get();

        Optional<PrintRun> largestPrintRun = printRunService.findTopByPrintingHouseAndNewspaperOrderByQuantityDesc(printingHouse, newspaper);

        if (largestPrintRun.isPresent()) {
            return new ResponseEntity<>(newspaper.getEditor(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Or return an empty string, indicating no print runs.
        }
    }

    // GET /newspapers/total-cost?newspaperId={newspaperId} - Get total cost for a newspaper's print runs
    @GetMapping("/total-cost")
    public ResponseEntity<BigDecimal> getTotalCostOfPrintRuns(@RequestParam Integer newspaperId) {
        Optional<Newspaper> newspaperOptional = Optional.ofNullable(newspaperService.findById(newspaperId));
        if (!newspaperOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Newspaper newspaper = newspaperOptional.get();
        List<PrintRun> printRuns = printRunService.findByNewspaper(newspaper);

        BigDecimal totalCost = printRuns.stream()
                .map(printRun -> newspaper.getPrice().multiply(BigDecimal.valueOf(printRun.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResponseEntity<>(totalCost, HttpStatus.OK);
    }
}

