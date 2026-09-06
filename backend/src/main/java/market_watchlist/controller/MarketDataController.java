
package market_watchlist.controller;

import market_watchlist.model.MarketData;
import market_watchlist.service.MarketDataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
public class MarketDataController {

    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/{symbol}")
    public MarketData getMarketData(@PathVariable String symbol) {
        return marketDataService.getCurrentData(symbol);
    }
}