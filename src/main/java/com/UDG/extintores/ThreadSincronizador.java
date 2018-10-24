package com.UDG.extintores;

import android.content.Context;
import android.util.Log;
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

public class ThreadSincronizador extends Thread implements Serializable {
    Context context;
    User user;
    String URL;
    Boolean status = false;
    RequestQueue requestQueue;
    volatile boolean continuar = true;

    public ThreadSincronizador(Context context) {
        this.context = context;
    }

    public void detener(){
        continuar = false;
    }

    @Override
    public void run() {
        localDB lodb = new localDB(context);
        int idUser;

        while (continuar) {
            Log.e("Num registros: ", Integer.toString(lodb.numRegistrosDB()));
            if (isOnlineNet()) {
                Log.e("Num registros: ", Integer.toString(lodb.numRegistrosDB()));
                if (lodb.numRegistrosDB() > 0) {
                    idUser = lodb.obtenerUltimoIdUser();
                    user = lodb.obtener(idUser);
                    if (user.actualizable) {
                        String URL = "holaMundo.com" + user.id;
                        //String URL = "holaMundo.com" + user.id;

                        if (requestQueue == null)
                            requestQueue = Volley.newRequestQueue(context);

                        StringRequest postRequest = new StringRequest(Request.Method.PUT, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            jsonObject = jsonObject.getJSONObject("data");

                                            Log.d("Insertado ", "Insertado con exito en el servidor");
                                            lodb.eliminar(user.id);

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

                                params.put("score", Integer.toString(user.score));
                                return params;
                            }
                        };
                        requestQueue.add(postRequest);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        URL = "http://holaMundo.com";
                        //URL = "http://holaMundo.com";
                        if (requestQueue == null)
                            requestQueue = Volley.newRequestQueue(context);

                        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            Log.d("Insertado ", "Insertado con exito en el servidor");
                                            lodb.eliminar(user.id);
                                        } catch (Exception e) {
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
                                params.put("name", user.name);
                                params.put("age", Integer.toString(user.age));
                                params.put("career", Integer.toString(user.career));
                                params.put("semester", Integer.toString(user.semester));
                                params.put("gender", Integer.toString(user.gender));
                                params.put("email", user.email);
                                params.put("score", Integer.toString(user.score));
                                return params;
                            }
                        };
                        requestQueue.add(postRequest);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isOnlineNet() {
        URL = "http://holaMundo.com";
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
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
        try {
            requestQueue.add(postRequest);
        } finally {
            return status;
        }
    }
}
