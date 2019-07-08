package com.comp259app2.berezowskib.mapsdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {
    //Information regarding the Database
    //Name and the columns that are in the database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LocationManager";
    private static final String TABLE_NAME = "locations";
    private static final String KEY_ID = "_id";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_DATE = "ndate";


    public DBHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

//This method creates the table  within the database
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_LAT + " TEXT,"
                + KEY_LON + " TEXT,"
                + KEY_DETAILS + " TEXT,"
                 + KEY_DATE + " TEXT)");
    }


    //When the version of the datbase is different than the one contained on the disk
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    //This method creates a location within the database
    public void createLocation(Notification notification){
        SQLiteDatabase db = getWritableDatabase();

        String insert = "INSERT or replace INTO " + TABLE_NAME +  " ("
                + KEY_LAT +", "
                + KEY_LON + ", "
                + KEY_DETAILS + ","
                + KEY_DATE +") " +
                "VALUES ('"
                + notification.getLat() + "', '"
                + notification.getLon() + "', '"
                + notification.getDescription() +"', '"
                + notification.getDate() + "')" ;
        //executes the command as well closes the connection
        db.execSQL(insert);
        db.close();
    }


    //Removes a Row from the database
//    public void deleteContact(Notification notification){
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(pet.getId())});
//        db.close();
//    }

    //Retrieves the amount of records within the database

        public int getLocationCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }


    //Retrieves all the rows that are within the database
    public List<Notification> getAllLocations(){
        List<Notification> allNotifications = new ArrayList<Notification>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        if(cursor.moveToFirst()){

            do{
                allNotifications.add(new Notification(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4)));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return allNotifications;
    }

}

