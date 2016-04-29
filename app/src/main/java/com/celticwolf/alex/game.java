package com.celticwolf.alex;

//import android.app.Activity;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class game extends Activity implements View.OnClickListener {

    final String ch = "CH", ra = "RA", aus = "AUS", b = "B", bol = "BOL", br = "BR",
            cn = "CN", dk = "DK", de = "DE", dor = "DO", eng = "ENG",
            ee = "EE", fi = "FI", fr = "FR", gr = "GR", in = "IN", ir = "IR",
            it = "IT", jm = "JM", jp = "JP", ca = "CA", ke = "KE", hr = "HR",
            cu = "CU", ma = "MA", mx = "MX", mn = "MN", nz = "NZ", nl = "NL",
            oe = "OE", pe = "PE", pl = "PL", pt = "PT", ru = "RU", sco = "SCO",
            se = "SE", sg = "SG", es = "ES", za = "ZA", tah = "TAH", tz = "TZ",
            th = "TH", cz = "CZ", usa = "USA", iceland = "iceland",
            bulgaria = "bulgaria", guatemala = "guatemala", nepal = "nepal",
            tibet = "tibet", venezuela = "venezuela";
    final int drinkfactor_half = 1;
    final int drinkfactor_normal = 2;
    final int drinkfactor_double = 3;
    private final int adKill = 6;
    Button beer, noBeer, setbrand;
    GoogleAnalyticsTracker tracker;
    TextView feedback;
    ImageView setFlag, glassone, glasstwo, cork, thefeedback;
    LinearLayout adscontainer;
    int dice, randomBeer, beercount, drinkcount, corkposv, corkposh, highscore;
    FileReader file;
    ArrayList<String> beers;
    ArrayList<String> beercountry;
    ArrayList<String> nobeers;
    ArrayList<String> nobeercountry;
    int nobeersize, beersize;
    boolean isValidToClick = true;
    boolean isBeer = true;
    boolean rightclick = false;
    boolean changeText = true;
    String flag, beenis;
    String[] beerflagname, nobeerflagname;
    String infotoast, right, wrong1, wrong2, wrong3, txthighscore;
    int drinkfactor = drinkfactor_normal;
    int selectedBeer;
    String anybeer;
    boolean newbeer = false;
    String[] beerstack = {" ", " ", " ", " "};
    String temp;
    Highscore highscoreobj;

    RelativeLayout corkwall, feedbackview;

    AdView adView;

    Bitmap ball;
    int x, y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        getDatabase();
        getStrings();
        initializeGame();


        beer.setOnClickListener(this);
        noBeer.setOnClickListener(this);
        setbrand.setOnClickListener(this);
    }

    private void loadAd() {
        //request TEST ads to avoid being disabled for clicking your own ads
        AdRequest adRequest = new AdRequest();

        //test mode on EMULATOR
        //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

        //test mode on DEVICE (this example code must be replaced with your device uniquq ID)
        //adRequest.addTestDevice("4G74FC73D62D42B62A7F7DA61EF5F776");

        adView = (AdView) findViewById(R.id.adMob);

        // Initiate a request to load an ad in test mode.
        // You can keep this even when you release your app on the market, because
        // only emulators and your test device will get test ads. The user will receive real ads.
        adView.loadAd(adRequest);

    }

    private void checkAd(int Score) {
        if (Score >= adKill) {

            View admobAds = (View) findViewById(R.id.adMob);

            adscontainer.removeView(admobAds);
        }


    }

    private void getStrings() {
        txthighscore = getResources().getString(R.string.txthighscore);
        infotoast = getResources().getString(R.string.infotoast);
        wrong1 = getResources().getString(R.string.wrong1);
        wrong2 = getResources().getString(R.string.wrong2);
        wrong3 = getResources().getString(R.string.wrong3);

    }

    private void initializeGame() {
        beercount = drinkcount = highscore = 1;
        beer = (Button) findViewById(R.id.bBeer);
        noBeer = (Button) findViewById(R.id.bNoBeer);
        setbrand = (Button) findViewById(R.id.tvBeerName);
        feedback = (TextView) findViewById(R.id.tvIsBeer);
        setFlag = (ImageView) findViewById(R.id.country);
        glassone = (ImageView) findViewById(R.id.glassfull1);
        glasstwo = (ImageView) findViewById(R.id.glassfull2);
        adscontainer = (LinearLayout) findViewById(R.id.adsContainer);
        corkposh = corkposv = 0;


        //beercount = 1;

        ball = BitmapFactory.decodeResource(getResources(), R.drawable.corkone);
        x = y = 0;

        randomBeer = (int) (Math.random() * beersize);
        anybeer = beers.get(randomBeer);
        setbrand.setText("" + anybeer);
        setbrand.setTextColor(Color.WHITE);
        setBeerFlag();
        loadAd();

    }

    private void getDatabase() {
//		DataBaseHelper myDbHelper = new DataBaseHelper(null);
//		myDbHelper = new DataBaseHelper(this);
//		try {
//			myDbHelper.createDataBase();
//		} catch (IOException ioe) {
//			throw new Error("Unable to create database");
//		}
//		myDbHelper.insertValue();
        DataBaseHelper myDbHelper = new DataBaseHelper(null);
        beers = myDbHelper.getbeers();
        beercountry = myDbHelper.getcountries();
        nobeercountry = myDbHelper.getnobeercountries();
        nobeers = myDbHelper.getnobeers();

        beersize = beers.size();
        nobeersize = nobeers.size();
    }

    private void rightClick() {
        feedback.setTextColor(Color.WHITE);
        feedback.setText("");
        rightclick = true;
        drawpic();
        setCrownCap();
        changeText = true;
        setText();
        glassone.setImageResource(R.drawable.glassfull);
        glasstwo.setImageResource(R.drawable.glassfullrigth);
        sethighscore();
        setdrinkamount();
        drinkcount++;

    }


    private void setdrinkamount() {
        if (drinkcount == 1) {
            beercount = 1;
        } else {
            switch (drinkfactor) {
                case 1:
                    beercount = drinkcount / 2;
                    break;
                case 2:
                    beercount = drinkcount;
                    break;
                case 3:
                    beercount = drinkcount * 2;
                    break;
                default:
                    beercount = drinkcount;
            }
        }
    }

    private void setText() {
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.this.runOnUiThread(new Runnable() {
                    public void run() {
//						pushbeer();
                        diceBeer();
                        try {
                            feedbackview.removeAllViewsInLayout();
                        } finally {
                            feedbackview.invalidate();
                        }

                    }

                });
            }
        };
        t.start();

    }

    private void sethighscore() {
        if (drinkcount >= highscore) {
            highscore = drinkcount;
            checkAd(drinkcount);
        }
    }

    private void wrongClick() {
        if (beercount == 1) {
            feedback.setText(wrong2);
            feedback.setTextColor(Color.RED);
        } else {
            feedback.setText(wrong1 + " " + beercount + " " + wrong3);
            feedback.setTextColor(Color.RED);
        }
        rightclick = false;
        saveScore();
        drawpic();
        setCrownCap();
        setText();
        setglass();
        trackEvent();
        beercount = drinkcount = 1;
    }

    private void trackEvent() {
        try {
            // Category, Action, Label, Value
            tracker.trackEvent("Clicks", "Button", "clicked", drinkcount);
        } catch (Exception error) {
            Log.e("<YOUR_TAG>", "ButtonClick: " + error.toString());
        }

    }

    private void setCrownCap() {
        corkwall = (RelativeLayout) findViewById(R.id.relativeLayout1);
        int capSize = (int) convertDipsToPx(45.0f);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        if (rightclick) {
            cork = new ImageView(this);
            getRandomCap();
            cork.setAdjustViewBounds(true);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(capSize, capSize);
            params.leftMargin = (metrics.widthPixels - 6 * capSize) / 2 + corkposh * capSize;
            params.topMargin = corkposv * capSize;
            corkwall.addView(cork, params);
            corkwall.invalidate();
            corkposh++;
            if (corkposh >= 6) {
                corkposv++;
                corkposh = 0;
            }
            if (corkposv >= 3) {
                corkposv = 0;
            }

        } else {
            try {
                corkwall.removeAllViewsInLayout();
            } finally {
                corkwall.invalidate();
            }
            corkposh = corkposv = 0;
        }

    }

    private void getRandomCap() {
        int randomCoaster = (int) (Math.random() * 21);

        switch (randomCoaster) {
            case 0:
                cork.setImageResource(R.drawable.bkzero);
                break;
            case 1:
                cork.setImageResource(R.drawable.bkone);
                break;
            case 2:
                cork.setImageResource(R.drawable.bk2);
                break;
            case 3:
                cork.setImageResource(R.drawable.bk3);
                break;
            case 4:
                cork.setImageResource(R.drawable.bk4);
                break;
            case 5:
                cork.setImageResource(R.drawable.bk5);
                break;
            case 6:
                cork.setImageResource(R.drawable.bk6);
                break;
            case 7:
                cork.setImageResource(R.drawable.bk7);
                break;
            case 8:
                cork.setImageResource(R.drawable.bk8);
                break;
            case 9:
                cork.setImageResource(R.drawable.bk9);
                break;
            case 10:
                cork.setImageResource(R.drawable.bk10);
                break;
            case 11:
                cork.setImageResource(R.drawable.bk11);
                break;
            case 12:
                cork.setImageResource(R.drawable.bk12);
                break;
            case 13:
                cork.setImageResource(R.drawable.bk13);
                break;
            case 14:
                cork.setImageResource(R.drawable.bk14);
                break;
            case 15:
                cork.setImageResource(R.drawable.bk15);
                break;
            case 16:
                cork.setImageResource(R.drawable.bk16);
                break;
            case 17:
                cork.setImageResource(R.drawable.bk17);
                break;
            case 18:
                cork.setImageResource(R.drawable.bk18);
                break;
            case 19:
                cork.setImageResource(R.drawable.bk19);
                break;
            case 20:
                cork.setImageResource(R.drawable.bk20);
                break;
            case 21:
                cork.setImageResource(R.drawable.bk21);
                break;
            default:
                cork.setImageResource(R.drawable.bkdefault);
                break;
        }
    }

    private void drawpic() {
        feedbackview = (RelativeLayout) findViewById(R.id.relativeFeedback);
        thefeedback = new ImageView(this);
        try {
            feedbackview.removeAllViewsInLayout();
        } finally {
            feedbackview.invalidate();
        }
        if (rightclick == true)
            thefeedback.setImageResource(R.drawable.resrigth2);
        else
            thefeedback.setImageResource(R.drawable.reswrong2);

        thefeedback.setAdjustViewBounds(true);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                400, 90);
        feedbackview.addView(thefeedback, params);
        feedbackview.invalidate();
    }


    private void setglass() {
        switch (beercount) {
            case 1:
                glassone.setImageResource(R.drawable.glasstwo);
                glasstwo.setImageResource(R.drawable.glasstwo);
                break;
            case 2:
                glassone.setImageResource(R.drawable.glasstwo);
                glasstwo.setImageResource(R.drawable.glasstwo);
                break;
            case 3:
                glassone.setImageResource(R.drawable.glassthree);
                glasstwo.setImageResource(R.drawable.glassthree);
                break;
            case 4:
                glassone.setImageResource(R.drawable.glassthree);
                glasstwo.setImageResource(R.drawable.glassthree);
                break;
            case 5:
                glassone.setImageResource(R.drawable.glassfour);
                glasstwo.setImageResource(R.drawable.glassfour);
                break;
            case 6:
                glassone.setImageResource(R.drawable.glassfour);
                glasstwo.setImageResource(R.drawable.glassfour);
                break;
            case 7:
                glassone.setImageResource(R.drawable.glassfive);
                glasstwo.setImageResource(R.drawable.glassfive);
                break;
            case 8:
                glassone.setImageResource(R.drawable.glassfive);
                glasstwo.setImageResource(R.drawable.glassfive);
                break;
            default:
                glassone.setImageResource(R.drawable.glassempty);
                glasstwo.setImageResource(R.drawable.glassempty);
                break;

        }

    }

    private void diceBeer() {
        pushbeer(anybeer);
        dice = (int) (Math.random() * 2);
        if (dice == 1) {
            isBeer = false;
            randomBeer = (int) (Math.random() * nobeersize);
            anybeer = nobeers.get(randomBeer);
            setbrand.setText("" + anybeer);
            setNoBeerFlag();
        } else {
            isBeer = true;
            randomBeer = (int) (Math.random() * beersize);
            anybeer = beers.get(randomBeer);
            setbrand.setText("" + anybeer);
            setBeerFlag();
//			temp = anybeer;
            // puts beer to the menulist to search
        }
        isValidToClick = true;

    }

    private String pushbeer(String currentBeer) {
        if (isBeer == true) {
            for (int i = beerstack.length - 1; i > 0; i--) {
                beerstack[i] = beerstack[i - 1];
            }
            beerstack[0] = currentBeer;
        }
        invalidateOptionsMenu();
        return null;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.bBeer) {
            bBeerPressed();
        } else if (v.getId() == R.id.bNoBeer) {
            bNoBeerPressed();
        } else if (v.getId() == R.id.tvBeerName) {
            //openWebsite();
        }

    }


    public void showHighscore() {
        Intent showHigh = new Intent("com.celticwolf.alex.HIGHSCOREVIEW");
        startActivity(showHigh);
    }

    private void bBeerPressed() {
        if (isValidToClick) {
            isValidToClick = false;
            if (isBeer == true) {
                rightClick();
            } else {
                wrongClick();
            }
        }
//		diceBeer();
    }

    private void bNoBeerPressed() {
        if (isValidToClick) {
            isValidToClick = false;
            if (isBeer == false) {
                rightClick();
            } else {
                wrongClick();
            }
        }
//		diceBeer();
    }

    @Override
    protected void onResume() {
        //super.onResume();
        try {
            super.onResume();
            //setContentView(R.layout.main);
            tracker = GoogleAnalyticsTracker.getInstance();
            tracker.startNewSession("UA-28750825-1", 20, this);
            tracker.trackPageView("Game");
        } catch (Exception error) {
            Log.e("<YOUR_TAG>", "onCreate: " + error.toString());
        }

//		tracker = GoogleAnalyticsTracker.getInstance();
//		tracker.startNewSession("UA-28750825-1", 30, this);
//
//		tracker.trackEvent("Game", // Category
//				"StartGame", // Action
//				"gamestarted", // Label
//				1); // Value

    }


    @Override
    protected void onDestroy() {
        //super.onDestroy();
        saveScore();
        try {
            super.onDestroy();
            tracker.dispatch();
            tracker.stopSession();
        } catch (Exception error) {
            Log.e("<YOUR_TAG>", "onDestroy: " + error.toString());
        }
        // Stop the tracker when it is no longer needed.
        //tracker.stopSession();

    }

    private void saveScore() {
        //String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        Date date = new Date();
        String strDateFormat = "MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String monthName = sdf.format(date);

        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        String dayOfMonthStr = String.valueOf(dayOfMonth);

        String mydate = monthName.concat(" ").concat(dayOfMonthStr);

        highscoreobj = new Highscore(this);  //initialize highscore
        highscoreobj.addScore(mydate, drinkcount);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item_beerone = menu.findItem(R.id.item_beerone);
        item_beerone.setTitle(beerstack[0]);
        MenuItem item_two = menu.findItem(R.id.item_beertwo);
        item_two.setTitle(beerstack[1]);
        MenuItem item_three = menu.findItem(R.id.item_beerthree);
        item_three.setTitle(beerstack[2]);
        MenuItem item_four = menu.findItem(R.id.item_beerfour);
        item_four.setTitle(beerstack[3]);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item1) {
            showHighscore();
            return true;
        } else if (itemId == R.id.df_half) {
            drinkfactor = drinkfactor_half;
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            return true;
        } else if (itemId == R.id.df_normal) {
            drinkfactor = drinkfactor_normal;
            if (item.isChecked())
                item.setChecked(true);
            else
                item.setChecked(true);
            return true;
        } else if (itemId == R.id.df_double) {
            drinkfactor = drinkfactor_double;
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            return true;
        } else if (itemId == R.id.item3) {
            return true;
        } else if (itemId == R.id.item_beerone) {
            selectedBeer = 1;
            openWebsite(beerstack[0]);
            return true;
        } else if (itemId == R.id.item_beertwo) {
            selectedBeer = 2;
            openWebsite(beerstack[1]);
            return true;
        } else if (itemId == R.id.item_beerthree) {
            selectedBeer = 3;
            openWebsite(beerstack[2]);
            return true;
        } else if (itemId == R.id.item_beerfour) {
            selectedBeer = 4;
            openWebsite(beerstack[3]);
            return true;
        } else if (itemId == R.id.item4) {
            rateApp();
            return true;
        } else if (itemId == R.id.item5) {
            shareIt();
            return true;
        } else if (itemId == android.R.id.home) {
            Intent intent = new Intent(this, menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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

    private String openWebsite(String beerBrand) {
        //String url = "http://beeradvocate.com/search?q=" + setbrand.getText() + "&qt=beer";
        String url = "http://beeradvocate.com/search?q=" + beerBrand + "&qt=beer";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        return null;
    }

    private void rateApp() {
        String url = "https://play.google.com/store/apps/details?id=com.celticwolf.alex#?t=W251bGwsMSwxLDIxMiwiY29tLmNlbHRpY3dvbGYuYWxleCJd";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private float convertDipsToPx(float dips) {
        //float dips = 20.0f;
        float scale = getBaseContext().getResources().getDisplayMetrics().density;
        float pixels = Math.round(dips * scale);
        return pixels;
    }

    private void setBeerFlag() {
        if (beercountry.get(randomBeer).equals(ch)) {
            setFlag.setImageResource(R.drawable.swissflag);
        } else if (beercountry.get(randomBeer).equals(ra)) {
            setFlag.setImageResource(R.drawable.argentinia);
        } else if (beercountry.get(randomBeer).equals(aus)) {
            setFlag.setImageResource(R.drawable.australia);
        } else if (beercountry.get(randomBeer).equals(b)) {
            setFlag.setImageResource(R.drawable.belgium);
        } else if (beercountry.get(randomBeer).equals(bol)) {
            setFlag.setImageResource(R.drawable.bolivia);
        } else if (beercountry.get(randomBeer).equals(br)) {
            setFlag.setImageResource(R.drawable.brazil);
        } else if (beercountry.get(randomBeer).equals(cn)) {
            setFlag.setImageResource(R.drawable.china);
        } else if (beercountry.get(randomBeer).equals(dk)) {
            setFlag.setImageResource(R.drawable.denemark);
        } else if (beercountry.get(randomBeer).equals(de)) {
            setFlag.setImageResource(R.drawable.germany);
        } else if (beercountry.get(randomBeer).equals(dor)) {
            setFlag.setImageResource(R.drawable.dominicanrepublic);
        } else if (beercountry.get(randomBeer).equals(eng)) {
            setFlag.setImageResource(R.drawable.england);
        } else if (beercountry.get(randomBeer).equals(ee)) {
            setFlag.setImageResource(R.drawable.estland);
        } else if (beercountry.get(randomBeer).equals(fi)) {
            setFlag.setImageResource(R.drawable.finland);
        } else if (beercountry.get(randomBeer).equals(fr)) {
            setFlag.setImageResource(R.drawable.france);
        } else if (beercountry.get(randomBeer).equals(gr)) {
            setFlag.setImageResource(R.drawable.greece);
        } else if (beercountry.get(randomBeer).equals(in)) {
            setFlag.setImageResource(R.drawable.india);
        } else if (beercountry.get(randomBeer).equals(ir)) {
            setFlag.setImageResource(R.drawable.ireland);
        } else if (beercountry.get(randomBeer).equals(it)) {
            setFlag.setImageResource(R.drawable.italy);
        } else if (beercountry.get(randomBeer).equals(jm)) {
            setFlag.setImageResource(R.drawable.jamaica);
        } else if (beercountry.get(randomBeer).equals(jp)) {
            setFlag.setImageResource(R.drawable.japan);
        } else if (beercountry.get(randomBeer).equals(ca)) {
            setFlag.setImageResource(R.drawable.canada);
        } else if (beercountry.get(randomBeer).equals(ke)) {
            setFlag.setImageResource(R.drawable.kenya);
        } else if (beercountry.get(randomBeer).equals(hr)) {
            setFlag.setImageResource(R.drawable.croatia);
        } else if (beercountry.get(randomBeer).equals(cu)) {
            setFlag.setImageResource(R.drawable.cuba);
        } else if (beercountry.get(randomBeer).equals(ma)) {
            setFlag.setImageResource(R.drawable.morocco);
        } else if (beercountry.get(randomBeer).equals(mx)) {
            setFlag.setImageResource(R.drawable.mexico);
        } else if (beercountry.get(randomBeer).equals(mn)) {
            setFlag.setImageResource(R.drawable.mongolia);
        } else if (beercountry.get(randomBeer).equals(nz)) {
            setFlag.setImageResource(R.drawable.australia);
        } else if (beercountry.get(randomBeer).equals(nl)) {
            setFlag.setImageResource(R.drawable.netherland);
        } else if (beercountry.get(randomBeer).equals(oe)) {
            setFlag.setImageResource(R.drawable.austria);
        } else if (beercountry.get(randomBeer).equals(pe)) {
            setFlag.setImageResource(R.drawable.peru);
        } else if (beercountry.get(randomBeer).equals(pl)) {
            setFlag.setImageResource(R.drawable.poland);
        } else if (beercountry.get(randomBeer).equals(pt)) {
            setFlag.setImageResource(R.drawable.portugal);
        } else if (beercountry.get(randomBeer).equals(ru)) {
            setFlag.setImageResource(R.drawable.russia);
        } else if (beercountry.get(randomBeer).equals(sco)) {
            setFlag.setImageResource(R.drawable.scotland);
        } else if (beercountry.get(randomBeer).equals(se)) {
            setFlag.setImageResource(R.drawable.sweden);
        } else if (beercountry.get(randomBeer).equals(sg)) {
            setFlag.setImageResource(R.drawable.singapur);
        } else if (beercountry.get(randomBeer).equals(es)) {
            setFlag.setImageResource(R.drawable.spain);
        } else if (beercountry.get(randomBeer).equals(za)) {
            setFlag.setImageResource(R.drawable.southafrica);
        } else if (beercountry.get(randomBeer).equals(tah)) {
            setFlag.setImageResource(R.drawable.tahiti);
        } else if (beercountry.get(randomBeer).equals(tz)) {
            setFlag.setImageResource(R.drawable.tasmain);
        } else if (beercountry.get(randomBeer).equals(th)) {
            setFlag.setImageResource(R.drawable.thailand);
        } else if (beercountry.get(randomBeer).equals(cz)) {
            setFlag.setImageResource(R.drawable.czec);
        } else if (beercountry.get(randomBeer).equals(usa)) {
            setFlag.setImageResource(R.drawable.americanflag);
        } else if (beercountry.get(randomBeer).equals(iceland)) {
            setFlag.setImageResource(R.drawable.iceland);
        } else if (beercountry.get(randomBeer).equals(bulgaria)) {
            setFlag.setImageResource(R.drawable.bulgaria);
        } else if (beercountry.get(randomBeer).equals(tibet)) {
            setFlag.setImageResource(R.drawable.tibet);
        } else if (beercountry.get(randomBeer).equals(venezuela)) {
            setFlag.setImageResource(R.drawable.venezuela);
        } else if (beercountry.get(randomBeer).equals(guatemala)) {
            setFlag.setImageResource(R.drawable.guatemala);
        } else if (beercountry.get(randomBeer).equals(nepal)) {
            setFlag.setImageResource(R.drawable.nepal);
        } else {
            setFlag.setImageResource(R.drawable.blanktwo);
        }

    }

    private void setNoBeerFlag() {
        if (nobeercountry.get(randomBeer).equals(ch)) {
            setFlag.setImageResource(R.drawable.swissflag);
        } else if (nobeercountry.get(randomBeer).equals(ra)) {
            setFlag.setImageResource(R.drawable.argentinia);
        } else if (nobeercountry.get(randomBeer).equals(aus)) {
            setFlag.setImageResource(R.drawable.australia);
        } else if (nobeercountry.get(randomBeer).equals(b)) {
            setFlag.setImageResource(R.drawable.belgium);
        } else if (nobeercountry.get(randomBeer).equals(bol)) {
            setFlag.setImageResource(R.drawable.bolivia);
        } else if (nobeercountry.get(randomBeer).equals(br)) {
            setFlag.setImageResource(R.drawable.brazil);
        } else if (nobeercountry.get(randomBeer).equals(cn)) {
            setFlag.setImageResource(R.drawable.china);
        } else if (nobeercountry.get(randomBeer).equals(dk)) {
            setFlag.setImageResource(R.drawable.denemark);
        } else if (nobeercountry.get(randomBeer).equals(de)) {
            setFlag.setImageResource(R.drawable.germany);
        } else if (nobeercountry.get(randomBeer).equals(dor)) {
            setFlag.setImageResource(R.drawable.dominicanrepublic);
        } else if (nobeercountry.get(randomBeer).equals(eng)) {
            setFlag.setImageResource(R.drawable.england);
        } else if (nobeercountry.get(randomBeer).equals(ee)) {
            setFlag.setImageResource(R.drawable.estland);
        } else if (nobeercountry.get(randomBeer).equals(fi)) {
            setFlag.setImageResource(R.drawable.finland);
        } else if (nobeercountry.get(randomBeer).equals(fr)) {
            setFlag.setImageResource(R.drawable.france);
        } else if (nobeercountry.get(randomBeer).equals(gr)) {
            setFlag.setImageResource(R.drawable.greece);
        } else if (nobeercountry.get(randomBeer).equals(in)) {
            setFlag.setImageResource(R.drawable.india);
        } else if (nobeercountry.get(randomBeer).equals(ir)) {
            setFlag.setImageResource(R.drawable.ireland);
        } else if (nobeercountry.get(randomBeer).equals(it)) {
            setFlag.setImageResource(R.drawable.italy);
        } else if (nobeercountry.get(randomBeer).equals(jm)) {
            setFlag.setImageResource(R.drawable.jamaica);
        } else if (nobeercountry.get(randomBeer).equals(jp)) {
            setFlag.setImageResource(R.drawable.japan);
        } else if (nobeercountry.get(randomBeer).equals(ca)) {
            setFlag.setImageResource(R.drawable.canada);
        } else if (nobeercountry.get(randomBeer).equals(ke)) {
            setFlag.setImageResource(R.drawable.kenya);
        } else if (nobeercountry.get(randomBeer).equals(hr)) {
            setFlag.setImageResource(R.drawable.croatia);
        } else if (nobeercountry.get(randomBeer).equals(cu)) {
            setFlag.setImageResource(R.drawable.cuba);
        } else if (nobeercountry.get(randomBeer).equals(ma)) {
            setFlag.setImageResource(R.drawable.morocco);
        } else if (nobeercountry.get(randomBeer).equals(mx)) {
            setFlag.setImageResource(R.drawable.mexico);
        } else if (nobeercountry.get(randomBeer).equals(mn)) {
            setFlag.setImageResource(R.drawable.mongolia);
        } else if (nobeercountry.get(randomBeer).equals(nz)) {
            setFlag.setImageResource(R.drawable.australia);
        } else if (nobeercountry.get(randomBeer).equals(nl)) {
            setFlag.setImageResource(R.drawable.netherland);
        } else if (nobeercountry.get(randomBeer).equals(oe)) {
            setFlag.setImageResource(R.drawable.austria);
        } else if (nobeercountry.get(randomBeer).equals(pe)) {
            setFlag.setImageResource(R.drawable.peru);
        } else if (nobeercountry.get(randomBeer).equals(pl)) {
            setFlag.setImageResource(R.drawable.poland);
        } else if (nobeercountry.get(randomBeer).equals(pt)) {
            setFlag.setImageResource(R.drawable.portugal);
        } else if (nobeercountry.get(randomBeer).equals(ru)) {
            setFlag.setImageResource(R.drawable.russia);
        } else if (nobeercountry.get(randomBeer).equals(sco)) {
            setFlag.setImageResource(R.drawable.scotland);
        } else if (nobeercountry.get(randomBeer).equals(se)) {
            setFlag.setImageResource(R.drawable.sweden);
        } else if (nobeercountry.get(randomBeer).equals(sg)) {
            setFlag.setImageResource(R.drawable.singapur);
        } else if (nobeercountry.get(randomBeer).equals(es)) {
            setFlag.setImageResource(R.drawable.spain);
        } else if (nobeercountry.get(randomBeer).equals(za)) {
            setFlag.setImageResource(R.drawable.southafrica);
        } else if (nobeercountry.get(randomBeer).equals(tah)) {
            setFlag.setImageResource(R.drawable.tahiti);
        } else if (nobeercountry.get(randomBeer).equals(tz)) {
            setFlag.setImageResource(R.drawable.tasmain);
        } else if (nobeercountry.get(randomBeer).equals(th)) {
            setFlag.setImageResource(R.drawable.thailand);
        } else if (nobeercountry.get(randomBeer).equals(cz)) {
            setFlag.setImageResource(R.drawable.czec);
        } else if (nobeercountry.get(randomBeer).equals(usa)) {
            setFlag.setImageResource(R.drawable.americanflag);
        } else if (nobeercountry.get(randomBeer).equals(iceland)) {
            setFlag.setImageResource(R.drawable.iceland);
        } else if (nobeercountry.get(randomBeer).equals(bulgaria)) {
            setFlag.setImageResource(R.drawable.bulgaria);
        } else if (nobeercountry.get(randomBeer).equals(tibet)) {
            setFlag.setImageResource(R.drawable.tibet);
        } else if (nobeercountry.get(randomBeer).equals(venezuela)) {
            setFlag.setImageResource(R.drawable.venezuela);
        } else if (nobeercountry.get(randomBeer).equals(guatemala)) {
            setFlag.setImageResource(R.drawable.guatemala);
        } else if (nobeercountry.get(randomBeer).equals(nepal)) {
            setFlag.setImageResource(R.drawable.nepal);
        } else {
            setFlag.setImageResource(R.drawable.blanktwo);
        }
    }


}
