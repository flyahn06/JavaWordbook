package ui.game.rain;

import main.VocManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class RainFrame extends JFrame {
    private int difficulty = -1;
    private VocManager vm;
    private int maxWord;
    private int speed;
    private int remainingHearts;
    private double speedMultiplicator;
    private Vector<RainEntity> rainEntities;
    private JMenuItem[] difficultyMenus;
    private JLabel[] hearts;
    private JPanel northPanel;
    private JLabel scoreLabel;

    Container frame = this.getContentPane();
    Container gamePanel;
    JTextField wordInput;

    static class Difficulty {
        private static final String[] difficultyInKorean = {
                "쉬움",
                "중간",
                "어려움"
        };
        public static final int EASY = 0;
        public static final int MEDIUM = 1;
        public static final int HARD = 2;

        public static String translateDifficulty(int difficulty) {
            if (difficulty > HARD || difficulty < 0)
                return "선택 안 됨";

            return difficultyInKorean[difficulty];
        }
    }

    private class DifficultyMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int difficulty = Integer.parseInt(e.getActionCommand());
            changeDifficulty(difficulty);
        }
    }

    public RainFrame(VocManager vm) {
        this.vm = vm;

        this.hearts = new JLabel[5];

        for (int i = 0; i < this.hearts.length; i++) {
            this.hearts[i] = new JLabel(new ImageIcon("img/heart.png"));
        }

        this.setTitle(vm.getFileName() + " 산성비 (난이도: " + this.getDifficulty() + ")");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.initLayout();
        this.setResizable(false);
        this.setVisible(true);
    }

    private void initLayout() {
        wordInput = new JTextField();
        gamePanel = new JPanel();
        gamePanel.setLayout(null);

        initNorthPanel();
        initMenu();

        this.frame.add(this.gamePanel, BorderLayout.CENTER);
        this.frame.add(this.wordInput, BorderLayout.SOUTH);
    }

    private void initNorthPanel() {
        this.northPanel = new JPanel();

        this.scoreLabel = new JLabel("점수: 0");
        this.northPanel.add(this.scoreLabel);
        this.northPanel.add(new JLabel("    |     "));

        for (JLabel heart: this.hearts) {
            heart.setVisible(false);
            this.northPanel.add(heart);
        }
        this.northPanel.setBackground(Color.ORANGE);

        this.frame.add(this.northPanel, BorderLayout.NORTH);
    }

    private void initMenu() {
        JMenuBar mb = new JMenuBar();

        JMenu gameMenu = new JMenu("게임");
        JMenuItem startMenu = new JMenuItem("게임 시작");
        JMenuItem restartMenu = new JMenuItem("다시 하기");
        JMenuItem exitMenu = new JMenuItem("게임 끝내기");
        gameMenu.add(startMenu);
        gameMenu.add(restartMenu);
        gameMenu.add(exitMenu);
        mb.add(gameMenu);

        JMenu difficultyMenu = new JMenu("난이도");
        DifficultyMenuListener listener = new DifficultyMenuListener();
        this.difficultyMenus = new JMenuItem[3];
        this.difficultyMenus[0] = new JMenuItem("난이도 " + Difficulty.translateDifficulty(0));
        this.difficultyMenus[1] = new JMenuItem("난이도 " + Difficulty.translateDifficulty(1));
        this.difficultyMenus[2] = new JMenuItem("난이도 " + Difficulty.translateDifficulty(2));

        for (int i = 0; i < 3; i++) {
            this.difficultyMenus[i].addActionListener(listener);
            this.difficultyMenus[i].setActionCommand(Integer.toString(i));
            difficultyMenu.add(this.difficultyMenus[i]);
        }

        mb.add(difficultyMenu);
        this.setJMenuBar(mb);
    }

    private void checkAnswer() {

    }

    private void run() {

    }

    private void enableDifficultyMenu() {
        for (JMenuItem item: this.difficultyMenus)
            item.setEnabled(true);
        this.difficultyMenus[this.difficulty].setEnabled(false);
    }

    private void changeDifficulty(int to) {
        if (this.difficulty != -1) {
            this.difficultyMenus[this.difficulty].setEnabled(true);
            this.difficultyMenus[this.difficulty].setText("난이도 " + Difficulty.translateDifficulty(this.difficulty));
        }

        this.difficultyMenus[to].setEnabled(false);
        this.difficultyMenus[to].setText(this.difficultyMenus[to].getText() + " ✔");
        this.difficulty = to;

        switch (difficulty) {
            case Difficulty.EASY -> {
                this.setMaxWord(5);
                this.setSpeedMultiplicator(0.7);
                this.setRemainingHearts(5);
            }
            case Difficulty.MEDIUM -> {
                this.setMaxWord(10);
                this.setSpeedMultiplicator(1);
                this.setRemainingHearts(4);
            }
            case Difficulty.HARD -> {
                this.setMaxWord(15);
                this.setSpeedMultiplicator(1.3);
                this.setRemainingHearts(3);
            }
        }

        this.setTitle(vm.getFileName() + " 산성비 (난이도: " + this.getDifficulty() + ")");
    }

    private void disableDifficultyMenu() {
        for (JMenuItem item: this.difficultyMenus) {
            item.setEnabled(false);
        }
    }

    private void updateHeartStatus() {
        for (int i = 0; i < 5; i++) {
            this.hearts[i].setVisible(i < this.remainingHearts);
        }
    }

    public int getRemainingHearts() {
        return remainingHearts;
    }

    public void setRemainingHearts(int remainingHearts) {
        this.remainingHearts = remainingHearts;
        this.updateHeartStatus();
    }

    public String getDifficulty() {
        return Difficulty.translateDifficulty(this.difficulty);
    }

    public int getMaxWord() {
        return maxWord;
    }

    public void setMaxWord(int maxWord) {
        this.maxWord = maxWord;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getSpeedMultiplicator() {
        return speedMultiplicator;
    }

    public void setSpeedMultiplicator(double speedMultiplicator) {
        this.speedMultiplicator = speedMultiplicator;
    }

    public static void main(String[] args) {
        VocManager manager = new VocManager("홍길동");
        manager.makeVoc("res/words.txt");
        new RainFrame(manager);
    }
}
