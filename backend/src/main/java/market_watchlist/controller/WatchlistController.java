
package market_watchlist.controller;

import market_watchlist.model.WatchlistStock;
import market_watchlist.repository.WatchlistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistRepository repository;

    public WatchlistController(WatchlistRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<WatchlistStock> getWatchlist() {
        return repository.findAll();
    }

    @PostMapping
    public WatchlistStock addStock(@RequestBody WatchlistStock stock) {
        stock.setSymbol(stock.getSymbol().toUpperCase().trim());
        return repository.save(stock);
    }

    @DeleteMapping("/{symbol}")
    public ResponseEntity<Void> deleteStock(@PathVariable String symbol) {
        return repository.findAll().stream()
                .filter(stock -> stock.getSymbol().equalsIgnoreCase(symbol))
                .findFirst()
                .map(stock -> {
                    repository.delete(stock);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}