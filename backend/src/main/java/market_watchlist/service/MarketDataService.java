package market_watchlist.service;

import market_watchlist.model.MarketData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MarketDataService {

    private final Random random = new Random();

    private final Map<String, Double> prices = new ConcurrentHashMap<>();
    private final Map<String, Long> baseVolumes = new ConcurrentHashMap<>();

    public MarketData getCurrentData(String symbol) {

        symbol = symbol.toUpperCase().trim();

        double currentPrice =
                prices.computeIfAbsent(symbol, this::initialPrice);

        long baseVolume =
                baseVolumes.computeIfAbsent(symbol, this::initialVolume);

        /*
         * Three types of simulated market behaviour:
         *
         * NORMAL     -> small movement
         * MODERATE   -> noticeable movement
         * SIGNIFICANT -> strong movement + volume spike
         */

        double priceMovement;
        long currentVolume;

        double event = random.nextDouble();

        if (event < 0.10) {

            // Significant market event
            priceMovement =
                    (random.nextBoolean() ? 1 : -1)
                            * (0.035 + random.nextDouble() * 0.02);

            currentVolume =
                    (long) (baseVolume
                            * (2.0 + random.nextDouble()));

        } else if (event < 0.30) {

            // Moderate market movement
            priceMovement =
                    (random.nextBoolean() ? 1 : -1)
                            * (0.015 + random.nextDouble() * 0.01);

            currentVolume =
                    (long) (baseVolume
                            * (1.25 + random.nextDouble() * 0.55));

        } else {

            // Normal market movement
            priceMovement =
                    (random.nextDouble() - 0.5) * 0.02;

            double volumeFactor =
                    0.85 + (random.nextDouble() * 0.30);

            currentVolume =
                    (long) (baseVolume * volumeFactor);
        }

        currentPrice =
                currentPrice * (1 + priceMovement);

        prices.put(symbol, currentPrice);

        return new MarketData(
                symbol,
                Math.round(currentPrice * 100.0) / 100.0,
                currentVolume,
                LocalDateTime.now()
        );
    }

    private double initialPrice(String symbol) {

        return switch (symbol) {

            case "RELIANCE" -> 2850.0;
            case "TCS" -> 4200.0;
            case "INFY" -> 1900.0;
            case "HDFCBANK" -> 1750.0;
            case "ICICIBANK" -> 1350.0;

            default -> 1000.0;
        };
    }

    private long initialVolume(String symbol) {

        return 100000 + random.nextInt(100000);
    }
}