package com.sb.exchangeinfo.client.utils;


import android.util.Pair;
import com.sb.exchangeinfo.client.models.Quote;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sbenner
 * Date: 8/13/14
 * Time: 9:05 PM
 */
public class QuoteUtils {


    // ,"totalPages":3,"totalElements":14,"firstPage":true,"lastPage":false,"last":false,"size":5,"number":0,"sort":
    // [{"direction":"DESC","property":"buyout","ignoreCase":false,"nullHandling":"NATIVE","ascending":false}],"numberOfElements":5,"first":true}

    public static List<Quote> buildQuotesFromString(String contents) throws JSONException {


        List<Quote> quotes = new ArrayList<Quote>();


        JSONArray auctionsArray = new JSONArray(contents);
        JSONObject obj = null;

        for (int i = 0; i < auctionsArray.length(); i++) {
            obj = (JSONObject) auctionsArray.get(i);
            Quote quote = new Quote();

            quote.setName(obj.getString("name"));
            quote.setSymbol(obj.getString("symbol"));
            quote.setPrice(obj.getString("price"));
            quote.setTs(obj.getLong("ts"));
            quote.setVolume(obj.getLong("volume"));
            quote.setType(obj.getString("type"));
            quote.setUtctime(obj.getString("utctime"));
            quote.setDate(DateUtils.getDayStringFromLong(quote.getTs()));

            quotes.add(quote);
        }


        return quotes;
    }

    public static List<Pair<Long, Double>> buildChartsFromString(String contents) throws JSONException {


        List<Pair<Long, Double>> l = new ArrayList<>();
        JSONArray auctionsArray = new JSONArray(contents);
        JSONObject obj = null;

        for (int i = 0; i < auctionsArray.length(); i++) {

            obj = (JSONObject) auctionsArray.get(i);
            Pair<Long, Double> p = new Pair<Long, Double>(obj.getLong("ts"), obj.getDouble("price"));
            l.add(p);
        }


        return l;
    }


}
