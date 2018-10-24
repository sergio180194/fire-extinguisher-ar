package com.UDG.extintores;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class localDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "db.db";

    public static final String TABLA_USER = "user";
    public static final String COLUMNA_ID = "id";
    public static final String COLUMNA_NAME = "name";
    public static final String COLUMNA_AGE = "age";
    public static final String COLUMNA_CAREER= "career";
    public static final String COLUMNA_SEMESTER = "semester";
    public static final String COLUMNA_GENDER = "gender";
    public static final String COLUMNA_EMAIL = "email";
    public static final String COLUMNA_SCORE = "score";
    public static final String COLUMNA_ACTUALIZABLE = "actualizable";

    private static final String SQL_CREAR = "create table user ("
            + "id integer primary key autoincrement, "
            + "name varchar(45) DEFAULT NULL, "
            + "age integer DEFAULT NULL, "
            + "career integer DEFAULT NULL, "
            + "semester integer DEFAULT NULL, "
            + "gender integer DEFAULT NULL ,"
            + "email varchar(50) DEFAULT NULL, "
            + "actualizable integer DEFAULT NULL,"
            + "score integer DEFAULT NULL);";

    public localDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void agregar(User u)
    {
        int actualizable;

        if(u.actualizable){
            actualizable=1;
        }else {
            actualizable=0;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMNA_NAME, u.name);
        values.put(COLUMNA_AGE, u.age);
        values.put(COLUMNA_CAREER, u.career);
        values.put(COLUMNA_SEMESTER, u.semester);
        values.put(COLUMNA_GENDER, u.gender);
        values.put(COLUMNA_EMAIL, u.email);
        values.put(COLUMNA_SCORE, u.score);
        values.put(COLUMNA_ACTUALIZABLE, actualizable);
        db.insert(TABLA_USER, null,values);
        db.close();
    }
    public int obtenerUltimoIdUser(){
        int id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM user ORDER BY id DESC",null);

        if (cursor != null)
            cursor.moveToFirst();

        id=cursor.getInt(0);

        cursor.close();
        db.close();
        return id;
    }
    public int numRegistrosDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        int contador=0;
        Cursor cursor=null;

        try {
            cursor = db.rawQuery("SELECT id FROM user ORDER BY id DESC",null);
            if (cursor != null)
                cursor.moveToFirst();
            contador = cursor.getCount();
        } finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();

        }
        db.close();
        return contador;
    }
    public User obtener(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        User user;
        int actualizable;
        //String[] projection = {COLUMNA_ID};

        Cursor cursor =
                db.query(TABLA_USER,
                        null,
                        "id = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();

        user = new User();
        user.id=cursor.getInt(0);
        user.name=cursor.getString(1);
        user.age=cursor.getInt(2);
        user.career=cursor.getInt(3);
        user.semester=cursor.getInt(4);
        user.gender=cursor.getInt(5);
        user.email=cursor.getString(6);
        actualizable=cursor.getInt(6);
        if (actualizable == 1)
            user.actualizable = true;
        else
            user.actualizable = false;
        user.score=cursor.getInt(8);

        cursor.close();
        db.close();

        return user;
    }

    public boolean eliminar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(TABLA_USER,
                    " id = ?",
                    new String[] { String.valueOf (id ) });
            db.close();
            return true;

        }catch(Exception ex){
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
