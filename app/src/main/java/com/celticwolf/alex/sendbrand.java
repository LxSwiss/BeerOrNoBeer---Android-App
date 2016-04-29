package com.celticwolf.alex;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class sendbrand extends Activity implements View.OnClickListener {

    EditText brandname, country, brandcomment;
    String sbrandname, scountry, sbrandcomment, radiobool;
    Button sendBrand;
    RadioButton realbrand, fakebrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendbeer);
        initialize();

    }

    private void initialize() {
        brandname = (EditText) findViewById(R.id.etBrandName);
        country = (EditText) findViewById(R.id.etBrandCountry);
        brandcomment = (EditText) findViewById(R.id.etBrandComment);
        sendBrand = (Button) findViewById(R.id.bSend);
        realbrand = (RadioButton) findViewById(R.id.rbRealBrand);
        fakebrand = (RadioButton) findViewById(R.id.rbFakeBrand);

        sendBrand.setOnClickListener(this);
        realbrand.setOnClickListener(this);
        fakebrand.setOnClickListener(this);
        realbrand.setChecked(true);

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast
                .makeText(
                        this,
                        R.string.wantfakeones,
                        duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public void onClick(View v) {

        if (v.getId() == R.id.rbRealBrand) {
            setRadioBtton();
        } else if (v.getId() == R.id.rbFakeBrand) {
        } else if (v.getId() == R.id.bSend) {
            setRadioBtton();
            //shareonfb();
            //convertEditTextvars();
        }
    }

    private void setRadioBtton() {
        // get selected radio button from radioGroup
        if (realbrand.isChecked()) {
            radiobool = "Real Brand";
        } else {
            radiobool = "Fake Brand";
        }
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
        if (itemId == android.R.id.home) {
            Intent intent = new Intent(this, menu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.item1) {
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
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Beer or no Beer Drinking Game");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, share));
    }

    private void rateApp() {
        String url = "https://play.google.com/store/apps/details?id=com.celticwolf.alex#?t=W251bGwsMSwxLDIxMiwiY29tLmNlbHRpY3dvbGYuYWxleCJd";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void convertEditTextvars() {
        String emailaddress[] = {"lxswiss@gmail.com"};
        sbrandname = brandname.getText().toString();
        scountry = country.getText().toString();
        sbrandcomment = brandcomment.getText().toString();
        String message = "New Brand: \nType: \t" + radiobool + "\nName: \t" + sbrandname + "\nCountry: \t" + scountry + "\nComment: \t" + sbrandcomment;

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailaddress);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[BONB] New Beer Brand!");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(emailIntent);

    }
//	private void shareonfb(){
//		sbrandname = brandname.getText().toString();
//		scountry = country.getText().toString();
//		sbrandcomment = brandcomment.getText().toString();
//		String message = "New Brand \nType: \t" + radiobool+ "\nName: \t"+sbrandname + "\nCountry: \t" + scountry + "\nComment: \t" + sbrandcomment;
//		Intent postOnFacebookWallIntent = new Intent(this, ShareOnFacebook.class);
//		postOnFacebookWallIntent.putExtra("facebookMessage", message);
//		startActivity(postOnFacebookWallIntent);
//	}

}
