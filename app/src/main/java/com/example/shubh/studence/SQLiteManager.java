package com.example.shubh.studence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class SQLiteManager extends SQLiteOpenHelper{
    public final static String DATABASE_NAME = "Student.db";
    public final static String USER_TABLE = "user";
    public final static String CLASS_TABLE = "class";
    public final static String STUDENT_TABLE = "student";
    public final static String PRESENT_TABLE = "present";
    public final static int VERSION = 1;
    public final static String ID ="ID";
    public final static String USERNAME ="USERNAME";
    public final static String PASSWORD ="PASSWORD";
    public final static String EMAIL ="EMAIL";
    public final static String DEPARTMENT ="DEPARTMENT";
    public final static String CLASS_ID ="CLASSID";
    public final static String CLASS_NAME ="CLASSNAME";
    public final static String CLASS_DEPT ="DEPARTMENT";
    public final static String NO_OF_STUDENT ="NOOFSTUDENT";
    /*students table coloumn name....*/
    public final static String STUDENT_ID ="STUDENT_ID";
    public final static String STUDENT_NAME ="STUDENT_NAME";
    public final static String STUDENT_ROLLNO ="STUDENT_ROLLNO";
    public final static String STUDENT_CLASS ="STUDENT_CLASS";
    public final static String STUDENT_FINGERPRINT ="STUDENT_FINGERPRINT";
    /*Present table coloumn name....*/
    public final static String PRE_ID ="PRE_ID";
    public final static String PRE_DATE ="PRE_DATE";
    public final static String PRE_DAY ="PRE_DAY";
    public final static String PRE_TIME ="PRE_TIME";
    public final static String PRE_NAME ="PRE_NAME";
    public final static String PRE_ROLL ="PRE_ROLL";
    public final static String PRE_CLASS ="PRE_CLASS";


    SQLiteDatabase db;


    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        db=this.getWritableDatabase();
        Log.e("database","Database Created...");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + USER_TABLE + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERNAME + " VARCHAR, " + PASSWORD + " VARCHAR, " + EMAIL + " VARCHAR," + DEPARTMENT + " VARCHAR);");
        sqLiteDatabase.execSQL("create table " + CLASS_TABLE + " ( " + CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CLASS_NAME + " VARCHAR, " + CLASS_DEPT + " VARCHAR, " + NO_OF_STUDENT + " INTEGER);");
        sqLiteDatabase.execSQL("create table " + STUDENT_TABLE + " ( " + STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + STUDENT_NAME + " VARCHAR, " + STUDENT_ROLLNO + " VARCHAR, " + STUDENT_CLASS + " VARCHAR," + STUDENT_FINGERPRINT + " BLOB);");
        sqLiteDatabase.execSQL("create table " + PRESENT_TABLE + " ( " + PRE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + PRE_DATE + " VARCHAR, " + PRE_DAY + " VARCHAR, " + PRE_TIME + " VARCHAR, " + PRE_NAME + " VARCHAR," + PRE_ROLL + " VARCHAR," + PRE_CLASS + " VARCHAR);");
        Log.e("database","Table Created...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        Log.e("database","Table Dropped...");
        onCreate(db);
    }

    public void insertuser(String user,String pass,String email,String dept) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERNAME,user);
        contentValues.put(PASSWORD,pass);
        contentValues.put(EMAIL,email);
        contentValues.put(DEPARTMENT,dept);
        db.insert(USER_TABLE, null, contentValues);
        db.close();
        Log.e("database","Record Inserted...");

    }
    public void insertstudent(String r,String n,String c,byte[] templete) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_NAME,n);
        contentValues.put(STUDENT_ROLLNO,r);
        contentValues.put(STUDENT_CLASS,c);
        contentValues.put(STUDENT_FINGERPRINT,templete);
        db.insert(STUDENT_TABLE, null, contentValues);
        db.close();
        Log.e("database","Student Record Inserted...");

    }

    public void insertclass(String cname,String cdept,int no_of_student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASS_NAME,cname);
        contentValues.put(CLASS_DEPT,cdept);
        contentValues.put(NO_OF_STUDENT,no_of_student);

        db.insert(CLASS_TABLE, null, contentValues);
        db.close();
        Log.e("database","Record Inserted...");

    }
    public void insertpresentstudent(String date,String day,String time,String name,String roll,String cls) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRE_DATE,date);
        contentValues.put(PRE_DAY,day);
        contentValues.put(PRE_TIME,time);
        contentValues.put(PRE_NAME,name);
        contentValues.put(PRE_ROLL,roll);
        contentValues.put(PRE_CLASS,cls);
        db.insert(PRESENT_TABLE, null, contentValues);
        db.close();
        Log.e("database","Student Record Inserted...");

    }
    public Cursor getuser(String user, String pass)
    {
        Cursor data = db.rawQuery("SELECT * FROM "+ USER_TABLE +" WHERE " + USERNAME + " = '" + user +"'" +" AND " + PASSWORD + " = '" + pass +"'",null);
        Log.e("database","All record get Sucessfully...");
        return data;
    }
    public Cursor getclassdata()
    {
        Cursor data = db.rawQuery(" SELECT * FROM "+ CLASS_TABLE,null);
        Log.e("database","All class get Sucessfully...");
        return data;
    }
    public Cursor getstudent(String classname)
    {
        Cursor data = db.rawQuery("SELECT * FROM "+ STUDENT_TABLE +" WHERE " + STUDENT_CLASS + " = '" + classname +"'",null);
        Log.e("database","All record get Sucessfully...");
        return data;
    }
    public Cursor getdatedata()
    {
        Cursor data = db.rawQuery(" SELECT Distinct "+ PRE_DATE +" FROM "+ PRESENT_TABLE,null);
        Log.e("database","All data get Sucessfully...");
        return data;
    }
    public Cursor gettimedata()
    {
        Cursor data = db.rawQuery(" SELECT Distinct "+ PRE_TIME +" FROM "+ PRESENT_TABLE,null);
        Log.e("database","All data get Sucessfully...");
        return data;
    }
    public Cursor getclasspdfdata()
    {
        Cursor data = db.rawQuery(" SELECT Distinct "+ PRE_CLASS +" FROM "+ PRESENT_TABLE,null);
        Log.e("database","All data get Sucessfully...");
        return data;
    }
    public Cursor getpresentdata(String classname,String date)
    {
        Cursor data = db.rawQuery("SELECT * FROM "+ PRESENT_TABLE +" WHERE " + PRE_DATE + " = '" + date +"'" +" AND " + PRE_CLASS + " = '" + classname +"'"+ "ORDER BY "+ PRE_ROLL,null);
        Log.e("database","All record get Sucessfully...");
        return data;
    }
}
