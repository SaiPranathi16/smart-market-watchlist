
package market_watchlist.controller;

import market_watchlist.model.MarketData;
import market_watchlist.model.UserStockState;
import market_watchlist.repository.UserStockStateRepository;
import market_watchlist.service.MarketDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final UserStockStateRepository stateRepository;
    private final MarketDataService marketDataService;

    public SessionController(
            UserStockStateRepository stateRepository,
            MarketDataService marketDataService) {
        this.stateRepository = stateRepository;
        this.marketDataService = marketDataService;
    }

    @PostMapping("/finish")
    public String finishSession() {

        List<UserStockState> states = stateRepository.findAll();

        for (UserStockState state : states) {

            MarketData current =
                    marketDataService.getCurrentData(state.getSymbol());

            state.setBaselinePrice(current.getPrice());
            state.setBaselineVolume(current.getVolume());
            state.setBaselineTimestamp(current.getTimestamp());

            stateRepository.save(state);
        }

        return "Session completed. Current market state is now the new baseline.";
    }
}