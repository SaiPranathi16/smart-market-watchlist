
package market_watchlist.model;

import jakarta.persistence.*;

@Entity
@Table(name = "watchlist_stocks")
public class WatchlistStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String symbol;

    public WatchlistStock() {
    }

    public WatchlistStock(String symbol) {
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}