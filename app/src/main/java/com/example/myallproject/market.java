package com.example.myallproject;

public class market {
    private String MarketName;
    private int marketNUM;
    private String AREA;


    public market(String marketName, int marketNUM, String AREA) {
        MarketName = marketName;
        this.marketNUM = marketNUM;
        this.AREA = AREA;
    }

    public String getMarketName() {
        return MarketName;
    }

    public void setMarketName(String marketName) {
        MarketName = marketName;
    }

    public int getMarketNUM() {
        return marketNUM;
    }

    public void setMarketNUM(int marketNUM) {
        this.marketNUM = marketNUM;
    }

    public String getAREA() {
        return AREA;
    }

    public void setAREA(String AREA) {
        this.AREA = AREA;
    }

    @Override
    public String toString() {
        return "market{" +
                "MarketName='" + MarketName + '\'' +
                ", marketNUM=" + marketNUM +
                ", AREA='" + AREA + '\'' +
                '}';
    }
}
