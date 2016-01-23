package com.sb.exchangeinfo.client.models;


import com.sb.exchangeinfo.client.utils.DateUtils;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 1/15/16
 * Time: 4:14 AM
 */
public class Quote {
    private String name;
    private String price;
    private String symbol;
    private long ts;
    private String type;//" : "future",
    private String utctime;//" : "2016-01-14T22:38:27+0000",
    private String date;
    private long volume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUtctime() {
        return utctime;
    }

    public void setUtctime(String utctime) {
        this.utctime = utctime;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getDate() {
        if(date==null)
            setDate(DateUtils.getDayStringFromLong(getTs()));

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
