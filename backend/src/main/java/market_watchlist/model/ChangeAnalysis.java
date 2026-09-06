package market_watchlist.model;

public class ChangeAnalysis {

    private String symbol;
    private double currentPrice;
    private double priceChangePercent;
    private long currentVolume;
    private double volumeChangePercent;
    private double attentionScore;
    private String attentionLevel;
    private String explanation;

    public ChangeAnalysis() {
    }

    public ChangeAnalysis(
            String symbol,
            double currentPrice,
            double priceChangePercent,
            long currentVolume,
            double volumeChangePercent,
            double attentionScore,
            String attentionLevel,
            String explanation) {

        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.priceChangePercent = priceChangePercent;
        this.currentVolume = currentVolume;
        this.volumeChangePercent = volumeChangePercent;
        this.attentionScore = attentionScore;
        this.attentionLevel = attentionLevel;
        this.explanation = explanation;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public long getCurrentVolume() {
        return currentVolume;
    }

    public double getVolumeChangePercent() {
        return volumeChangePercent;
    }

    public double getAttentionScore() {
        return attentionScore;
    }

    public String getAttentionLevel() {
        return attentionLevel;
    }

    public String getExplanation() {
        return explanation;
    }
}