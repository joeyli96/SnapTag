package com.example.potatotank.dubhacks2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PremainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premain);

        Button one = (Button) findViewById(R.id.button_one);
        Button two = (Button) findViewById(R.id.button_two);

        ImageView logo = (ImageView) findViewById(R.id.imageView_logo);
        logo.setImageResource(R.drawable.snaptag_logo);

        one.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToMainOne = new Intent(getApplicationContext(), MainActivity.class);
                goToMainOne.putExtra("KEY", true);
                startActivity(goToMainOne);
                finish();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToMainTwo = new Intent(getApplicationContext(), MainActivity.class);
                goToMainTwo.putExtra("KEY", false);
                startActivity(goToMainTwo);
                finish();
            }
        });
    }

}
