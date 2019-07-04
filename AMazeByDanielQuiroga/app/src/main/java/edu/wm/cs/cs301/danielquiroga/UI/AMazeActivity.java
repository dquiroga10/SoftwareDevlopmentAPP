package edu.wm.cs.cs301.danielquiroga.UI;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import edu.wm.cs.daniel.daniel.R;


public class AMazeActivity extends AppCompatActivity {


    MediaPlayer music;
    String tag = "AMazeActivity";
    Button revisit, explore;
    /**
     * the onCreate mehtod is called whenever this activity is
     * displayed. Inside the method I call on all the added features
     * I have added to the screen such as buttons, spinners, and bars.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        music = MediaPlayer.create(this, R.raw.start);
        music.setLooping(true);
        music.start();

        // find the spinner used in activity_amaze.xml
        Spinner spin_build = findViewById(R.id.builder);
        // make an array adapter to populate spinner
        ArrayAdapter<CharSequence> adapter_build = ArrayAdapter.createFromResource(this, R.array.builders, android.R.layout.simple_spinner_item);
        // set the drop down menu
        adapter_build.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_build.setAdapter(adapter_build);

        // the spinner in reference of the driver
        Spinner spin_drive = findViewById(R.id.driver);
        // make an array adapter to populate spinner
        ArrayAdapter<CharSequence> adapter_drive = ArrayAdapter.createFromResource(this, R.array.drivers, android.R.layout.simple_spinner_item);
        // set the drop down menu
        adapter_drive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_drive.setAdapter(adapter_drive);

        revisit = findViewById(R.id.revisit_button);
        revisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchtoGeneratingRevisit();
            }
        });

    }

    /**
     * this method is called when one of two buttons is pressed in
     * order to move the app from the title screen to the generating
     * screen
     * @param view
     */
    public void switchtoGenerating(View view){
        // get the selected build
        Spinner spin_build = findViewById(R.id.builder);
        String build = spin_build.getSelectedItem().toString();
        Log.v(tag, build);


        // get the selected driver
        Spinner spin_drive = findViewById(R.id.driver);
        String drive = spin_drive.getSelectedItem().toString();
        Log.v(tag, drive);

        // get the integer selected
        SeekBar bar = findViewById(R.id.size_bar);
        Integer size = bar.getProgress();
        Log.v(tag, String.valueOf(size));

        Intent intent = new Intent(this, GeneratingActivity.class);

        // these pass the user inputed values to GeneratingActivity
        intent.putExtra("builder", build);
        intent.putExtra("driver", drive);
        intent.putExtra("size", String.valueOf(size));
        intent.putExtra("button", "explore");

        // start the activity
        //Toast.makeText(this, "Selected Driver : "+ drive + " Size: " + size + " Builder: " + build, Toast.LENGTH_SHORT).show();
        music.stop();
        startActivity(intent);
    }

    public void switchtoGeneratingRevisit(){

        // get the integer selected
        SeekBar bar = findViewById(R.id.size_bar);
        Integer size = bar.getProgress();
        Log.v(tag, String.valueOf(size));

        // get the selected build
        Spinner spin_build = findViewById(R.id.builder);
        String build = spin_build.getSelectedItem().toString();
        Log.v(tag, build);

        // get the selected driver
        Spinner spin_drive = findViewById(R.id.driver);
        String drive = spin_drive.getSelectedItem().toString();
        Log.v(tag, drive);



        Intent intent = new Intent(this, GeneratingActivity.class);

        Toast.makeText(this, "MAZE IS LOADING FROM A FILE PLEASE WAIT", Toast.LENGTH_SHORT);
        // these pass the user inputed values to GeneratingActivity
        intent.putExtra("builder", build);
        intent.putExtra("driver", drive);
        intent.putExtra("size", String.valueOf(size));

        intent.putExtra("button", "revisit");
        // start the activity
        //Toast.makeText(this, "Selected Driver : "+ drive + " Size: " + size + " Builder: " + build, Toast.LENGTH_SHORT).show();
        music.stop();
        startActivity(intent);

    }

}

