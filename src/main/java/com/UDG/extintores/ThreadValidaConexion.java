package com.UDG.extintores;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ThreadValidaConexion extends Thread {
    Context context;
    String URL;
    public boolean status=false;

    public ThreadValidaConexion(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        URL = "holaMundo.com";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        status = true;
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
                    }
                }
        );
        requestQueue.add(postRequest);
    }
}
