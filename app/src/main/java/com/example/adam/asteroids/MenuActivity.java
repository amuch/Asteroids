package com.example.adam.asteroids;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button playButton = (Button) findViewById(R.id.button);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent game;
        game = new Intent(this, GameActivity.class);
        startActivity(game);
    }
}