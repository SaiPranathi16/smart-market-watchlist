
package market_watchlist.controller;

import market_watchlist.model.ChangeAnalysis;
import market_watchlist.model.WatchlistStock;
import market_watchlist.repository.WatchlistRepository;
import market_watchlist.service.ChangeDetectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final WatchlistRepository watchlistRepository;
    private final ChangeDetectionService changeDetectionService;

    public DashboardController(
            WatchlistRepository watchlistRepository,
            ChangeDetectionService changeDetectionService) {

        this.watchlistRepository = watchlistRepository;
        this.changeDetectionService = changeDetectionService;
    }

    @GetMapping
    public List<ChangeAnalysis> getDashboard() {

        List<WatchlistStock> stocks = watchlistRepository.findAll();

        return stocks.stream()
                .map(stock -> changeDetectionService.analyze(stock.getSymbol()))
                .toList();
    }
}