package com.example.stockbeaver;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    final String TAG = "Sample";
    protected Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //make sure that email exists and the password matches when login is clicked
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Signup.this, com.example.stockbeaver.MainActivity.class);
                String email = emailEditText.getText().toString();
                myIntent.putExtra(MainActivity.EXTRA_MESSAGE + "email", email);
                startActivity(myIntent);
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), com.example.stockbeaver.MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}

