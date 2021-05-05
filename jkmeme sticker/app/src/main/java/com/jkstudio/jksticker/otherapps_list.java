package com.jkstudio.jksticker;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class otherapps_list extends AppCompatActivity {


    //=====btn var=======
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherapps_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //=============spinner1===============
        Spinner spinner = (Spinner) findViewById(R.id.spinnergame);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.games_spinner_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        //=============spinner2===============
        Spinner spinner2 = (Spinner) findViewById(R.id.spinnergreeting);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.greetings_spinner_array, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);

        //=============spinne3r===============
        Spinner spinner3 = (Spinner) findViewById(R.id.spinnermeme);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.meme_spinner_array, android.R.layout.simple_spinner_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner3.setAdapter(adapter3);

        //=============spinner4===============
        Spinner spinner4 = (Spinner) findViewById(R.id.spinneractor);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.actor_spinner_array, android.R.layout.simple_spinner_item);

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner4.setAdapter(adapter4);


        //=============spinner5===============
        Spinner spinner5 = (Spinner) findViewById(R.id.spinnermovie);

        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.movie_spinner_array, android.R.layout.simple_spinner_item);

        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner5.setAdapter(adapter5);

        //=============spinner6===============
        Spinner spinner6 = (Spinner) findViewById(R.id.spinneryoytuber);

        ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.youtuber_spinner_array, android.R.layout.simple_spinner_item);

        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner6.setAdapter(adapter6);


        //=============spinner7===============
        Spinner spinner7 = (Spinner) findViewById(R.id.spinnerimoji);

        ArrayAdapter<CharSequence> adapter7 = ArrayAdapter.createFromResource(this,
                R.array.imoji_spinner_array, android.R.layout.simple_spinner_item);

        adapter7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner7.setAdapter(adapter7);

        //=============spinner8===============
        Spinner spinner8 = (Spinner) findViewById(R.id.spinnercartoon);

        ArrayAdapter<CharSequence> adapter8 = ArrayAdapter.createFromResource(this,
                R.array.cartoon_spinner_array, android.R.layout.simple_spinner_item);

        adapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner8.setAdapter(adapter8);





        //==========buttton1========

        btn1 =  findViewById(R.id.getcartoon);
        btn1.setOnClickListener(v ->
        {
            Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();
        });

        //==========buttton2========

        btn2 =  findViewById(R.id.getmeme);
        btn2.setOnClickListener(v ->
        { Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();

        });

        //==========buttton3========

        btn3 =  findViewById(R.id.getactor);
        btn3.setOnClickListener(v ->
        { Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();

        });

        //==========buttton4========

        btn4 =  findViewById(R.id.getyoutuber);
        btn4.setOnClickListener(v ->
        { Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();

        });

        //==========buttton5========

        btn5 =  findViewById(R.id.getmovie);
        btn5.setOnClickListener(v ->
        { Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();

        });

        //==========buttton6========

        btn6 =  findViewById(R.id.getimoji);
        btn6.setOnClickListener(v ->
        { Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();

        });

        //==========buttton7========

        btn7 =  findViewById(R.id.getgames);
        btn7.setOnClickListener(v ->
        { Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();

        });


        //==========buttton8========

        btn8 =  findViewById(R.id.getgreeting);
        btn8.setOnClickListener(v ->
        { Toast.makeText(this, "coming soon", Toast.LENGTH_SHORT).show();

        });




    }
}