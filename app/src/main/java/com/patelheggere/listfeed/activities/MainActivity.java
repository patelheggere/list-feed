package com.patelheggere.listfeed.activities;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.patelheggere.listfeed.R;
import com.patelheggere.listfeed.adapter.ViewPagerAdapter;
import com.patelheggere.listfeed.fragments.NewsFragment;
import com.patelheggere.listfeed.fragments.UploadFragment;
import com.patelheggere.listfeed.fragments.grievence_fragment;
import com.patelheggere.listfeed.helper.LocaleHelper;

import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {


	private android.support.v7.widget.Toolbar toolbar;
	private TabLayout tabLayout;
	private ViewPager viewPager;
	public ViewPagerAdapter adapter;
	private Configuration config;
	FirebaseAuth mAuth = FirebaseAuth.getInstance();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(!checkPermission())
		requestPermission();
		FirebaseUser user = mAuth.getCurrentUser();
		if (user != null) {
			// do your stuff
		} else {
			signInAnonymously();
		}
		//toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);


		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
		getSupportActionBar().setTitle("           "+getString(R.string.name));
		getSupportActionBar().setLogo(R.drawable.wwe_logo);
		getSupportActionBar().setDisplayUseLogoEnabled(true);

		//ActionBar bar = getActionBar();
		//bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000")));

		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		setupViewPager(viewPager);

		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				//getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.tab_color_selector));
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

	}


	private void setupViewPager(ViewPager viewPager) {
		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(new NewsFragment(), getString(R.string.news_tab));
		adapter.addFragment(new grievence_fragment(),getString(R.string.grievence_tab));
		//adapter.addFragment(new UploadFragment(),"Upload");
		//adapter.addFragment(new ThreeFragment(), "THREE");
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.eng:
				Context context = LocaleHelper.setLocale(this, "en");
				Resources resources = context.getResources();
				LocaleHelper.onAttach(context,"en");
				restartActivity();
				/*String languageToLoad = "en"; // your language
				Locale locale = new Locale(languageToLoad);
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config,
						getBaseContext().getResources().getDisplayMetrics());*/
				this.setContentView(R.layout.activity_main);

				break;
			case R.id.kan:
				 context = LocaleHelper.setLocale(this, "kn");
				 resources = context.getResources();
				LocaleHelper.onAttach(context,"kn");
				restartActivity();

				/*languageToLoad = "kn"; // your language
				locale = new Locale(languageToLoad);
				Locale.setDefault(locale);
				config = new Configuration();
				config.locale = locale;
				getBaseContext().getResources().updateConfiguration(config,
						getBaseContext().getResources().getDisplayMetrics());*/
				this.setContentView(R.layout.activity_main);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void restartActivity() {
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.onAttach(base, "kn"));
	}


	private boolean checkPermission(){
		int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
		if (result == PackageManager.PERMISSION_GRANTED){

			return true;

		} else {

			return false;

		}
	}

	public void requestPermission(){

		if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){

			//Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

		} else {

			ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					//Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

				} else {

					//Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();

				}
				break;
		}
	}


	private void signInAnonymously() {
		mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
			@Override
			public void onSuccess(AuthResult authResult) {
				// do your stuff
			}
		})
				.addOnFailureListener(this, new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Log.e("TAG", "signInAnonymously:FAILURE", exception);
					}
				});
	}


}
