package com.UDG.extintores;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivityIntro extends AppCompatActivity {

    private final int DURACION_SPLASH = 3000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);
    }
    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                finish();
            };
        }, DURACION_SPLASH);
    }
}
