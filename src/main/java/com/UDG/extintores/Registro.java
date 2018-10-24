package com.UDG.extintores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends Activity {

    Spinner careerList, semesterList, genderList;
    ArrayList<String> colectionCareer, colectionSemester, colectionGender;
    ArrayAdapter<String> adapterCareer, adaptarSemester, adapterGender;
    int gender;
    String idUsuario;
    User user;
    String URL;
    boolean status=false;
    ThreadSincronizador tr;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        careerList = (Spinner) findViewById(R.id.careerList);
        semesterList = (Spinner) findViewById(R.id.semesterList);
        genderList = (Spinner) findViewById(R.id.genderList);

        colectionCareer = new ArrayList<String>();

        // carreras
        /*/for (int i = 0; i<10; i++){
            colectionCareer.add(Integer.toString(i));
        }*/
        colectionCareer.add("1.- Ing. en Computación");
        colectionCareer.add("2.- Ing, en Logística y Transporte");
        colectionCareer.add("3.- Ing. Fotónica");
        colectionCareer.add("4.- Lic. en Ciencia de Materiales");
        colectionCareer.add("5.- Lic. en Física");
        colectionCareer.add("6.- Lic. en Ingeniería Biomédica");
        colectionCareer.add("7.- Lic. en Ingeniería Civil");
        colectionCareer.add("8.- Lic. en Ing. en Alimentos y Biotecnología");
        colectionCareer.add("9.- Lic. en Ing. en Comunicaciones");
        colectionCareer.add("10.- Lic. en Ing. Industrial");
        colectionCareer.add("11.- Lic. en Ing. Informática");
        colectionCareer.add("12.- Lic. en Ing. Mecánica Eléctrica");
        colectionCareer.add("13.- Lic. en Ing. Química");
        colectionCareer.add("14.- Lic. en Ing. Topográfica");
        colectionCareer.add("15.- Lic. en Matemáticas");
        colectionCareer.add("16.- Lic. en Química");
        colectionCareer.add("17.- Lic. en Químico Farmacéutico Biólogo");
        colectionCareer.add("18.- Robótica");


        adapterCareer = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colectionCareer);

        careerList.setAdapter(adapterCareer);

        // semestres
        colectionSemester = new ArrayList<String>();
        for (int i = 0; i < 16; i++) {
            colectionSemester.add(Integer.toString(i + 1));
        }
        adaptarSemester = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colectionSemester);
        semesterList.setAdapter(adaptarSemester);

        // sexo
        colectionGender = new ArrayList<String>();

        colectionGender.add("M");
        colectionGender.add("F");

        adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colectionGender);
        genderList.setAdapter(adapterGender);
    }

    private int posCareer(String career) {
        switch (career) {
            case "1.- Ing. en Computación":
                return 1;
            case "2.- Ing, en Logística y Transporte":
                return 2;
            case "3.- Ing. Fotónica":
                return 3;
            case "4.- Lic. en Ciencia de Materiales":
                return 4;
            case "5.- Lic. en Física":
                return 5;
            case "6.- Lic. en Ingeniería Biomédica":
                return 6;
            case "7.- Lic. en Ingeniería Civil":
                return 7;
            case "8.- Lic. en Ing. en Alimentos y Biotecnología":
                return 8;
            case "9.- Lic. en Ing. en Comunicaciones":
                return 9;
            case "10.- Lic. en Ing. Industrial":
                return 10;
            case "11.- Lic. en Ing. Informática":
                return 11;
            case "12.- Lic. en Ing. Mecánica Eléctrica":
                return 12;
            case "13.- Lic. en Ing. Química":
                return 13;
            case "14.- Lic. en Ing. Topográfica":
                return 14;
            case "15.- Lic. en Matemáticas":
                return 15;
            case "16.- Lic. en Química":
                return 16;
            case "17.- Lic. en Químico Farmacéutico Biólogo":
                return 17;
            case "18.- Robótica":
                return 18;
            default:
                return 0;
        }
    }

    private boolean validarEntradas() {
        EditText editTextName, editTextAge,editTextEmail;
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);


        if (editTextName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Missing name ", Toast.LENGTH_SHORT).show();
            editTextName.requestFocus();
            return false;
        }else if (editTextEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Missing email ", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            editTextEmail.requestFocus();
            return false;
        }
        else if (editTextAge.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Missing age ", Toast.LENGTH_SHORT).show();
            editTextAge.requestFocus();
            return false;
        }
        else if (Integer.parseInt(editTextAge.getText().toString()) == 0) {
            Toast.makeText(this, "Age can not be 0", Toast.LENGTH_SHORT).show();
            editTextAge.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isNetDisponible(){
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
    public void pressNext(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText editTextName, editTextAge,editTextEmail;

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        if (validarEntradas()) {
            if (genderList.getSelectedItem().toString() == "M") {
                gender = 0;
            } else {
                gender = 1;
            }
            user = new User();
            user.name=editTextName.getText().toString();
            user.age=Integer.parseInt(editTextAge.getText().toString());
            user.career=posCareer(careerList.getSelectedItem().toString());
            user.semester=Integer.parseInt(semesterList.getSelectedItem().toString());
            user.gender=gender;
            user.email=editTextEmail.getText().toString();
            user.score=0;

            if (isNetDisponible()){
                isOnlineNet();
            }
            else
            {
                Intent cuestionario = new Intent(getApplicationContext(), Cuestionario.class);
                user.actualizable = false;
                cuestionario.putExtra("user", (Serializable) user); // user
                startActivityForResult(cuestionario,3);
                //startActivity(cuestionario);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        finish();
    }
    void continuarEjecucion(){
        if(status)
        {
            URL = "http://holaMundo.com";
            //URL = "http://holaMundo.com";
            if (requestQueue == null)
                requestQueue = Volley.newRequestQueue(this);

            StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d("Response", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                jsonObject = jsonObject.getJSONObject("data");
                                idUsuario = jsonObject.getString("insertId");
                                user.id = Integer.parseInt(idUsuario);
                                Log.d("IdUsuario", idUsuario.toString());

                                Intent cuestionario = new Intent(getApplicationContext(), Cuestionario.class);
                                //cuestionario.putExtra("idUser", idUsuario.toString()); // idUser
                                user.actualizable = true;
                                cuestionario.putExtra("user", (Serializable) user); // user
                                startActivityForResult(cuestionario,3);

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
                    params.put("name", user.name);
                    params.put("age", Integer.toString(user.age));
                    params.put("career", Integer.toString(user.career));
                    params.put("semester", Integer.toString(user.semester));
                    params.put("gender", Integer.toString(user.gender));
                    params.put("email", user.email);
                    params.put("score", "0");
                    return params;
                }
            };
            requestQueue.add(postRequest);
        }
        else
        {
            Intent cuestionario = new Intent(getApplicationContext(), Cuestionario.class);
            user.actualizable = false;
            cuestionario.putExtra("user", (Serializable) user); // user
            startActivityForResult(cuestionario,3);
        }
    }
}
