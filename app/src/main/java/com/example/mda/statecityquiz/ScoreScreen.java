package com.example.mda.statecityquiz;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;

public class ScoreScreen extends ListActivity
{
    SQLiteDatabase mydb;
    String name;
    int userscore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.score_screen);

        mydb = openOrCreateDatabase("mydb.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Cursor c = mydb.query("scores",null,null,null,null,null,"score DESC",null);

        String[] top10 = {" ", " ", " "," ", " "," "," " ," " ," " ," " };
        String Test = "BLAH BLAH BLAH";
        top10[0] = Test;

        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++)
        {
            name = c.getString(1);
            userscore = c.getInt(2);

            top10[i] = name + "   " + userscore;
            c.moveToNext();
        }
        mydb.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(),android.R.layout.simple_list_item_1,top10);
        getListView().setAdapter(adapter);
    }
}
