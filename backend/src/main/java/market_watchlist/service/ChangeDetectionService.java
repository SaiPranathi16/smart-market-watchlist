package market_watchlist.service;

import market_watchlist.model.ChangeAnalysis;
import market_watchlist.model.MarketData;
import market_watchlist.model.UserStockState;
import market_watchlist.repository.UserStockStateRepository;
import org.springframework.stereotype.Service;

@Service
public class ChangeDetectionService {

    private final MarketDataService marketDataService;
    private final UserStockStateRepository stateRepository;

    public ChangeDetectionService(
            MarketDataService marketDataService,
            UserStockStateRepository stateRepository) {

        this.marketDataService = marketDataService;
        this.stateRepository = stateRepository;
    }

    public ChangeAnalysis analyze(String symbol) {

        symbol = symbol.toUpperCase().trim();

        MarketData current = marketDataService.getCurrentData(symbol);

        var existingState = stateRepository.findBySymbol(symbol);

        // First observation: establish the baseline.
        if (existingState.isEmpty()) {

            UserStockState state = new UserStockState(
                    symbol,
                    current.getPrice(),
                    current.getVolume(),
                    current.getTimestamp()
            );

            stateRepository.save(state);

            return new ChangeAnalysis(
                    symbol,
                    current.getPrice(),
                    0,
                    current.getVolume(),
                    0,
                    0,
                    "BASELINE",
                    "Baseline established. Changes will be tracked from the next session."
            );
        }

        UserStockState baseline = existingState.get();

        // Calculate percentage changes from the saved baseline.
        double priceChange =
                ((current.getPrice() - baseline.getBaselinePrice())
                        / baseline.getBaselinePrice()) * 100;

        double volumeChange =
                ((double) (current.getVolume() - baseline.getBaselineVolume())
                        / baseline.getBaselineVolume()) * 100;

        // Round values for cleaner API responses.
        priceChange = Math.round(priceChange * 100.0) / 100.0;
        volumeChange = Math.round(volumeChange * 100.0) / 100.0;

        /*
         * Attention scoring:
         * Price movement contributes up to 50 points.
         * Volume movement contributes up to 30 points.
         */
        double priceScore =
                Math.min(Math.abs(priceChange) / 5.0 * 50, 50);

        double volumeScore =
                Math.min(Math.abs(volumeChange) / 100.0 * 30, 30);

        double totalScore = priceScore + volumeScore;

        String attentionLevel;

        if (totalScore >= 60) {
            attentionLevel = "HIGH";
        } else if (totalScore >= 30) {
            attentionLevel = "MEDIUM";
        } else {
            attentionLevel = "LOW";
        }

        String explanation;

        if (Math.abs(priceChange) >= 3
        && volumeChange >= 50) {

    explanation =
            "Significant price movement accompanied by elevated volume.";

} else if (Math.abs(priceChange) >= 3) {

    explanation =
            "Price moved significantly since your last session.";

} else if (volumeChange >= 50) {

    explanation =
            "Trading volume is unusually elevated compared with the baseline.";

} else if (volumeChange <= -50) {

    explanation =
            "Trading volume has decreased significantly compared with the baseline.";

} else {

    explanation =
            "No material change detected.";
}

        return new ChangeAnalysis(
                symbol,
                current.getPrice(),
                priceChange,
                current.getVolume(),
                volumeChange,
                totalScore,
                attentionLevel,
                explanation
        );
    }
}