package ui.main.quiz;

import main.VocManager;
import main.Word;
import problems.ProblemManager;
import problems.SubjectiveProblem;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Random;
import java.util.Vector;

public class QuizTop10Dialog extends JDialog {
    private VocManager vm;
    private ProblemManager pm;
    private int currentProblemIndex = 0;

    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitBtn;

    static Random rand = new Random();

    public QuizTop10Dialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame, "오답률 Top10 퀴즈", true);
        this.vm = vm;
        this.pm = new ProblemManager(vm);

        setSize(500, 250);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout(10, 10));

        initQuiz();
        setVisible(true);
    }

    private void initQuiz() {
        pm.problemType = 2;
        Vector<Word> words = new Vector<>(vm.getVoc().values());

        if (words.size() < 10) pm.problemCount = words.size();
        else pm.problemCount = 10;

        words.sort(Comparator.comparingInt(Word::getRanking).reversed());

        Vector<String> top10 = new Vector<>();
        for (int i = 0; i < pm.problemCount; i++) top10.add(words.get(i).getEng());

        pm.problems = top10.toArray(new String[0]);

        showNextProblem();
    }

    private void showNextProblem() {
        if (currentProblemIndex >= pm.problemCount) {
            showResult();
            return;
        }

        String currentWord = pm.problems[currentProblemIndex];
        showSubjectiveProblem(currentWord, currentProblemIndex + 1);
    }

    private void showSubjectiveProblem(String word, int number) {
        getContentPane().removeAll();
        repaint();
        revalidate();

        SubjectiveProblem sp;
        if (rand.nextInt(2) == 0) sp = new SubjectiveProblem(number, word, vm, 1); // 영→한
        else sp = new SubjectiveProblem(number, word, vm, 2); // 한→영

        String questionText = sp.subjectiveType == 1
                ? number + ". '" + word + "' 의 뜻을 입력하세요."
                : number + ". '" + vm.getVoc().get(word).getKor() + "' 의 영단어를 입력하세요.";

        questionLabel = new JLabel(questionText);
        questionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(questionLabel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        answerField = new JTextField(15);
        submitBtn = new JButton("제출");
        inputPanel.add(answerField);
        inputPanel.add(submitBtn);
        add(inputPanel, BorderLayout.SOUTH);

        submitBtn.addActionListener(e -> {
            String userAnswer = answerField.getText().trim();
            boolean correct = sp.isCorrect(userAnswer);

            if (correct) {
                JOptionPane.showMessageDialog(this, "정답입니다!");
                pm.rightCount++;
            } else {
                String correctAnswer = sp.subjectiveType == 1 ? vm.getVoc().get(word).getKor() : word;
                JOptionPane.showMessageDialog(this, "오답입니다. 정답: " + correctAnswer);
                pm.wrongCount++;
                vm.rank(word);
            }

            currentProblemIndex++;
            showNextProblem();
        });

        revalidate();
        repaint();
    }

    private void showResult() {
        String result = "Top10 퀴즈 종료!\n정답: " + pm.rightCount + "\n오답: " + pm.wrongCount;
        JOptionPane.showMessageDialog(this, result);
        dispose();
    }
}
