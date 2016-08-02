/*
Created by: Andrew Chow
Cs 211d
5/20/2016
Objective of this app is to create a quiz game with city and states and be able to save thie scores
into a database and display them when the user wants to see them.
 */

package com.example.mda.statecityquiz;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.content.ContentValues;
import android.widget.Button;


public class Splash extends Activity
{
    SQLiteDatabase mydb;
    Button Play, Score, Exit;

    String States [] = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut",
    "Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas",
    "Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi",
    "Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico", "New York",
    "North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island"
    ,"South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington",
    "West Virginia","Wisconsin","Wyoming"};

    String Capitals [] = {"Montgomery","Juneau","Phoenix","Little Rock","Sacramento","Denver","Hartford",
    "Dover","Tallahassee","Atlanta","Honolulu","Boise","Springfield","Indianapolis","Des Moines","Topeka",
    "Frankfort","Baton Rouge","Augusta","Annapolis","Boston","Lansing","Saint Paul","Jackson",
    "Jefferson City","Helena","Lincoln","Carson City","Concord","Trenton","Santa Fe", "Albany",
    "Raleigh","Bismarck","Columbus","Oklahoma City","Salem","Harrisburg","Providence"
    ,"Columbia","Pierre","Nashville","Austin","Salt Lake City","Montpelier","Richmond","Olympia",
    "Charleston","Madison","Cheyenne"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_splash);
        Play = (Button) findViewById(R.id.Play);
        Score = (Button) findViewById(R.id.Scores);
        Exit = (Button) findViewById(R.id.Exit);

        Play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    Intent i = new Intent(Splash.this,Game.class);
                    startActivity(i);
            }
        });

        Score.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i2 = new Intent(Splash.this,ScoreScreen.class);
                startActivity(i2);
            }
        });

        Exit.setOnClickListener(new View.OnClickListener()
        {
             @Override
             public void onClick(View v)
             {
                 finish();
                 System.exit(0);
             }
        });

        mydb = openOrCreateDatabase("mydb.db",SQLiteDatabase.CREATE_IF_NECESSARY, null);
        mydb.setVersion(1);

        String execute = "drop table if exists states";
        mydb.execSQL(execute);

        String execute1 = "create table if not exists scores (id integer primary key autoincrement "+ ",username text not null, score integer);";
        mydb.execSQL(execute1);

        String execute2 = "create table if not exists states (id integer primary key autoincrement"+ ",State text not null, Capital text not null);";
        mydb.execSQL(execute2);

        ContentValues cv = new ContentValues();
        for(int i=0; i < 50; i++)
        {
            cv.put("State", States[i]);
            cv.put("Capital", Capitals[i]);
            long recNum = mydb.insert("states",null,cv);
        }

    }
}
