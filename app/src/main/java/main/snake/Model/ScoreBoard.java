package main.snake.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ScoreBoard implements Serializable {
    private ArrayList<Integer> scores;

    private int maxScores = 5;

    public ScoreBoard() {
        scores = new ArrayList<>();
        for (int i = 0; i < maxScores; i++) {
            scores.add(0);
        }
    }

    public void save() {
        try {
            FileOutputStream f = new FileOutputStream(new File("scoreBoard.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(this);
            f.close();
            o.close();
        } catch (IOException e) {

        }
    }

    public void addScore(int score) {
        Collections.sort(scores);
        if (scores.size() < maxScores) scores.add(score);
        else if (score > scores.get(0)) {
            scores.remove(0);
            scores.add(score);
        }
        save();
    }

    public ArrayList<Integer> getScores() {
        Collections.sort(scores);
        return new ArrayList<>(scores);
    }
}