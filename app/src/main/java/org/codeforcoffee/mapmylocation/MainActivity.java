package org.codeforcoffee.mapmylocation;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<Location> mLocationList = new ArrayList<>();
    private LocationArrayAdapter mLocationArrayAdapter;
    private ListView mListView;
    private Intent mGmapsIntent;
    private AdapterView.OnItemLongClickListener mListener;

    private class FetchLocationsTask extends AsyncTask<String, Void, JSONObject> {

        private OkHttpClient http = new OkHttpClient();

        // while the task runs....
        @Override
        protected JSONObject doInBackground(String... strings) {
            // async call the API
            String url = "http://illinoisdispensaries.space/api-v1";
            Request req = new Request.Builder().url(url).build();
            try {
                Response res = http.newCall(req).execute();
                String data = res.body().string();
                JSONArray jsonLocations = new JSONArray(data);
                Integer length = jsonLocations.length();

                // loop through results
                // first, purge all prior results
                mLocationList.clear();
                for (int i = 0; i < length; i++) {
                    JSONObject obj = jsonLocations.getJSONObject(i);
                    String name = obj.getString("name");
                    String city = obj.getString("city");
                    String phone = obj.getString("phone");
                    String lat = obj.getString("lat");
                    String lon = obj.getString("long");
                    Location tempLoc = new Location(name, city, phone, Double.parseDouble(lat), Double.parseDouble(lon));
                    mLocationList.add(tempLoc);
                }

            } catch(IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            // fetch resources with http
            // get the string body of a request's response
            // convert that to json
            return null;
        }

        // when the task is done
        @Override
        protected void onPostExecute(JSONObject location) {
            // when done, we need to populate our List<Location>
            // then need to tell the adapter: 'yo, we're done - render'
            mLocationArrayAdapter.notifyDataSetChanged();
            mListView.smoothScrollToPosition(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find our listview
        mListView = (ListView) findViewById(R.id.list_view);
        mLocationArrayAdapter = new LocationArrayAdapter(this, mLocationList);
        mListView.setAdapter(mLocationArrayAdapter);
        mListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Location currentLoc = (Location) adapterView.getItemAtPosition(i);
                Double lat = currentLoc.lat;
                Double lon = currentLoc.lon;
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lon);
                mGmapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(mGmapsIntent);
                return false;
            }
        };
        mListView.setOnItemLongClickListener(mListener);

        // find our asyntask
        FetchLocationsTask fetchLocationsTask = new FetchLocationsTask();
        // start our task
        fetchLocationsTask.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchLocationsTask fetchLocationsTask = new FetchLocationsTask();
                // start our task
                fetchLocationsTask.execute();
                Snackbar.make(view, "Refreshing API List...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
