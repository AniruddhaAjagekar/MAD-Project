package com.example.typingtestproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HistoryDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TypeHistory";
    public static final int  DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "TypeInfo";
    public static final String DATE = "date";
    public static final String ACC = "accuracy";
    public static final String SPEED = "speed";
    public static final String CORRECT_WORDS = "correct_words";
    public static final String WRONG_WORDS = "wrong_words";


    public HistoryDatabase(Context context){super(context,DATABASE_NAME,null,DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"( id INTEGER PRIMARY KEY AUTOINCREMENT ,"+DATE+" TEXT,"+ACC+" TEXT ,"+SPEED+" TEXT, "+CORRECT_WORDS+" TEXT,"+WRONG_WORDS+" TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db , int oldVersion,int newVersion){

    }

    public boolean insert(String date,String acc,String speed,String correct_words,String wrong_words){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues con = new ContentValues();
        con.put(DATE,date);
        con.put(ACC,acc);
        con.put(SPEED,speed);
        con.put(WRONG_WORDS,wrong_words);
        con.put(CORRECT_WORDS,correct_words);
        long val =  db.insert(TABLE_NAME,null,con);
        return val > 0;
    }
    public Cursor read(){
        SQLiteDatabase db = getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_NAME ;
            Cursor cur = db.rawQuery(query, null);

            if(cur.getCount() > 0)
            {
                return cur;
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

