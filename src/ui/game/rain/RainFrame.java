package ui.game.rain;

import main.VocManager;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RainFrame extends JFrame {
    private final String difficulty;
    private VocManager vm;
    private int maxWord;
    private int speed;
    private double speedMultiplicator;
    private Vector<RainEntity> rainEntities;

    Container frame = this.getContentPane();
    Container gamePanel;
    JTextField wordInput;

    static class Difficulty {
        public static final String EASY = "쉬움";
        public static final String MEDIUM = "중간";
        public static final String HARD = "어려움";
    }

    public RainFrame(String difficulty, VocManager vm) {
        this.difficulty = difficulty;
        this.vm = vm;

        switch (difficulty) {
            case Difficulty.EASY -> {
                this.setMaxWord(5);
                this.setSpeedMultiplicator(0.7);
            }
            case Difficulty.MEDIUM -> {
                this.setMaxWord(10);
                this.setSpeedMultiplicator(1);
            }
            case Difficulty.HARD -> {
                this.setMaxWord(15);
                this.setSpeedMultiplicator(1.3);
            }
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

        initMenu();

        this.frame.add(this.gamePanel, BorderLayout.CENTER);
        this.frame.add(this.wordInput, BorderLayout.SOUTH);
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
        JMenuItem easyMenu = new JMenuItem("난이도 하");
        JMenuItem mediumMenu = new JMenuItem("난이도 중");
        JMenuItem hardMenu = new JMenuItem("난이도 상");
        gameMenu.add(startMenu);
        gameMenu.add(restartMenu);
        gameMenu.add(exitMenu);
        mb.add(gameMenu);



        this.setJMenuBar(mb);
    }

    private void checkAnswer() {

    }

    private void run() {

    }

    public String getDifficulty() {
        return this.difficulty;
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
        new RainFrame(Difficulty.EASY, manager);
    }
}
