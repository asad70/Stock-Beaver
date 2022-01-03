package com.example.stockbeaver;
import java.util.ArrayList;

/**
 * Stores all the trade information; paper/real
 */
public class TradeInfo {

    private String compSymbol;
    private String positionSize;
    private String price;
    private String privacy; //Public or private status for postion; social


    /**
     * Holds all parameters for TradeInfo
     */
    public TradeInfo(String compSymbol, String positionSize, String price,  String privacy) {
        this.compSymbol = compSymbol;
        this.positionSize = positionSize;
        this.price = price;
        this.privacy = privacy;
    }


    public String getCompSymbol() {
        return compSymbol;
    }

    public void setCompSymbol(String compSymbol) {
        this.compSymbol = compSymbol;
    }

    public String getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(String positionSize) {
        this.positionSize = positionSize;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}