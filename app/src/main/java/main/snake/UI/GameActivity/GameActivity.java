package main.snake.UI.GameActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import main.snake.Model.Snake;
import main.snake.R;
import main.snake.SessionManager;
import main.snake.UI.EndGameActivity.EndGameActivity;
import main.snake.UI.Game;

public class GameActivity extends AppCompatActivity {
    Game game;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        game = new Game(getApplicationContext(), size);
        SessionManager.setGame(game);
        frameLayout = findViewById(R.id.game_layout_activity);
        frameLayout.addView(game);
        findViewById(R.id.game_pause_button).bringToFront();
        findViewById(R.id.game_pause_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.isPlaying()) game.pause();
                else game.resume();
            }
        });
    }

    public void endGame() {
        game.pause();
        Intent intent = new Intent(this, EndGameActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        game.pause();
        Intent intent = new Intent(this, EndGameActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        game.resume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        game.pause();
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

}