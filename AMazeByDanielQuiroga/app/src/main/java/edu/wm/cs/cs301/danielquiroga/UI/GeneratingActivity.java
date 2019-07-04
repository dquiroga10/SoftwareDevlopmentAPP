package edu.wm.cs.cs301.danielquiroga.UI;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


import org.w3c.dom.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.wm.cs.cs301.danielquiroga.generation.Cells;
import edu.wm.cs.cs301.danielquiroga.generation.MazeConfiguration;
import edu.wm.cs.cs301.danielquiroga.generation.Order;
import edu.wm.cs.cs301.danielquiroga.gui.Constants;
import edu.wm.cs.cs301.danielquiroga.gui.MazeFileReader;
import edu.wm.cs.cs301.danielquiroga.gui.MazeFileWriter;
import edu.wm.cs.daniel.daniel.R;


import edu.wm.cs.cs301.danielquiroga.generation.MazeFactory;
import edu.wm.cs.cs301.danielquiroga.generation.MazeOrder;






public class GeneratingActivity extends AppCompatActivity implements Order{

    private String maze0 = "maze0";
    private Boolean maze0F = false;
    private String maze1 = "maze1";
    private Boolean maze1F = false;
    private String maze2 = "maze2";
    private Boolean maze2F = false;
    private String maze3 = "maze3";
    private Boolean maze3F = false;
    //private String maze4 = "maze4";
    private ProgressBar bar;
    protected Handler handler;
    private MazeFactory fac = new MazeFactory();
    private Integer skillLevel;
    private String build;
    private String driver;
    //private Integer percentdone = 0;
    public static MazeConfiguration config;
    private MazeOrder order;
    private String tag = "GeneratingActivity";
    private static String tags = "GeneratingActivity";
    private Order.Builder builder = Order.Builder.DFS;
    MediaPlayer music;
    Button menu;
    Boolean change;


    /**
     * the onCreate method is called whenever this activity is
     * displayed. Inside the method I call on all the added features
     * I have added to the screen such as buttons, spinners, and bars.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        music = MediaPlayer.create(this, R.raw.loading);
        music.setLooping(true);
        music.start();
        Intent info = getIntent();
        String button = info.getStringExtra("button");
        change = true;
        String size_str = info.getStringExtra("size");
        skillLevel = Integer.valueOf(size_str);
        build = info.getStringExtra("builder");
        switch(build){

            case "DFS": builder = Order.Builder.DFS;
            case "Eller": builder = Order.Builder.Eller;
            case "Prim": builder = Order.Builder.Prim;

        }
        driver = info.getStringExtra("driver");
        //Toast.makeText(this, "Size passed: " + size ,Toast.LENGTH_SHORT ).show();
        //order = new MazeOrder();
        //order.setBuilder(build);
        //order.setSkillLevel(skillLevel);

        menu = findViewById(R.id.gen_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(GeneratingActivity.this, AMazeActivity.class);
                music.stop();
                startActivity(menu);
                change = false;
                finish();
            }
        });

        bar = findViewById(R.id.gen_bar);
        handler = new Handler();
        if(button.equals("explore") || skillLevel >= 4) {
            fac.order(this);
        }
        else if(button.equals("revisit")){

            String maze = null;

            if(getSkillLevel() == 0){
                maze = maze0;
            }
            if (getSkillLevel() == 1){
                maze = maze1;
            }
            if(getSkillLevel() == 2){
                maze = maze2;
            }
            if(getSkillLevel() == 3){
                maze = maze3;
            }
            /*if(getSkillLevel() == 0 && maze0F == true){
                maze = maze0;
            }
            else if(getSkillLevel() == 0 && maze0F == false){
                fac.order(this);
            }

            if(getSkillLevel() == 1 && maze1F){
                maze = maze0;
            }
            else if(getSkillLevel() == 1 && !maze1F){
                fac.order(this);
            }

            if(getSkillLevel() == 2 && maze2F){
                maze = maze0;
            }
            else if(getSkillLevel() == 2 && !maze2F){
                fac.order(this);
            }

            if(getSkillLevel() == 3 && maze3F){
                maze = maze0;
            }
            else if(getSkillLevel() == 3 && !maze3F){
                fac.order(this);
            }*/

            try {
                FileInputStream file = openFileInput(maze);
                deliver(loadMazeConfigurationFromFile(file));
            } catch (FileNotFoundException e) {
                fac.order(this);
                //e.printStackTrace();
            }

            //deliver(loadMazeConfigurationFromFile(file));

        }
    }

    private MazeConfiguration loadMazeConfigurationFromFile(FileInputStream file){
        MazeFileReader read = new MazeFileReader(file);//TODO make it load the file is copied
        //handler = new Handler();
        return read.getMazeConfiguration();
    }

    public static void setConfig(MazeConfiguration configur){
        if(configur != null) {
            Log.v(tags, "theres a configuration occurring");
            config = configur;
        }
    }

    /**
     * method is called once the loading has finished
     * uses information passed on by the title screen
     * to switch to proper play screen
     */
    private void switchtoPlaying(){
        Intent manual = new Intent(this, PlayManuallyActivity.class);
        Intent auto = new Intent(this, PlayAnimationActivity.class);
        //config = mazeConfig;
        Log.v(tag, driver);
        if(change == true ) {
            if (driver.equals("Manual")) {
                music.stop();
                startActivity(manual);
                finish();
            } else {
                auto.putExtra("driver", driver);
                music.stop();
                startActivity(auto);
                finish();
            }
        }
    }



    public static MazeConfiguration getMazeConfig(){
        return config;
    }


    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    @Override
    public Builder getBuilder() {
        return builder;
    }

    @Override
    public boolean isPerfect() {
        return false;
    }

    @Override
    public void deliver(MazeConfiguration mazeConfig) {

        setConfig(mazeConfig);

        if(getSkillLevel() == 0){
            // copy the maze to file here
            File maze00 = new File(getFilesDir(), maze0);
            maze0F = true;
            MazeFileWriter.store(maze00, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[0], Constants.SKILL_PARTCT[0],
                   config.getRootnode(), config.getMazecells(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        else if(getSkillLevel() == 1){
            // copy the maze to the file here
            File maze01 = new File(getFilesDir(), maze1);
            maze1F = true;
            MazeFileWriter.store(maze01, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[1], Constants.SKILL_PARTCT[1],
                   config.getRootnode(), config.getMazecells(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        else if(getSkillLevel() == 2){
            // copy the maze to the file here
            File maze02 = new File(getFilesDir(), maze2);
            maze2F = true;
            MazeFileWriter.store(maze02, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[2], Constants.SKILL_PARTCT[2],
                    config.getRootnode(), config.getMazecells(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        else if(getSkillLevel() == 3){
            // copy the maze to the file here
            File maze03 = new File(getFilesDir(), maze3);
            maze3F = true;
            MazeFileWriter.store(maze03, config.getWidth(), config.getHeight(), Constants.SKILL_ROOMS[3], Constants.SKILL_PARTCT[3],
                    config.getRootnode(), config.getMazecells(), config.getMazedists().getAllDistanceValues(), config.getStartingPosition()[0], config.getStartingPosition()[1]);
        }
        switchtoPlaying();
    }

    @Override
    public void updateProgress(final int percentage) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                Log.v(tag, "loading " + percentage);
                bar.setProgress(percentage);

            }
        });

    }
}

