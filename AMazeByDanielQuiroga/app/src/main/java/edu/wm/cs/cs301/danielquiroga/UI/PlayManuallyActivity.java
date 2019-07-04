package edu.wm.cs.cs301.danielquiroga.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
//import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.media.MediaPlayer;

//import edu.wm.cs.cs301.danielquiroga.generation.Cells;
import edu.wm.cs.cs301.danielquiroga.generation.CardinalDirection;
import edu.wm.cs.cs301.danielquiroga.generation.MazeConfiguration;
import edu.wm.cs.cs301.danielquiroga.gui.Constants;
//import edu.wm.cs.cs301.danielquiroga.gui.State;
import edu.wm.cs.cs301.danielquiroga.gui.StatePlaying;

import edu.wm.cs.cs301.danielquiroga.gui.MazePanel;
import edu.wm.cs.daniel.daniel.R;

import static edu.wm.cs.cs301.danielquiroga.generation.CardinalDirection.*;


public class PlayManuallyActivity extends AppCompatActivity {

    ToggleButton map, path, wall;
    Button mapM, mapP, right, left, forward, menu;

    Integer pathLen = 0;
    Double batLev = 0.0;

    String tag = "PlayManualAcitivity";
    StatePlaying currentState = new StatePlaying();
    MazeConfiguration maze = GeneratingActivity.getMazeConfig();
    String dir;


    //MazePanel panel = findViewById(R.id.maze_panel);

    /**
     * the onCreate mehtod is called whenever this activity is
     * displayed. Inside the method I call on all the added features
     * I have added to the screen such as buttons, spinners, and bars.
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        currentState.setMazeConfiguration(GeneratingActivity.getMazeConfig());
        currentState.start((MazePanel) findViewById(R.id.maze_panel));
        final TextView direction = findViewById(R.id.direction1);
        direction.setText("East");
        final MediaPlayer music = MediaPlayer.create(this, R.raw.playing);
        music.setLooping(true);
        music.start();
        //panel.update();

        menu = findViewById(R.id.manual_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(PlayManuallyActivity.this, AMazeActivity.class);
                music.stop();
                startActivity(menu);
                finish();
            }
        });

        map = findViewById(R.id.toggle_map);
        map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //Toast.makeText(PlayManuallyActivity.this, "Map toggle ON", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
                    Log.v(tag, "Map toggle ON");
                } else {
                    // The toggle is disabled
                    //Toast.makeText(PlayManuallyActivity.this, "Map toggle OFF", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
                    Log.v(tag, "Map toggle OFF");
                }
            }
        });

        path = findViewById(R.id.toggle_path);
        path.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //Toast.makeText(PlayManuallyActivity.this, "Path toggle ON", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleSolution, 0 );
                    Log.v(tag, "Path toggle ON");
                } else {
                    // The toggle is disabled
                    //Toast.makeText(PlayManuallyActivity.this, "Path toggle OFF", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleSolution, 0 );
                    Log.v(tag, "Path toggle OFF");
                }
            }
        });

        wall = findViewById(R.id.toggle_walls);
        wall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //Toast.makeText(PlayManuallyActivity.this, "Wall toggle ON", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0 );
                    Log.v(tag, "Wall toggle ON");
                } else {
                    // The toggle is disabled
                    //Toast.makeText(PlayManuallyActivity.this, "Wall toggle OFF", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0 );
                    Log.v(tag, "Wall toggle OFF");
                }
            }
        });

        mapM = findViewById(R.id.button_map_minus);
        mapM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PlayManuallyActivity.this, "Clicked MAP MINUS", Toast.LENGTH_SHORT).show();
                currentState.keyDown(Constants.UserInput.ZoomOut, 0 );
                Log.v(tag, "Clicked MAP MINUS");
            }
        });

        mapP = findViewById(R.id.button_map_plus);
        mapP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PlayManuallyActivity.this, "Clicked MAP PLUS", Toast.LENGTH_SHORT).show();
                currentState.keyDown(Constants.UserInput.ZoomIn, 0 );
                Log.v(tag, "Clicked MAP PLUS");
            }
        });

        forward = findViewById(R.id.key_up);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PlayManuallyActivity.this, "MOVE UP", Toast.LENGTH_SHORT).show();
                int x = currentState.getCurrentPosition()[0];
                int y = currentState.getCurrentPosition()[1];

                currentState.keyDown(Constants.UserInput.Up, 0 );
                int newx = currentState.getCurrentPosition()[0];
                int newy = currentState.getCurrentPosition()[1];
                if((x != newx) || (y != newy)){
                    batLev += 5.0;
                    pathLen += 3;
                }
                if(!maze.isValidPosition(newx, newy)){
                    music.stop();
                    switchToWinning();
                }
                CardinalDirection direc = currentState.getCurrentDirection();

                direction.setText(String.valueOf(direc));
                Log.v(tag, String.valueOf(direc));
                Log.v(tag, "MOVE UP");
            }
        });

        right = findViewById(R.id.key_right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PlayManuallyActivity.this, "MOVE RIGHT", Toast.LENGTH_SHORT).show();
                currentState.keyDown(Constants.UserInput.Right, 0 );
                batLev += 3.0;
                CardinalDirection direc = currentState.getCurrentDirection();

                direction.setText(String.valueOf(direc));
                Log.v(tag, String.valueOf(direc));
                Log.v(tag, "MOVE RIGHT");
            }
        });

        left = findViewById(R.id.key_left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PlayManuallyActivity.this, "MOVE LEFT", Toast.LENGTH_SHORT).show();
                currentState.keyDown(Constants.UserInput.Left, 0 );
                batLev += 3.0;
                CardinalDirection direc = currentState.getCurrentDirection();

                direction.setText(String.valueOf(direc));
                Log.v(tag, String.valueOf(direc));
                Log.v(tag, "MOVE LEFT");
            }
        });


    }



    /**
     * method is called when the user selects to switch to the winning
     * screen from the app. this method will also be responsible to pass
     * on the information relevant to the robot such as path length.
     */
    public void switchToWinning(){

        //Toast.makeText(this, "Clicked to go to winning screen", Toast.LENGTH_SHORT).show();
        Log.v(tag, "clicked to go to winning screen");

        Intent winning = new Intent(this, WinningActivity.class);

        // this is adds the information from the robot to the final screen
        winning.putExtra("battery", String.valueOf(batLev));
        winning.putExtra("path_length", String.valueOf(pathLen));


        startActivity(winning);

        finish();
    }


}

