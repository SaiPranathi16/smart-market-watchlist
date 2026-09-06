import { useEffect, useState } from "react";
import "./App.css";


const API_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8080/api";
function App() {
  const [stocks, setStocks] = useState([]);
  const [symbol, setSymbol] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [lastUpdated, setLastUpdated] = useState(null);
  const [sessionMessage, setSessionMessage] = useState("");

  const loadDashboard = async () => {
    try {
      setError("");

      const response = await fetch(`${API_URL}/dashboard`);

      if (!response.ok) {
        throw new Error("Unable to load market data");
      }

      const data = await response.json();

      setStocks(data);
      setLastUpdated(new Date());
    } catch (err) {
      setError("Backend is currently unavailable. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboard();

    const interval = setInterval(loadDashboard, 15000);

    return () => clearInterval(interval);
  }, []);

  const addStock = async () => {
    const cleanSymbol = symbol.trim().toUpperCase();

    if (!cleanSymbol) return;

    try {
      setError("");
      setSessionMessage("");

      const response = await fetch(`${API_URL}/watchlist`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          symbol: cleanSymbol,
        }),
      });

      if (!response.ok) {
        throw new Error("Unable to add stock");
      }

      setSymbol("");
      await loadDashboard();
    } catch (err) {
      setError("Unable to add this stock.");
    }
  };

  const removeStock = async (stockSymbol) => {
    try {
      setError("");
      setSessionMessage("");

      const response = await fetch(
        `${API_URL}/watchlist/${stockSymbol}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok && response.status !== 404) {
        throw new Error("Unable to remove stock");
      }

      await loadDashboard();
    } catch (err) {
      setError("Unable to remove this stock.");
    }
  };

  const finishSession = async () => {
    try {
      setError("");
      setSessionMessage("");

      const response = await fetch(`${API_URL}/session/finish`, {
        method: "POST",
      });

      if (!response.ok) {
        throw new Error("Unable to finish session");
      }

      setSessionMessage(
        "Session finished. Your current market state is now the comparison baseline."
      );

      await loadDashboard();
    } catch (err) {
      setError("Unable to finish the session.");
    }
  };

  const attentionCount = stocks.filter(
    (stock) => stock.attentionLevel === "HIGH"
  ).length;

  const changedCount = stocks.filter(
    (stock) =>
      stock.attentionLevel === "HIGH" ||
      stock.attentionLevel === "MEDIUM"
  ).length;

  const unchangedCount = stocks.filter(
    (stock) =>
      stock.attentionLevel === "LOW" ||
      stock.attentionLevel === "BASELINE"
  ).length;

  const formatTime = () => {
    if (!lastUpdated) return "Waiting for data";

    return lastUpdated.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
    });
  };

  return (
    <div className="app">

      {/* HEADER */}
      <header className="header">

        <div className="brand">
          <div className="brand-mark">M</div>

          <div>
            <h1>MarketPulse</h1>
            <p>Smart Market Watchlist</p>
          </div>
        </div>

        <div className="live">
          <span className="live-dot"></span>
          LIVE
        </div>

      </header>

      <main>

        {/* HERO */}
        <section className="hero">

          <div className="hero-content">
            <span className="eyebrow">
              SMART MARKET MONITOR
            </span>

            <h2>
              What changed since
              <br />
              you last checked?
            </h2>

            <p>
              MarketPulse watches your selected stocks and highlights
              meaningful changes so you can focus on what actually needs
              your attention.
            </p>
          </div>

          <div className="hero-status">
            <span className="status-label">
              LAST UPDATED
            </span>

            <strong>
              {formatTime()}
            </strong>

            <span className="refresh-note">
              Refreshes every 15 seconds
            </span>
          </div>

        </section>

        {/* SUMMARY */}
        <section className="summary">

          <div className="summary-card attention-card">
            <div className="summary-icon">!</div>

            <div>
              <span className="summary-number">
                {attentionCount}
              </span>

              <span className="summary-label">
                Need attention
              </span>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon">↗</div>

            <div>
              <span className="summary-number">
                {changedCount}
              </span>

              <span className="summary-label">
                Changed
              </span>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon">✓</div>

            <div>
              <span className="summary-number">
                {unchangedCount}
              </span>

              <span className="summary-label">
                No material change
              </span>
            </div>
          </div>

          <div className="summary-card">
            <div className="summary-icon">◉</div>

            <div>
              <span className="summary-number">
                {stocks.length}
              </span>

              <span className="summary-label">
                Stocks watching
              </span>
            </div>
          </div>

        </section>

        {/* ADD STOCK */}
        <section className="watchlist-toolbar">

          <div>
            <h2>Your Watchlist</h2>
            <p>
              Add stocks you want MarketPulse to monitor.
            </p>
          </div>

          <div className="add-section">

            <input
              type="text"
              placeholder="Enter symbol e.g. TCS"
              value={symbol}
              onChange={(e) => setSymbol(e.target.value)}
              onKeyDown={(e) => {
                if (e.key === "Enter") {
                  addStock();
                }
              }}
            />

            <button onClick={addStock}>
              + Add Stock
            </button>

          </div>

        </section>

        {/* ERROR */}
        {error && (
          <div className="error">
            <strong>Something went wrong:</strong> {error}
          </div>
        )}

        {/* SESSION MESSAGE */}
        {sessionMessage && (
          <div className="success">
            ✓ {sessionMessage}
          </div>
        )}

        {/* LOADING */}
        {loading ? (

          <div className="message">
            <div className="loader"></div>
            <p>Loading market data...</p>
          </div>

        ) : stocks.length === 0 ? (

          /* EMPTY STATE */
          <div className="empty">

            <div className="empty-icon">
              +
            </div>

            <h2>
              Your watchlist is empty
            </h2>

            <p>
              Add your first stock above to start tracking
              meaningful market changes.
            </p>

          </div>

        ) : (

          /* STOCKS */
          <section className="stocks">

            {stocks.map((stock) => {

              const pricePositive =
                stock.priceChangePercent > 0;

              const volumePositive =
                stock.volumeChangePercent > 0;

              const scorePercentage =
                Math.min(
                  (stock.attentionScore / 80) * 100,
                  100
                );

              return (

                <article
                  className={`stock-card ${stock.attentionLevel.toLowerCase()}`}
                  key={stock.symbol}
                >

                  {/* CARD HEADER */}
                  <div className="stock-top">

                    <div>
                      <h2>
                        {stock.symbol}
                      </h2>

                      <p>
                        Market monitored
                      </p>
                    </div>

                    <span className="attention">
                      {stock.attentionLevel}
                    </span>

                  </div>

                  {/* PRICE */}
                  <div className="price">
                    ₹{stock.currentPrice.toFixed(2)}
                  </div>

                  {/* METRICS */}
                  <div className="metrics">

                    <div className="metric">

                      <span>
                        Price change
                      </span>

                      <strong
                        className={
                          pricePositive
                            ? "positive"
                            : stock.priceChangePercent < 0
                              ? "negative"
                              : ""
                        }
                      >
                        {stock.priceChangePercent > 0
                          ? "+"
                          : ""}
                        {stock.priceChangePercent.toFixed(2)}%
                      </strong>

                    </div>

                    <div className="metric">

                      <span>
                        Volume change
                      </span>

                      <strong
                        className={
                          volumePositive
                            ? "positive"
                            : stock.volumeChangePercent < 0
                              ? "negative"
                              : ""
                        }
                      >
                        {stock.volumeChangePercent > 0
                          ? "+"
                          : ""}
                        {stock.volumeChangePercent.toFixed(2)}%
                      </strong>

                    </div>

                  </div>

                  {/* SCORE */}
                  <div className="score-section">

                    <div className="score-header">

                      <span>
                        Attention Score
                      </span>

                      <strong>
                        {stock.attentionScore.toFixed(1)}
                        <small> / 80</small>
                      </strong>

                    </div>

                    <div className="score-bar">

                      <div
                        className="score-fill"
                        style={{
                          width: `${scorePercentage}%`,
                        }}
                      ></div>

                    </div>

                  </div>

                  {/* EXPLANATION */}
                  <div className="explanation-box">

                    <span className="explanation-label">
                      WHY IT MATTERS
                    </span>

                    <p className="explanation">
                      {stock.explanation}
                    </p>

                  </div>

                  {/* REMOVE */}
                  <button
                    className="remove"
                    onClick={() =>
                      removeStock(stock.symbol)
                    }
                  >
                    Remove from watchlist
                  </button>

                </article>

              );
            })}

          </section>

        )}

        {/* SESSION */}
        <section className="session">

          <div>
            <h3>
              Done checking the market?
            </h3>

            <p>
              Finish your session to save the current market
              state as the baseline for your next visit.
            </p>
          </div>

          <button
            className="finish-button"
            onClick={finishSession}
          >
            Finish Session →
          </button>

        </section>

      </main>

      {/* FOOTER */}
      <footer>
        <span>
          MarketPulse
        </span>

        <span>
          Smart change detection for your watchlist
        </span>
      </footer>

    </div>
  );
}

export default App;