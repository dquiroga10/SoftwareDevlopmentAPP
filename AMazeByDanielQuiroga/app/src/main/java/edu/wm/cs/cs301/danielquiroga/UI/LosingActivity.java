package edu.wm.cs.cs301.danielquiroga.UI;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import edu.wm.cs.daniel.daniel.R;


public class LosingActivity extends AppCompatActivity {


    MediaPlayer music;
    String tag = "LosingActivity";
    /**
     * the onCreate mehtod is called whenever this activity is
     * displayed. Inside the method I call on all the added features
     * I have added to the screen such as buttons, spinners, and bars.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);
        music = MediaPlayer.create(this, R.raw.losing);
        music.setLooping(true);
        music.start();

        // getting information to display on the screen
        Intent messages = getIntent();
        String pathLength = messages.getStringExtra("path_length");
        String batLev = messages.getStringExtra("battery");

        TextView bat = findViewById(R.id.level_bat);
        bat.setText(String.valueOf(batLev));

        TextView path = findViewById(R.id.length_path);
        path.setText(String.valueOf(pathLength));
    }

    public void switchToTitle(View view){
        Log.v(tag, "switching to title");
        Intent title = new Intent(this, AMazeActivity.class);
        music.stop();
        startActivity(title);
    }
}

