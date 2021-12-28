package com.example.stockbeaver;
import java.util.ArrayList;

/**
 * Stores all the trade information; paper/real
 */
public class TradeInfo {

    private String compSymbol;
    private int positionSize;
    private String compName;
    private String privacy; //Public or private status for postion; social


    /**
     * Holds all parameters for TradeInfo
     */
    public TradeInfo(String compSymbol, Integer positionSize, String compName,  String privacy) {
        this.compSymbol = compSymbol;
        this.positionSize = positionSize;
        this.compName = compName;
        this.privacy = privacy;
    }


    public String getCompSymbol() {
        return compSymbol;
    }

    public void setCompSymbol(String compSymbol) {
        this.compSymbol = compSymbol;
    }

    public int getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(int positionSize) {
        this.positionSize = positionSize;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}