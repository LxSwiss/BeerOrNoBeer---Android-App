package com.celticwolf.alex;

import android.app.ProgressDialog;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class menu extends Activity implements View.OnClickListener {

    private static String DB_PATH = "/data/data/com.celticwolf.alex/databases/";
    private static String DB_NAME = "sqlbeerlist.sqlite";
    Button start, about, banner;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.main);
        start = (Button) findViewById(R.id.bStart);
        about = (Button) findViewById(R.id.bAbout);
        //sendbeer = (Button) findViewById(R.id.bSendBrand);
        //updatedatabase = (Button) findViewById(R.id.bUpdateDatabase);
        banner = (Button) findViewById(R.id.bBanner);

        start.setOnClickListener(this);
        about.setOnClickListener(this);
        //sendbeer.setOnClickListener(this);
        //updatedatabase.setOnClickListener(this);
        banner.setOnClickListener(this);
        AppRater.app_launched(this);

        if (isOnline())
            getData();

    }

    public void onClick(View v) {
        if (v.getId() == R.id.bStart) {
            startGame();
        } else if (v.getId() == R.id.bAbout) {
            about();
        } else if (v.getId() == R.id.bBanner) {
            openWebsite();
        }

    }

    private void openWebsite() {
        String url = "http://www.schuetzengarten.ch/portal/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    private void about() {
        Intent goabout = new Intent("com.celticwolf.alex.ABOUT");
        startActivity(goabout);

    }

/*private void sendbeer(){
    Intent gosendbeer  = new Intent("com.celticwolf.alex.SENDBRAND");
	startActivity(gosendbeer);
}*/


    public void startGame() {

        Intent startGame = new Intent("com.celticwolf.alex.GAME");
        startActivity(startGame);
    }

    private void getDatabase() {
        DataBaseHelper myDbHelper = new DataBaseHelper(null);
        myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
    }

    private void getData() {
        getDatabase();
        loading = ProgressDialog.show(this, "Please Wait...", "Update Database...", false, false);


        try {

            String myPath = DB_PATH + DB_NAME;

            SQLiteDatabase
                    .openDatabase(
                            myPath,
                            null,
                            (SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS));

        } catch (SQLiteException e) {

            // database does't exist yet.

        }


        //get JSON response for beer list
        StringRequest stringRequest = new StringRequest(Config.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(menu.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(menu.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showJSON(String response) {
        String beer_id = "";
        String beer_brand = "";
        String beer_country = "";


        try {
            JSONObject jsonObject = new JSONObject(response);

            // Get JSON Array for beers
            JSONArray result_beer = jsonObject.getJSONArray(Config.JSON_ARRAY_BEERS);
            // Get JSON Array for nobeers
            JSONArray result_nobeer = jsonObject.getJSONArray(Config.JSON_ARRAY_NOBEERS);

            DataBaseHelper myDbHelper = new DataBaseHelper(null);
            myDbHelper = new DataBaseHelper(this);

            //Check if Onlinedatabase has been updated
            if ((result_beer.length() + result_nobeer.length()) != myDbHelper.QueryNumEntries()) {
                myDbHelper.deleteDatabase();


                for (int i = 0; i < result_beer.length(); i++) {
                    JSONObject actor = result_beer.getJSONObject(i);
                    beer_id = actor.getString(Config.KEY_ID);
                    beer_brand = actor.getString(Config.KEY_BRAND);
                    beer_country = actor.getString(Config.KEY_COUNTRY);

                    myDbHelper.insertValue(beer_id, beer_brand, beer_country, Config.JSON_ARRAY_BEERS);
                }


                for (int i = 0; i < result_nobeer.length(); i++) {
                    JSONObject actor = result_nobeer.getJSONObject(i);
                    beer_id = actor.getString(Config.KEY_ID);
                    beer_brand = actor.getString(Config.KEY_BRAND);
                    beer_country = actor.getString(Config.KEY_COUNTRY);

                    myDbHelper.insertValue(beer_id, beer_brand, beer_country, Config.JSON_ARRAY_NOBEERS);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.short_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item1) {
            rateApp();
            return true;
        } else if (itemId == R.id.item2) {
            shareIt();
            return true;
        }
        return false;
    }

    private void shareIt() {
        String share = getResources().getString(R.string.ac_share);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://play.google.com/store/apps/details?id=com.celticwolf.alex#?t=W251bGwsMSwxLDIxMiwiY29tLmNlbHRpY3dvbGYuYWxleCJd";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Beer or no Beer� Drinking Game");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, share));
    }

    private void rateApp() {
        String url = "https://play.google.com/store/apps/details?id=com.celticwolf.alex#?t=W251bGwsMSwxLDIxMiwiY29tLmNlbHRpY3dvbGYuYWxleCJd";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}
