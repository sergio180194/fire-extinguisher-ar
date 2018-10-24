package com.UDG.extintores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class Cuestionario extends AppCompatActivity implements Serializable {
    User user;
    ThreadSincronizador tr;
    boolean status;
    String URL;
    RadioButton rB10, rB11, rB20, rB21, rB30, rB31, rB40, rB41, rB50, rB51;
    float puntuacion, calificacion;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        user = (User) getIntent().getExtras().getSerializable("user");
    }

    private boolean validarRespuestas() {
        rB10 = (RadioButton) findViewById(R.id.rB10);
        rB11 = (RadioButton) findViewById(R.id.rB11);
        rB20 = (RadioButton) findViewById(R.id.rB20);
        rB21 = (RadioButton) findViewById(R.id.rB21);
        rB30 = (RadioButton) findViewById(R.id.rB30);
        rB31 = (RadioButton) findViewById(R.id.rB31);
        rB40 = (RadioButton) findViewById(R.id.rB40);
        rB41 = (RadioButton) findViewById(R.id.rB41);
        rB50 = (RadioButton) findViewById(R.id.rB50);
        rB51 = (RadioButton) findViewById(R.id.rB51);

        if (!rB10.isChecked() && !rB11.isChecked()) {
            Toast.makeText(this, "Missing answer question 1", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!rB20.isChecked() && !rB21.isChecked()) {
            Toast.makeText(this, "Missing answer question 2", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!rB30.isChecked() && !rB31.isChecked()) {
            Toast.makeText(this, "Missing answer question 3", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!rB40.isChecked() && !rB41.isChecked()) {
            Toast.makeText(this, "Missing answer question 4", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!rB50.isChecked() && !rB51.isChecked()) {
            Toast.makeText(this, "Missing answer question 5", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean isNetDisponible() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public void isOnlineNet() {
        URL = "http://holaMundo.com";

        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        status = true;
                        continuarEjecucion();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 404) {
                                status = true;
                            }
                        } else {
                            status = false;
                        }
                        continuarEjecucion();
                    }
                }
        );
        requestQueue.add(postRequest);
    }

    public void pressSave(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("SAVE")
                .setMessage("Registration Success...!")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = getIntent();
                                //intent.putExtra("text", text);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

        puntuacion = 0;

        if (validarRespuestas()) {
            if (rB10.isChecked()) {
                puntuacion += 0.2;
            }
            if (rB20.isChecked()) {
                puntuacion += 0.2;
            }
            if (rB31.isChecked()) {
                puntuacion += 0.2;
            }
            if (rB41.isChecked()) {
                puntuacion += 0.2;
            }
            if (rB50.isChecked()) {
                puntuacion += 0.2;
            }
            calificacion = puntuacion * 100 / 1;

            Toast.makeText(this, "Your score is " + calificacion, Toast.LENGTH_SHORT).show();

            if (isNetDisponible()) {
                isOnlineNet();
            } else {
                if ((calificacion / 100) >= 0.5) {
                    user.score = 1;
                } else {
                    user.score = 0;
                }
                localDB lodb = new localDB(getApplicationContext());
                lodb.agregar(user);
                builder.show();
            }
        }
    }

    void continuarEjecucion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("SAVE")
                .setMessage("Registration Success...!")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = getIntent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });

        if (status && user.actualizable) {
            String URL = "http://holaMundo.com" + user.id;
            //String URL = "http://holaMundo.com" + user.id;
            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(this);

            StringRequest postRequest = new StringRequest(Request.Method.PUT, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                jsonObject = jsonObject.getJSONObject("data");

                                builder.show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    if ((calificacion / 100) >= 0.5) {
                        params.put("score", Float.toString(1));
                    } else {
                        params.put("score", Float.toString(0));
                    }
                    //params.put("score", Float.toString(calificacion / 100));
                    return params;
                }
            };
            requestQueue.add(postRequest);
        } else {
            if ((calificacion / 100) >= 0.5) {
                user.score = 1;
            } else {
                user.score = 0;
            }
            localDB lodb = new localDB(getApplicationContext());
            lodb.agregar(user);
            builder.show();
        }
    }
}
