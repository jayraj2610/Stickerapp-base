package com.jkstudio.jkcartoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class contact_me extends AppCompatActivity {


    EditText Name;
    EditText Subject;
    EditText Messsage;
    Button btn_sendmail;
    Button btn_back2;






    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);

        Name = findViewById(R.id.name);
        Subject = findViewById(R.id.subject);
        Messsage = findViewById(R.id.body);
        btn_sendmail = findViewById(R.id.btn_sendmail);
        btn_back2 = findViewById(R.id.btn_to_main2);

        btn_back2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            finish();
            }
        });
        btn_sendmail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( !Name.getText().toString().isEmpty() && !Subject.getText().toString().isEmpty() && !Messsage.getText().toString().isEmpty())
                {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.putExtra(intent.EXTRA_EMAIL,new String[]{"jimmy.devstudio26@gmail.com"});
                    intent.putExtra(intent.EXTRA_SUBJECT,Subject.getText().toString() + " by - " + Name.getText().toString());
                    intent.putExtra(intent.EXTRA_TEXT,Messsage.getText().toString());
                    intent.setData(Uri.parse("mailto:"));
                    if (intent.resolveActivity(getPackageManager()) != null)
                    {
                    startActivity(intent);
                    }else {
                        Toast.makeText(contact_me.this, "No application Available to send Email", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(contact_me.this, "please fill all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}