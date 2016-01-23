package com.sb.exchangeinfo.client.utils;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.util.IndexXYMap;
import org.json.JSONException;

import java.util.*;


public class QuoteStatisticsLoader extends AsyncTask<String, Void, Pair<String, XYMultipleSeriesDataset>> {
    private ProgressDialog dialog;
    Context ctx;
    private Pair pair;

    public QuoteStatisticsLoader(Context ctx, Pair pair) {
        this.ctx = ctx;
        this.dialog = new ProgressDialog(this.ctx);
        this.pair = pair;

    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Building chart...");
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(Pair<String, XYMultipleSeriesDataset> dataset) {
        if (dialog.isShowing()) {
            dialog.cancel();
        }

        String date = DateUtils.getDayStringFromLong(System.currentTimeMillis()/1000);

        if (dataset != null) {
            Intent intent = ChartFactory.getTimeChartIntent(ctx, dataset.second, getRenderer(dataset.first), null);
            ctx.startActivity(intent);
        } else {
            UIUtils.showToast(this.ctx, "There's no chart data available for this item");
        }
    }


    @Override
    protected Pair<String, XYMultipleSeriesDataset> doInBackground(String... params) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        String symbol=params[0];
        String period=params[1].equals("day")?"date=%s":"period="+params[1];
        String url = "%squotes/getquotes?symbol=%s&"+period;
        url = String.format(url,pair.first,symbol,DateUtils.getDayStringFromLong(System.currentTimeMillis()/1000));

        //url = String.format(url,pair.first,params[0].toString(),"20160122");

        String quotes = NetUtils.getResourceFromUrl(url, pair.second.toString());

        if(quotes==null||quotes.isEmpty())
            return null;

        List<Pair<Long,Double>> auctionList = null;
        try {
            auctionList = QuoteUtils.buildChartsFromString(quotes);
        } catch (JSONException e) {
            Log.e("QuoteStatisticsLoader", e.getMessage());
        }

        if (auctionList.isEmpty())
            return null;

        TreeMap<Long,Double> f = new TreeMap<>();

        TimeSeries series = new TimeSeries("Quotes "+params[0].toString());
        for (Pair e : auctionList) {
            double price = Double.parseDouble(e.second.toString());
            long timestamp = Long.parseLong(e.first.toString());
          //  averagePrice += price;
            series.add(new Date(timestamp*1000), price );

            f.put(timestamp,price);
        }

      //  long maxTs= Collections.max(f.keySet());
        long maxTs = f.lastKey();
        double currentPrice = f.get(maxTs);

        //averagePrice = averagePrice / auctionList.size();
        String title="Last quote: "+currentPrice+" "+DateUtils.getDayStringFromLongHHmm(maxTs);

        dataset.addSeries(series);

        Pair<String, XYMultipleSeriesDataset> rv = new Pair(title, dataset);

        return rv;


    }


    private XYMultipleSeriesRenderer getRenderer(String title) {

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(35);
        renderer.setChartTitleTextSize(35);
        renderer.setLabelsTextSize(35);
        renderer.setLegendTextSize(35);
        renderer.setPointSize(10f);
        renderer.setMargins(new int[]{100, 70, 100, 0});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setLineWidth(9f);

        renderer.setFitLegend(true);
        renderer.setLegendHeight(50);
        renderer.setChartTitle(title);

        r.setChartValuesSpacing(5);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setColor(Color.YELLOW);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
        renderer.setAxesColor(Color.DKGRAY);
        renderer.setLabelsColor(Color.LTGRAY);
        return renderer;
    }

}
