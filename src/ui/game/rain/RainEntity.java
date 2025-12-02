package ui.game.rain;

import main.Word;

import javax.swing.*;

public class RainEntity {
    public JLabel label;
    public String answer;

    public RainEntity(JLabel label, String answer) {
        this.label = label;
        this.answer = answer;
    }

    public boolean isRight(String userAnswer) {
        return userAnswer.trim().equals(this.answer);
    }
}
