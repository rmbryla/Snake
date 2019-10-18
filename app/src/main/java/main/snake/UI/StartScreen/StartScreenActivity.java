package main.snake.UI.StartScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import main.snake.Model.ScoreBoard;
import main.snake.R;
import main.snake.SessionManager;
import main.snake.UI.GameActivity.GameActivity;

public class StartScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager.loadScoreBoard();

        setContentView(R.layout.activity_start_screen);
        TextView playButton = findViewById(R.id.start_screen_play_button);
        final AppCompatActivity self = this;
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
