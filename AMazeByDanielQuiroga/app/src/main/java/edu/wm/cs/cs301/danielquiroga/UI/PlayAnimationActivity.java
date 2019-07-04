package edu.wm.cs.cs301.danielquiroga.UI;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
//import android.widget.Toast;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ProgressBar;

import edu.wm.cs.cs301.danielquiroga.generation.CardinalDirection;
import edu.wm.cs.cs301.danielquiroga.generation.MazeConfiguration;
import edu.wm.cs.cs301.danielquiroga.gui.BasicRobot;
import edu.wm.cs.cs301.danielquiroga.gui.Constants;
import edu.wm.cs.cs301.danielquiroga.gui.Explorer;
import edu.wm.cs.cs301.danielquiroga.gui.MazePanel;
import edu.wm.cs.cs301.danielquiroga.gui.Pledge;
import edu.wm.cs.cs301.danielquiroga.gui.RobotDriver;
import edu.wm.cs.cs301.danielquiroga.gui.StatePlaying;
import edu.wm.cs.cs301.danielquiroga.gui.WallFollower;
import edu.wm.cs.cs301.danielquiroga.gui.Wizard;
import edu.wm.cs.daniel.daniel.R;


public class PlayAnimationActivity extends AppCompatActivity {

    ToggleButton map, path, wall, start;
    Button mapM, mapP, menu;



    String tag = "PlayAnimationActivity";

    StatePlaying currentState = new StatePlaying();
    RobotDriver driver;
    BasicRobot robot = new BasicRobot();
    MazeConfiguration maze = GeneratingActivity.getMazeConfig();
    private boolean drive = false;
    private boolean exit = false;
    Handler notice = new Handler();
    String autodrive;
    MediaPlayer music;

    ProgressBar bar;

    /**
     * the onCreate mehtod is called whenever this activity is
     * displayed. Inside the method I call on all the added features
     * I have added to the screen such as buttons, spinners, and bars.
     * @param savedInstanceState .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);
        Intent title = getIntent();
        autodrive = title.getStringExtra("driver");
        currentState.setMazeConfiguration(GeneratingActivity.getMazeConfig());
        currentState.start((MazePanel) findViewById(R.id.maze_panel1));
        robot.setMaze(currentState);
        music = MediaPlayer.create(this, R.raw.playing);
        final TextView direction = findViewById(R.id.direction);
        music.setLooping(true);
        direction.setText("East");
        music.start();
        Log.v(tag, autodrive);
        //Toast.makeText(this, autodrive, Toast.LENGTH_LONG).show();\
        if(autodrive.equals("Wizard")){
            driver = new Wizard();
        }
        else if(autodrive.equals("Explorer")){
            driver = new Explorer();
        }
        else if(autodrive.equals("Pledge")){
            driver = new Pledge();
        }
        else if(autodrive.equals("Wall Follower")) {
            driver = new WallFollower();
        }
        Log.v(tag, String.valueOf(driver));
        driver.setRobot(robot);
        driver.setDimensions(maze.getWidth(), maze.getHeight());
        driver.setDistance(maze.getMazedists());
        currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0);
        currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
        currentState.keyDown(Constants.UserInput.ToggleSolution, 0);
        bar = findViewById(R.id.progressBattery);
        bar.setProgress(3000);


        int x = 0;
        while(x < 20){
            currentState.keyDown(Constants.UserInput.ZoomIn, 0);
            x++;
        }

        menu = findViewById(R.id.Animation_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(PlayAnimationActivity.this, AMazeActivity.class);
                music.stop();
                startActivity(menu);
                finish();

            }
        });

        map = findViewById(R.id.toggleMap);
        map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //Toast.makeText(PlayAnimationActivity.this, "Map toggle ON", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
                    Log.v(tag, "Map toggle ON");
                } else {
                    // The toggle is disabled
                    //Toast.makeText(PlayAnimationActivity.this, "Map toggle OFF", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleFullMap, 0);
                    Log.v(tag, "Map toggle OFF");
                }
            }
        });

        path = findViewById(R.id.togglePath);
        path.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //Toast.makeText(PlayAnimationActivity.this, "Path toggle ON", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleSolution, 0 );
                    Log.v(tag, "Path toggle ON");
                } else {
                    // The toggle is disabled
                    //Toast.makeText(PlayAnimationActivity.this, "Path toggle OFF", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleSolution, 0 );
                    Log.v(tag, "Path toggle OFF");
                }
            }
        });

        wall = findViewById(R.id.toggleWalls);
        wall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    //Toast.makeText(PlayAnimationActivity.this, "Wall toggle ON", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0 );
                    Log.v(tag, "Wall toggle ON");
                } else {
                    // The toggle is disabled
                    //Toast.makeText(PlayAnimationActivity.this, "Wall toggle OFF", Toast.LENGTH_SHORT).show();
                    currentState.keyDown(Constants.UserInput.ToggleLocalMap, 0 );
                    Log.v(tag, "Wall toggle OFF");
                }
            }
        });

        mapM = findViewById(R.id.buttonMapMinus);
        mapM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PlayAnimationActivity.this, "Clicked MAP MINUS", Toast.LENGTH_SHORT).show();
                currentState.keyDown(Constants.UserInput.ZoomOut, 0 );
                Log.v(tag, "Clicked MAP MINUS");
            }
        });

        mapP = findViewById(R.id.buttonMapPlus);
        mapP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PlayAnimationActivity.this, "Clicked MAP PLUS", Toast.LENGTH_SHORT).show();
                currentState.keyDown(Constants.UserInput.ZoomIn, 0 );
                Log.v(tag, "Clicked MAP PLUS");
            }
        });

        start = findViewById(R.id.toggleDriver);
        start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Runnable driving = new Runnable() {
                    @Override
                    public void run() {
                        if(drive){
                            try {
                                exit = driver.drive2ExitAnimation();
                                // update the progress bar
                                CardinalDirection direc = currentState.getCurrentDirection();

                                direction.setText(String.valueOf(direc));
                                Log.v(tag, String.valueOf(direc));
                                bar.setProgress((int) robot.getBatteryLevel());
                                if(exit){
                                    Intent winning = new Intent(PlayAnimationActivity.this, WinningActivity.class);
                                    winning.putExtra("path_length", String.valueOf(robot.getOdometerReading()));
                                    winning.putExtra("battery", String.valueOf(driver.getEnergyConsumption()));
                                    Log.v(tag, String.valueOf(robot.getOdometerReading()));
                                    Log.v(tag, String.valueOf(driver.getEnergyConsumption()));
                                    music.stop();
                                    startActivity(winning);
                                    drive = false;
                                    finish();

                                }
                                if(robot.hasStopped() || robot.getBatteryLevel() < 5){
                                    Intent losing = new Intent(PlayAnimationActivity.this, LosingActivity.class);
                                    losing.putExtra("path_length", String.valueOf(robot.getOdometerReading()));
                                    losing.putExtra("battery", String.valueOf(driver.getEnergyConsumption()));
                                    music.stop();
                                    startActivity(losing);
                                    drive = false;
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            notice.postDelayed(this, 50);
                        }

                    }
                };

                if (isChecked) {
                    // The toggle is enabled
                    //Toast.makeText(PlayAnimationActivity.this, "DRIVER ON", Toast.LENGTH_SHORT).show();
                    drive = true;
                    notice.post(driving);
                    Log.v(tag, "Driver GOING");
                } else {
                    // The toggle is disabled
                    //Toast.makeText(PlayAnimationActivity.this, "DRIVER OFF", Toast.LENGTH_SHORT).show();
                    drive = false;
                    Log.v(tag, "Driver STOP");
                }
            }
        });
    }


}
