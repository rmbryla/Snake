package main.snake.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;

import main.snake.Model.Apple;
import main.snake.Model.Callback;
import main.snake.Model.Snake;
import main.snake.R;
import main.snake.UI.EndGameActivity.EndGameActivity;

public class Game extends SurfaceView implements GestureDetector.OnGestureListener, Runnable {
    private boolean isPlaying;
    private Thread thread = null;
    private Callback done;

    private GestureDetector gestureDetector;

    private Paint paint = new Paint();

    private final int NUM_BOXES_WIDE = 25;
    private int NUM_BOXES_TALL;
    private int boxWidth;
    private int widthMargin;
    private int topMargin;
    private int bottomMargin;
    private final int scoreBoardHeight = 250;

    private Snake snake;
    private Apple apple;
    private int screenWidth;
    private int screenHeight;


    public Game(Context context, Point size, Callback done) {
        super(context);

        this.done = done;

        this.screenHeight = size.y;
        this.screenWidth = size.x;
        this.widthMargin = (int) ((double) this.screenWidth *.025);
        this.widthMargin += getMarginOffset(screenWidth, widthMargin)/2;
        this.boxWidth = (size.x-2* widthMargin)/NUM_BOXES_WIDE;

        this.bottomMargin = boxWidth;
        this.topMargin = calcHeightMargin();
        this.NUM_BOXES_TALL = calcNumBoxesTall();

        this.isPlaying = true;

        this.snake = new Snake(NUM_BOXES_WIDE, NUM_BOXES_TALL);
        this.apple = new Apple(NUM_BOXES_WIDE, NUM_BOXES_TALL);

        this.gestureDetector = new GestureDetector(this);
        setWillNotDraw(false);
    }

    public Snake getSnake() {
        return snake;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private int calcNumBoxesTall() {
        int adjustedHeight = screenHeight-scoreBoardHeight-2*topMargin-bottomMargin;
        double exactNumBoxes = adjustedHeight/boxWidth;
        return (int) exactNumBoxes;
    }

    private int calcHeightMargin() {
        int adjustedHeight = screenHeight-scoreBoardHeight-bottomMargin;
        int maxBoxesTall = adjustedHeight/boxWidth;
        double exactNumBoxes = (double)adjustedHeight/(double)boxWidth;
        double error = (exactNumBoxes-maxBoxesTall)*boxWidth;
        double margin = 0;//screenHeight-maxBoxesTall*boxWidth-scoreBoardHeight;
        margin += boxWidth/2 + error/2;
        return (int) Math.floor(margin);
    }

    private int getMarginOffset(int width, int margin) {
        int currSize = width-margin*2;
        double exactBoxWidth = (double) currSize/NUM_BOXES_WIDE;
        double widthError = exactBoxWidth - Math.floor(exactBoxWidth);
        int totalError = (int) (widthError*NUM_BOXES_WIDE);
        return totalError;
    }

    public void setCurrentDirection(Snake.Direction dir) {
        snake.setCurrentDirection(dir);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        event.getOrientation();
        gestureDetector.onTouchEvent(event);
        return true;
    }



    private void update() {

        if (snake.updateLocation(apple)) this.apple.regen();
        if (snake.isDead()) {
            this.gameOver();
        }

    }

    private void gameOver(){
        done.onDone(snake.isDead());
        //pause();
//        Intent intent = new Intent(mContext, EndGameActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawGame() {
        if (!getHolder().getSurface().isValid()) return;
        Canvas canvas = getHolder().lockCanvas();
        canvas.drawColor(Color.BLACK);

        // DRAW BORDERS
        paint.setColor(Color.DKGRAY);
        makeBackground(canvas);

        snake.draw(canvas, boxWidth, widthMargin, topMargin*2+scoreBoardHeight);
        apple.draw(canvas, boxWidth, widthMargin, topMargin*2+scoreBoardHeight);

        drawScore(canvas);
        getHolder().unlockCanvasAndPost(canvas);
    }

    private void drawScore(Canvas canvas) {
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.subwt_);
        paint.setColor(Color.LTGRAY);
        paint.setTypeface(font);
        paint.setTextSize(150);
        canvas.drawText("Score", widthMargin+50, topMargin+200,paint);

        int shiftScore = snake.getSize()-5<10 ? 150 : (snake.getSize()-5<100 ? 250 : 350)+widthMargin;
        canvas.drawText(Integer.toString(snake.getSize()-5),screenWidth-shiftScore, topMargin+200, paint);
    }

    private void makeBackground(Canvas canvas) {
        int topOfBottomRect = screenHeight-bottomMargin; //heightMargin*2+scoreBoardHeight+boxWidth*NUM_BOXES_TALL;

        canvas.drawRect(0, 0, screenWidth, topMargin, paint); // top
        canvas.drawRect(0, topMargin+scoreBoardHeight, screenWidth, topMargin*2 + scoreBoardHeight, paint); // top

        canvas.drawRect(0, 0, widthMargin, screenHeight, paint); // left
        canvas.drawRect(screenWidth- widthMargin, 0, screenWidth, screenHeight, paint); // right
        canvas.drawRect(0, topOfBottomRect, screenWidth, screenHeight, paint); // bottom
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.setName("Game Thread");
        thread.start();
    }



    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x1 = e1.getX();
        float x2 = e2.getX();
        float y1 = e1.getY();
        float y2 = e2.getY();

        snake.setCurrentDirection(getDirection(x1, x2, y1, y2));
        return true;
    }
    public static Snake.Direction getDirection(float x1, float x2, float y1, float y2) {
        double angle = getAngle(x1, x2, y1, y2);
        return fromAngle(angle);
    }
    public static double getAngle(float x1, float x2, float y1, float y2) {
        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }
    public static Snake.Direction fromAngle(double angle) {
        if(inRange(angle, 45, 135)){
            return Snake.Direction.UP;
        }
        else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
            return Snake.Direction.RIGHT;
        }
        else if(inRange(angle, 225, 315)){
            return Snake.Direction.DOWN;
        }
        else{
            return Snake.Direction.LEFT;
        }
    }
    public static boolean inRange(double val, int min, int max) {
        return (val > min && val <=max);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            drawGame();
            try {
                thread.sleep(175);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


