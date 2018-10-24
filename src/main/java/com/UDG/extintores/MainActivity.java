package com.UDG.extintores;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

public class MainActivity extends Activity {
    ThreadSincronizador tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(tr==null){
            Log.e("Inicio thread","Inicio thread");
            tr = new ThreadSincronizador(getApplicationContext());
            tr.start();
        }
    }
    public void changeVuforia(View v) {
        Intent vuforia = new Intent(getApplicationContext(), UnityPlayerActivity.class);
        startActivity(vuforia);
    }

    public void changeSurvey(View v) {
        Intent survey = new Intent(getApplicationContext(), Registro.class);
        startActivityForResult(survey,2);
        //startActivity(survey);
    }

    public void changeMapBox(View v) {
        Intent mapbox = new Intent(getApplicationContext(), MapBox.class);
        startActivity(mapbox);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (tr!=null){
            tr.detener();
        }
        finishAffinity();
    }
}