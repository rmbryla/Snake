package main.snake;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import main.snake.Model.ScoreBoard;
import main.snake.UI.Game;

public class SessionManager {
    private static Game game;
    private static ScoreBoard scoreBoard;
    private static boolean initialized = false;


    public static void setGame(Game game) {
        SessionManager.game = game;
    }

    public static Game getGame() {
        return game;
    }

    public static ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public static void loadScoreBoard() {
        if (initialized) return;
        try {
            FileInputStream f = new FileInputStream(new File("scoreBoard.txt"));
            ObjectInputStream o = new ObjectInputStream(f);

            scoreBoard = (ScoreBoard) o.readObject();
            f.close();
            o.close();
        } catch (IOException e) {
            scoreBoard = new ScoreBoard();
        }
        catch (ClassNotFoundException e) {
        }
        initialized = true;
    }

    public static void addScore(int score) {
        scoreBoard.addScore(score);
    }
}
