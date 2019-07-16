package nandroid.in.gaap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.security.AccessController.getContext;

public class LoginRegisterActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
                    RegisterFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);


        if (InternetConnection.checkConnection(getApplicationContext())) {

            SharedPreferences pref = getSharedPreferences("MyPref", 0);
            try {

                final String uName = pref.getString("user",null);
                if(uName.isEmpty()) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.loginregFragment, new HomeFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    startActivity(new Intent(LoginRegisterActivity.this, MainActivity.class));
                }

            } catch (Exception e) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.loginregFragment, new HomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }



        } else {
            // Not Available...


            new AlertDialog.Builder(getApplicationContext())
                    .setTitle("No Internet connectivity!")
                    .setMessage("Don't panic! Just enable Internet connection and try again!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(LoginRegisterActivity.this, LoginRegisterActivity.class));
                        }
                    })
                    .show();

        }



    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

