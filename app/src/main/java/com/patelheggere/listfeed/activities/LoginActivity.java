package com.patelheggere.listfeed.activities;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.patelheggere.listfeed.R;
import com.patelheggere.listfeed.data.userDetails;
import com.patelheggere.listfeed.helper.LocaleHelper;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

     EditText _nameText;
     EditText _placeText;
    EditText _phoneText;
    Button _signupButton;
    TextView _loginLink;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference mFireBaseUserRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setTitle("                            "+getString(R.string.name));
               // restartActivity();
        firebaseDatabase = FirebaseDatabase.getInstance();// firebase instance
        _nameText =(EditText) findViewById(R.id.input_name);
        _placeText = (EditText)findViewById(R.id.input_place);
        _phoneText = (EditText)findViewById(R.id.input_phone);
        _signupButton = (Button)findViewById(R.id.btn_signup);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        /*_loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });*/
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        mFireBaseUserRef = firebaseDatabase.getReference("Users");

        String name = _nameText.getText().toString();
        String place = _placeText.getText().toString();
        String phone = _phoneText.getText().toString();
        userDetails userDetailsObj =new userDetails();
        userDetailsObj.setName(name);
        userDetailsObj.setPhone(phone);
        userDetailsObj.setPlace(place);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        mFireBaseUserRef.child(phone).setValue(userDetailsObj, "null");

        SharedPreferences settings=getSharedPreferences("prefs",0);
        SharedPreferences.Editor editor=settings.edit();
        editor.putBoolean("firstRun",true);
        editor.putString("Phone",phone);
        editor.putString("place",place);
        editor.putString("Name",name);
        editor.commit();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        //System.out.println("signup");
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _placeText.getText().toString();
        String password = _phoneText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(getString(R.string.valid_name));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty()) {
            _placeText.setError(getString(R.string.valid_place));
            valid = false;
        } else {
            _placeText.setError(null);
        }

        if (password.isEmpty() || password.length()!= 10) {
            _phoneText.setError(getString(R.string.valid_Phone));
            valid = false;
        } else {
            _phoneText.setError(null);
        }

        return valid;
    }
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
