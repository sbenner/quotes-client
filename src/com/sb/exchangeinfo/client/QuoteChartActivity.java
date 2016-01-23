package com.sb.exchangeinfo.client;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.*;
import android.widget.*;
import com.sb.exchangeinfo.client.utils.AuctionsApplication;
import com.sb.exchangeinfo.client.utils.QuoteStatisticsLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QuoteChartActivity extends ListActivity {

    /**
     * Called when the activity is first created.
     */
    public static Map<String, String> periods;

    static {
        Map<String, String> aMap = new HashMap<String, String>();

        aMap.put("USDRUB=X", "USD/RUB");
        aMap.put("EURRUB=X", "EUR/RUB");
        aMap.put("BZH16.NYM", "Brent Crude Oil Last Day Finance");
        aMap.put("BZG16.NYM", "BZ Future FEB 2016");
        aMap.put("BZZ17.NYM", "Brent Crude Oil Last Day Finance");
        aMap.put("BZJ16.NYM", "Brent Crude Oil Last Day Finance");
        aMap.put("CLG16.NYM", "Crude Oil Feb 16");

        periods = Collections.unmodifiableMap(aMap);
    }

    Pair p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        ActionBar actionBar = getActionBar();

        actionBar.show();
        String key = ((AuctionsApplication) getApplication()).getPrivateKey();
        String ip = ((AuctionsApplication) getApplication()).getIpAddress();

        p = new Pair(ip, key);

        registerForContextMenu(getListView());
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, periods);
        setListAdapter(adapter);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        l.requestFocus();
        //if (v.getTag() instanceof ItemListAdapter.ViewHolder) {
        l.showContextMenuForChild(v);
//        } else {
//            new SearchDialog(this, p);
//        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String symbol = info.targetView.getTag().toString();
        switch (item.getItemId()) {
            case R.id.todays_chart:
                new QuoteStatisticsLoader(QuoteChartActivity.this, p).execute(symbol, "day");
                return true;
            case R.id.sevendays_chart:
                new QuoteStatisticsLoader(QuoteChartActivity.this, p).execute(symbol, "seven_days");
                return true;
            case R.id.month_chart:
                new QuoteStatisticsLoader(QuoteChartActivity.this, p).execute(symbol, "month");
                return true;
            case R.id.sixmonths_chart:
                new QuoteStatisticsLoader(QuoteChartActivity.this, p).execute(symbol, "six_months");
                return true;
            case R.id.year_chart:
                new QuoteStatisticsLoader(QuoteChartActivity.this, p).execute(symbol, "year");
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        if (((AdapterView.AdapterContextMenuInfo) menuInfo).targetView.getTag() == null)
            return;

        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private class StableArrayAdapter extends BaseAdapter {

        Map<Integer, Pair<String, String>> mIdMap = new HashMap<Integer, Pair<String, String>>();
        Context ctx;
        int textViewResourceId;
        LayoutInflater inflater = null;

        public StableArrayAdapter(Context context, int textViewResourceId, Map<String, String> aMap) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.textViewResourceId = textViewResourceId;
            int i = 0;
            this.ctx = context;

            for (Map.Entry e : aMap.entrySet()) {
                Pair<String, String> p = new Pair(e.getKey().toString(), e.getValue().toString());
                mIdMap.put(i, p);
                i++;
            }
        }

        @Override
        public int getCount() {
            return mIdMap.size();
        }

        @Override
        public Object getItem(int position) {
            return mIdMap.get(position);  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public long getItemId(int position) {
            return position;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.v("position", Integer.toString(position));
            if (convertView == null) {
                convertView = (TextView) inflater.inflate(textViewResourceId, parent, false);
                Pair<String, String> p = mIdMap.get(position);
                ((TextView) convertView).setText(p.second.toString());
                convertView.setTag(p.first.toString());


            }
            return convertView;
        }
    }


//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//
//            case R.id.search:
//                new SearchDialog(this, p);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//
//    }
}