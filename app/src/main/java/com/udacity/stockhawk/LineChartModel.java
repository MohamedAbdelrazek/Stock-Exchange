package com.udacity.stockhawk;

/**
 * Created by mostafa_anter on 12/15/16.
 */

public class LineChartModel {
    private float history;
    private float price;

    public float getHistory() {
        return history;
    }

    public void setHistory(float history) {
        this.history = history;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "LineChartModel [History=" + history + ", price=" + price + "]";
    }
}
