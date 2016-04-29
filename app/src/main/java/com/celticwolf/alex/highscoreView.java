package com.celticwolf.alex;


//import android.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.view.MenuItem;
//import android.view.Menu;
//import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class highscoreView extends Activity implements View.OnClickListener {

    GridView gridView;
    TextView highscorelist;
    Highscore highscoreobj;
    String hsclist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);


        highscorelist = (TextView) findViewById(R.id.tvHighscorelist);

        addContent();
    }

    private void addContent() {
        highscoreobj = new Highscore(this);

        hsclist = "";
        for (int i = 0; i < 10; i++) {
            int leScore = highscoreobj.getScore(i);
            String leDate = highscoreobj.getName(i);
            hsclist = hsclist + (i + 1) + ".\t" + leDate + "\t :\t " + leScore + "\n";
        }
        highscorelist.setText(hsclist);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.short_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item1) {
            rateApp();
            return true;
        } else if (itemId == R.id.item2) {
            openFacebook();
            return true;
        } else if (itemId == android.R.id.home) {
            Intent intent = new Intent(this, menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void openFacebook() {
        String url = "https://www.facebook.com/BeerOrNoBeer";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void rateApp() {
        String url = "https://play.google.com/store/apps/details?id=com.celticwolf.alex&feature=search_result#?t=W251bGwsMSwyLDEsImNvbS5jZWx0aWN3b2xmLmFsZXgiXQ..";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
