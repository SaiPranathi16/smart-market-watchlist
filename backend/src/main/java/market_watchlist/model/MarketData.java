
package market_watchlist.model;

import java.time.LocalDateTime;

public class MarketData {

    private String symbol;
    private double price;
    private long volume;
    private LocalDateTime timestamp;

    public MarketData() {
    }

    public MarketData(String symbol, double price, long volume, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public long getVolume() {
        return volume;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}