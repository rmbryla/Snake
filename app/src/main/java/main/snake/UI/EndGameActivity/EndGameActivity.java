package main.snake.UI.EndGameActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import main.snake.R;
import main.snake.SessionManager;
import main.snake.UI.GameActivity.GameActivity;
import main.snake.UI.StartScreen.StartScreenActivity;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_end_game);

        ArrayList<Integer> scores = SessionManager.getScoreBoard().getScores();
        if (scores.size() >= 1) {
            String text1 = "1st : " + Integer.toString(scores.get(scores.size() - 1));
            TextView score1 =  findViewById(R.id.end_game_high_score_1);
            score1.setText(text1);
            score1.setVisibility(View.VISIBLE);

        }
        if (scores.size() >= 2) {
            String text2 = "2nd : " + Integer.toString(scores.get(scores.size() - 2));
            TextView score2 =  findViewById(R.id.end_game_high_score_2);
            score2.setText(text2);
            score2.setVisibility(View.VISIBLE);
        }
        if (scores.size() >= 3) {
            String text3 = "3rd : " + Integer.toString(scores.get(scores.size() - 3));
            TextView score3 =  findViewById(R.id.end_game_high_score_3);
            score3.setText(text3);
            score3.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.end_game_play_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.end_game_main_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StartScreenActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StartScreenActivity.class);
        startActivity(intent);
    }
}
