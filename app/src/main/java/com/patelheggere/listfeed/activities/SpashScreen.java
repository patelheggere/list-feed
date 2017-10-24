package com.patelheggere.listfeed.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;

import com.patelheggere.listfeed.R;
import com.patelheggere.listfeed.helper.LocaleHelper;

public class SpashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    String Phone,Name, Place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = LocaleHelper.setLocale(this, "kn");
        Resources resources = context.getResources();
        LocaleHelper.onAttach(context,"kn");
        setContentView(R.layout.activity_spash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences settings=getSharedPreferences("prefs",0);
                boolean firstRun=settings.getBoolean("firstRun",false);
                Phone = settings.getString("Phone","");
                if(firstRun==false || Phone=="" )//if running for first time
                //Splash will load for first time
                {
                    SharedPreferences.Editor editor=settings.edit();
                    //editor.putBoolean("firstRun",true);
                    editor.commit();
                    Intent i=new Intent(SpashScreen.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SpashScreen.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();


                }
            }



        }, SPLASH_TIME_OUT);
    }

}
