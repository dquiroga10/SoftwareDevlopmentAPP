package edu.wm.cs.cs301.danielquiroga.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import edu.wm.cs.daniel.daniel.R;

public class MazePanel extends View {

    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private Drawable backgroundBottom;
    private Drawable backgroundTop;


    /**
     * Constructor that creates and initializes the bitmap, canvas and paint
     * @param context
     */
    public MazePanel(Context context) {
        super(context);
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888); // might need to change, currently hardcoded to fit the screen
        canvas = new Canvas(bitmap);
        paint = new Paint();
        backgroundBottom = getResources().getDrawable(R.drawable.maze_background1);
        backgroundTop = getResources().getDrawable(R.drawable.maze_background2);


    }

    /**
     * Constructor that creates and initializes the bitmap, canvas and paint
     * @param context
     */
    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        backgroundBottom = getResources().getDrawable(R.drawable.maze_background1);
        backgroundTop = getResources().getDrawable(R.drawable.maze_background2);


    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
    }


    /**
     * Draws given canvas.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //canvas.drawPaint(paint);
        //test_draw(); // this is for testing ONLY
        canvas.drawBitmap(bitmap, 0, 0, paint); // makes it so that the canvas being draw upon is drawn on the screen
    }

    public void drawMario(int left, int top, int right, int bottom){
        backgroundBottom.setBounds(left, bottom/2, right, bottom);
        backgroundBottom.draw(canvas);
        backgroundTop.setBounds(left, top, right, bottom/2);
        backgroundTop.draw(canvas);

    }

    /**
     * used this method to test various draw to make sure it
     * appeared on my canvas in the program
     */
    private void test_draw(){
        setColor(255, 0, 0);
        fillRect(1000, 1000, 50, 50);

        setColor(0, 0 , 255);
        fillOval(500, 500, 25, 25);

        setColor(0, 255, 0);
        drawLine(1000, 1000, 1000, 0);

        setColor(0, 255, 255);
        drawLine(1300, 1300, 100, 100);
    }
    /**
     * Method to draw the buffer image on a graphics object that is
     * obtained from the superclass.
     * Warning: do not override getGraphics() or drawing might fail.
     */
    public void update() {
        //paint(getGraphics());
        invalidate();
    }

    /**
     * Draws the buffer image to the given graphics object.
     * This method is called when this panel should redraw itself.
     * The given graphics object is the one that actually shows
     * on the screen.
     */
    /*
    @Override
    public void paint(Graphics g) {
    */
        /*if (null == g) {
            System.out.println("MazePanel.paint: no graphics object, skipping drawImage operation");
        }
        else {
            g.drawImage(bufferImage,0,0,null);
        }*/
   //}


    /**
     * Obtains a graphics object that can be used for drawing.
     * This MazePanel object internally stores the graphics object
     * and will return the same graphics object over multiple method calls.
     * The graphics object acts like a notepad where all clients draw
     * on to store their contribution to the overall image that is to be
     * delivered later.
     * To make the drawing visible on screen, one needs to trigger
     * a call of the paint method, which happens
     * when calling the update method.
     * @return graphics object to draw on, null if impossible to obtain image
     */
    /*
    public Graphics createBufferGraphics() {
        // if necessary instantiate and store a graphics object for later use
        /*if (null == graphics) {
            if (null == bufferImage) {
                bufferImage = createImage(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT);
                if (null == bufferImage)
                {
                    System.out.println("Error: creation of buffered image failed, presumedly container not displayable");
                    return null; // still no buffer image, give up
                }
            }
            graphics = (Graphics2D) bufferImage.getGraphics();
            if (null == graphics) {
                System.out.println("Error: creation of graphics for buffered image failed, presumedly container not displayable");
            }
            else {
                // System.out.println("MazePanel: Using Rendering Hint");
                // For drawing in FirstPersonDrawer, setting rendering hint
                // became necessary when lines of polygons
                // that were not horizontal or vertical looked ragged
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            }
        }
        return graphics;*/
    //}


    /**
     * uses java graphics to fill the rectangle with the specified dimensions
     * and position in the maze
     * @param x position in the maze
     * @param y position in the maze
     * @param width how wide the rectangle is
     * @param height how high the rectangles is
     */
    public void fillRect(int x, int y, int width, int height) {
        //graphics.fillRect(x, y, width, height);
        paint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect(x, y , width + x, height + y);

        canvas.drawRect(rect, paint);
    }

    /**
     * sets the color of the graphics for when drawing in order to draw
     * with various colors
     * @param red how much red component in the color
     * @param green how much green component in the color
     * @param blue how much blue component in the color
     */
    public void setColor(int red, int green, int blue) {
        //graphics.setColor(new Color(red , green, blue));
        paint.setColor(Color.rgb(red, green, blue));
    }

    /**
     * uses java graphics to fill the polygon whose dimensions and points are
     * passed through as parameters
     * @param xPoints array of the x points
     * @param yPoints array of the y points
     * @param nPoints total number of points
     */
    public void fillPolygon(int[] xPoints, int[]yPoints, int nPoints) {
        // graphics.fillPolygon(xPoints, yPoints, nPoints);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);// gives the starting point
        for (int i = 1; i < nPoints; i++){ // iterates through the arrray of points
            path.lineTo(xPoints[i], yPoints[i]); // essentially connects all the points
        }
        path.lineTo(xPoints[0], yPoints[0]);// set it back to the original
        canvas.drawPath(path, paint);
    }

    /**
     * used to draw a line from the distance between the two points passed in
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
       // graphics.drawLine(x1, y1, x2, y2);
        paint.setStrokeWidth(8);
        canvas.drawLine(x1, y1, x2, y2, paint);


    }

    /**
     * fills and oval bounded by the parameters passed in
     * @param x position in the maze
     * @param y position in the maze
     * @param width how wide the oval is
     * @param height how high the oval is
     */
    public void fillOval(int x, int y, int width, int height) {
       // graphics.fillOval(x, y, width, height);
        paint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(x, y , width + x, height + y);
        canvas.drawOval(oval, paint);
    }


    /**
     * converts the three components of the color into a single integer
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return the integer representation of the color in sRGB
     */
    public static int getRGB(int r, int g, int b) {
        /*Color col = new Color(r, g, b);
        return col.getRGB();*/
        return Color.rgb(r, g, b);
    }

    /**
     * converts the sRGB representation of the color wanted into a an array
     * with 3 elements each for the reference of the componenets of the Color
     * class
     * @param col, the sRGB representation of the color
     * @return the array with the components of the color
     */

    public static int[] getColorComponents(int col) {
        int[] com = new int[3];
        /*Color color = new Color(col);
        com[0] = color.getRed();
        com[1] = color.getGreen();
        com[2] = color.getBlue();
        return com;*/

        com[0] = Color.red(col);//(int) color.getComponents()[0];
        com[1] = Color.green(col);//(int) color.getComponents()[1];
        com[2] = Color.blue(col);//(int) color.getComponents()[2];

        return com;
    }


}
