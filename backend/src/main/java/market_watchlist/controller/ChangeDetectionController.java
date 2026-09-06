
package market_watchlist.controller;

import market_watchlist.model.ChangeAnalysis;
import market_watchlist.service.ChangeDetectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/changes")
public class ChangeDetectionController {

    private final ChangeDetectionService changeDetectionService;

    public ChangeDetectionController(
            ChangeDetectionService changeDetectionService) {

        this.changeDetectionService = changeDetectionService;
    }

    @GetMapping("/{symbol}")
    public ChangeAnalysis analyze(@PathVariable String symbol) {
        return changeDetectionService.analyze(symbol);
    }
}