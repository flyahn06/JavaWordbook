package ui.main;

import main.Word;

import javax.swing.*;
import java.awt.*;

public class CardEntity {
    private Word word;
    private boolean starred;
    public JPanel entity;
    public JButton starButton;
    private JLabel engLabel;
    private JLabel korLabel;

    public CardEntity(Word word) {
        this.entity = new JPanel(new BorderLayout());
        this.engLabel = new JLabel("영단어");
        this.korLabel = new JLabel("한글 뜻");
        this.starred = word.isStarred();
        this.updateCard(word);
        this.star();

        this.initLayout();
    }

    private void initLayout() {
        JPanel engkorPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        engkorPanel.add(this.engLabel);
        engkorPanel.add(this.korLabel);

        this.starButton = new JButton("별");
        this.entity.add(this.starButton, BorderLayout.NORTH);
        this.entity.add(engkorPanel, BorderLayout.CENTER);
    }

    public void updateCard(Word with) {
        this.word = with;
        this.engLabel.setText(this.word.getEng());
        this.korLabel.setText(this.word.getKor());
    }

    public void star() {
        if (this.starred) {

        } else {

        }

        this.starred = !this.starred;
        this.word.setStarred(this.starred);
    }
}
