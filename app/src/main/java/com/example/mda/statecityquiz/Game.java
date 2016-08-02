package com.example.mda.statecityquiz;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.content.ContentValues;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Game extends Activity
{
    SQLiteDatabase mydb;
    Button Start,Capital,State,Confirm;
    String name,capstate,capstateopposite,caporstateholder,capstateoppositeholder,answer1;
    TextView Round,Counttest,Question2,Answer1,Answer2;
    EditText et1, et2;
    int roundctr = 1,userpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_activity);

        et1= (EditText) findViewById(R.id.name);
        name = et1.getText().toString();

        et2 = (EditText) findViewById(R.id.capitalanswer);
        et2.setEnabled(false);
        et2.setVisibility(View.INVISIBLE);
        String answer1 = et2.getText().toString();

        mydb = openOrCreateDatabase("mydb.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        Round = (TextView) findViewById(R.id.Round);
        Round.setTextColor(Color.TRANSPARENT);

        Start = (Button) findViewById(R.id.StartButton);

        State = (Button) findViewById(R.id.statebutton);
        Capital = (Button) findViewById(R.id.capitalbutton);
        Confirm = (Button) findViewById(R.id.ConfirmButton);
        Counttest = (TextView) findViewById(R.id.Prompt1);
        Question2 = (TextView) findViewById(R.id.Prompt2);
        Answer1 = (TextView) findViewById(R.id.answer1);
        Answer2 = (TextView)findViewById(R.id.answer2);

        Question2.setTextColor(Color.TRANSPARENT);
        Counttest.setTextColor(Color.TRANSPARENT);

        Start.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                NameCheck();
            }
        });
    }
    //Checks if the name isn't blank, if not shoots a error message
    public void NameCheck()
    {
        name = et1.getText().toString();
        if(name.length() == 0)
        {
            Counttest.setTextColor(Color.RED);
            Counttest.setText(getResources().getString(R.string.Proper));
        }
        else
            FirstHalf();
    }
    public int rand(int a, int b)
    {
        return ((int) ((b - a + 1) * Math.random() + a));
    }
    //First question of the game and enabling its buttons for response.
    public void FirstHalf()
    {
        Start.setEnabled(false);
        Counttest.setTextColor(Color.BLACK);
        Round.setTextColor(Color.BLACK);

        Capital.setVisibility(View.VISIBLE);
        Capital.setEnabled(true);
        State.setVisibility(View.VISIBLE);
        State.setEnabled(true);

        Round.setText("Round "+ roundctr + ":");

        Cursor c1 = mydb.rawQuery("select * from states", null, null);

        c1.moveToFirst();
        c1.moveToPosition(rand(0, 49));

        if (rand(1, 2) == 1)
        {
            capstate = c1.getString(1);// Original State
            capstateopposite = c1.getString(2); // Capital of State
            capstateoppositeholder = c1.getColumnName(1);
            caporstateholder = c1.getColumnName(2); //Get the capital of the state
        } else
        {
            capstate = c1.getString(2); //Original Capital
            capstateopposite = c1.getString(1); //State of the Capital
            capstateoppositeholder = c1.getColumnName(2); //Get the Capital column name
            caporstateholder = c1.getColumnName(1); //Get the state column name
        }

        //Counttest.setText(c1.getString(1)+ " " + c1.getString(2));
        Counttest.setText("Is " + capstate + " a State or Capital?");

        State.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Cursor c2 = mydb.rawQuery("select * from states where State = '" + capstate + "'", null, null);
                Answer1.setVisibility(View.VISIBLE);

                if (c2.getCount() == 1)
                {
                    userpoints = userpoints + 10;
                    Answer1.setText(R.string.Correct);
                    Answer1.setTextColor(Color.GREEN);
                    SecondHalf();
                }
                else
                {
                    Answer1.setText(R.string.Wrong);
                    Answer1.setTextColor(Color.RED);
                    WaitthenReset();
                }
            }
        });

        Capital.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Cursor c2 = mydb.rawQuery("select * from states where Capital = '" + capstate + "'", null, null);
                Answer1.setVisibility(View.VISIBLE);
                if(c2.getCount()== 1)
                {
                    userpoints = userpoints + 10 ;
                    Answer1.setText(R.string.Correct);
                    Answer1.setTextColor(Color.GREEN);
                    SecondHalf();
                }
                else
                {
                    Answer1.setText(R.string.Wrong);
                    Answer1.setTextColor(Color.RED);
                    WaitthenReset();
                }
            }
        });


    }
    //Second question that follows up the first question, with its buttons and edittexts
    public void SecondHalf()
    {
        Capital.setEnabled(false);
        State.setEnabled(false);

        et2.setEnabled(true);
        et2.setVisibility(View.VISIBLE);

        Question2.setTextColor(Color.BLACK);
        Question2.setText("What is the " + caporstateholder + " of " + capstate);
        Confirm.setVisibility(View.VISIBLE);

        Confirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                answer1 = et2.getText().toString().toLowerCase().trim();

                Answer2.setVisibility(View.VISIBLE);

                if (answer1.equals(capstateopposite.trim().toLowerCase()))
                {
                    userpoints = userpoints + 10;
                    Answer2.setTextColor(Color.GREEN);
                    Answer2.setText(R.string.Correct);
                }
                else
                {
                    Answer2.setText(R.string.Wrong);
                    Answer2.setTextColor(Color.RED);
                }
                WaitthenReset();
            }
        });

    }
    //Sets everything to where it was before the Firsthalf() and Secondhalf() functions.
    public void ResetRound()
    {

        if(roundctr >= 5)
        {
            ContentValues cv = new ContentValues();
            cv.put ("username", name);
            cv.put ("score",userpoints);
            long recnum = mydb.insertOrThrow("scores",null,cv);
            Intent i2 = new Intent(Game.this,ScoreScreen.class);
            startActivity(i2);
        }
        Start.setEnabled(true);
        Counttest.setTextColor(Color.TRANSPARENT);
        Round.setTextColor(Color.TRANSPARENT);
        Question2.setTextColor(Color.TRANSPARENT);
        Capital.setVisibility(View.INVISIBLE);
        State.setVisibility(View.INVISIBLE);
        Confirm.setVisibility(View.INVISIBLE);
        Answer1.setTextColor(Color.TRANSPARENT);
        Answer2.setTextColor(Color.TRANSPARENT);

        et2.setEnabled(false);
        et2.setVisibility(View.INVISIBLE);
        et2.setText("");

        roundctr++;

        FirstHalf();
    }
    //Delay so user can see if they are correct or not with answers before round reset.
    public void WaitthenReset()
    {
        new CountDownTimer(2000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {

            }

            @Override
            public void onFinish()
            {
                ResetRound();
            }
        }.start();
    }

}

