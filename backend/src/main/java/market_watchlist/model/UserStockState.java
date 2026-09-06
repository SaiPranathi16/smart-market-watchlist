
package market_watchlist.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_stock_state")
public class UserStockState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(nullable = false, unique = true)
private String symbol;

    @Column(nullable = false)
    private double baselinePrice;

    @Column(nullable = false)
    private long baselineVolume;

    @Column(nullable = false)
    private LocalDateTime baselineTimestamp;

   

    public UserStockState() {
    }

    public UserStockState(
            String symbol,
            double baselinePrice,
            long baselineVolume,
            LocalDateTime baselineTimestamp) {

        this.symbol = symbol;
        this.baselinePrice = baselinePrice;
        this.baselineVolume = baselineVolume;
        this.baselineTimestamp = baselineTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getBaselinePrice() {
        return baselinePrice;
    }

    public long getBaselineVolume() {
        return baselineVolume;
    }

    public LocalDateTime getBaselineTimestamp() {
        return baselineTimestamp;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setBaselinePrice(double baselinePrice) {
        this.baselinePrice = baselinePrice;
    }

    public void setBaselineVolume(long baselineVolume) {
        this.baselineVolume = baselineVolume;
    }

    public void setBaselineTimestamp(LocalDateTime baselineTimestamp) {
        this.baselineTimestamp = baselineTimestamp;
    }
}