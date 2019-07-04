package edu.wm.cs.cs301.danielquiroga.generation;

import android.os.Handler;
import android.util.Log;

import edu.wm.cs.cs301.danielquiroga.UI.GeneratingActivity;



public class MazeOrder implements Order {


    private static Integer skillLevel;
    private static Builder builder;
    private Integer percentdone = 0;
    static MazeConfiguration config;
    String tag = "Maze Order";
    Handler handler;

    public MazeOrder(){
        builder = Order.Builder.DFS;
        skillLevel = 0;
    }

    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    public static void setSkillLevel(int level){
        skillLevel = level;
    }

    @Override
    public Builder getBuilder() {
        return builder;
    }

    public static void setBuilder(String build){

        switch(build){

            case "DFS": builder = Order.Builder.DFS;
            case "Eller": builder = Order.Builder.Eller;
            case "Prim": builder = Order.Builder.Prim;

        }
    }

    @Override
    public boolean isPerfect() {
        return false;
    }

    /**
     * The deliver method is the call back method for the background
     * thread operated in the maze factory to deliver the ordered
     * product, here the generated maze in its container,
     * the MazeConfiguration object.
     */
    @Override
    public void deliver(MazeConfiguration mazeConfig) {
        // WARNING: DO NOT REMOVE, USED FOR GRADING PROJECT ASSIGNMENT
        if (Cells.deepdebugWall)
        {   // for debugging: dump the sequence of all deleted walls to a log file
            // This reveals how the maze was generated
            mazeConfig.getMazecells().saveLogFile(Cells.deepedebugWallFileName);
        }// TODO send the maze Configuration to playing states
        Log.v(tag, "creates Maze Configuration");
        GeneratingActivity.setConfig(mazeConfig);
    }

    /**
     * Allows external increase to percentage in generating mode.
     * Internal value is only updated if it exceeds the last value and is less or equal 100
     * @param percentage gives the new percentage on a range [0,100]
     * @return true if percentage was updated, false otherwise
     */
    @Override
    public void updateProgress(int percentage) {
        if (percentdone < percentage && percentage <= 100) {
            percentdone = percentage;

        }
    }

    public int getPercentDone(){
        return percentdone;
    }

    public MazeConfiguration getMazeConfig(){
        return config;
    }
}
