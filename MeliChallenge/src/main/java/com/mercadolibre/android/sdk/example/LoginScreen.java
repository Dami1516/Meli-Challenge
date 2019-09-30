package com.mercadolibre.android.sdk.example;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadolibre.android.sdk.Identity;
import com.mercadolibre.android.sdk.Meli;

public class LoginScreen extends AppCompatActivity {


    // Request code used to receive callbacks from the SDK
    private static final int REQUEST_CODE = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        //if (Meli.getCurrentIdentity(this.getApplicationContext())!=null && Meli.getCurrentIdentity(this.getApplicationContext()).getAccessToken().getAccessTokenLifetime()==0) {
        borrarAccessToken();
        //}
        if(savedInstanceState == null) {
            // ask the SDK to start the login process
            Meli.startLogin(this, REQUEST_CODE);
        }
    }

    //Método casero para eliminar le sesión actual, no pude encontrar la forma correcta de hacerlo
    private void borrarAccessToken(){
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName() + ".access_token", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                processLoginProcessCompleted();
            } else {
                processLoginProcessWithError();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void processLoginProcessCompleted() {
        Identity identity = Meli.getCurrentIdentity(getApplicationContext());
        if (identity != null) {
            this.finish();
        }
    }




    private void processLoginProcessWithError() {
        Toast.makeText(LoginScreen.this, "Oooops, algo ocurrio en el proceso de logueo", Toast.LENGTH_SHORT).show();
    }
}
