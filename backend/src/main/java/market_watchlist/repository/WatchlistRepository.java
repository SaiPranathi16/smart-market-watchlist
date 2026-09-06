
package market_watchlist.repository;

import market_watchlist.model.WatchlistStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepository extends JpaRepository<WatchlistStock, Long> {
}
