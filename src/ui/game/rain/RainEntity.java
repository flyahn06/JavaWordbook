package ui.game.rain;

import main.Word;

import javax.swing.*;
import java.awt.*;

public class RainEntity {
    public JLabel label;
    public String answer;
    public boolean destroyed = false;

    public RainEntity(JLabel label, String answer) {
        this.label = label;
        this.answer = answer;

        this.label.setOpaque(true);
        this.label.setBackground(Color.WHITE);
    }

    public boolean isRight(String userAnswer) {
        return userAnswer.trim().equals(this.answer);
    }
}
