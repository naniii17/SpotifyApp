package com.example.applispotifyfinal.Controler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.applispotifyfinal.Model.User;
import com.example.applispotifyfinal.R;

public class MainActivity extends AppCompatActivity {


    /*Declaration de nos Views*/
    Button butonPlay;
    TextView textViewWhatsYourName;
    EditText editTextTypeYourName;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Referencement des nos Views*/
        butonPlay = findViewById(R.id.idBoutonLetPlay);
        textViewWhatsYourName = findViewById(R.id.idTextViewYourName);
        editTextTypeYourName = findViewById(R.id.idEditTextTypeYourName);
        user = new User();
    }

    /*Methode onClick de notre Button qui lance la nouvelle activité et save le pseudo*/
    public void redirection(View view) {

        String pseudo = editTextTypeYourName.getText().toString();
        user.setPseudo(pseudo);
    Intent intent = new Intent(MainActivity.this, JeuActivity.class);
    startActivity(intent);

    }
}