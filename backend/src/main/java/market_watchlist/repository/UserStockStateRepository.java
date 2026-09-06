
package market_watchlist.repository;

import market_watchlist.model.UserStockState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStockStateRepository
        extends JpaRepository<UserStockState, Long> {

    Optional<UserStockState> findBySymbol(String symbol);
}