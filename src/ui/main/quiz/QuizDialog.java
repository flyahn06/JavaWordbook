package ui.main.quiz;

import main.VocManager;
import problems.ChoiceProblem;
import problems.ProblemManager;
import problems.SubjectiveProblem;
import ui.main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class QuizDialog extends JDialog {
    private VocManager vm;
    private ProblemManager pm;
    private int currentProblemIndex = 0; // 현재 진행 중인 문제 인덱스

    private JTextField answerField; // 주관식 입력 필드

    private int problemType; // 문제 유형: 1=객관식, 2=주관식, 3=혼합

    static Random rand = new Random();

    // 🔹 생성자
    public QuizDialog(MainFrame mainFrame, VocManager vm) {
        super(mainFrame, "퀴즈 풀기", true);
        this.vm = vm;
        this.pm = new ProblemManager(vm);

        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initProblemSettings(); // 문제 유형,수 설정 UI 초기화
        setVisible(true);
    }

    // 문제 설정 초기화 (유형, 수)
    private void initProblemSettings() {
        JPanel settingsPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel typeLabel = new JLabel("문제 유형:");
        String[] typeOptions = {"객관식", "주관식", "혼합"};
        JComboBox<String> typeBox = new JComboBox<>(typeOptions); // 문제 유형 선택

        JLabel countLabel = new JLabel("문제 수:");
        JTextField countField = new JTextField(); // 문제 수 입력

        JButton startBtn = new JButton("시작"); // 시작 버튼

        settingsPanel.add(typeLabel);
        settingsPanel.add(typeBox);
        settingsPanel.add(countLabel);
        settingsPanel.add(countField);
        settingsPanel.add(new JLabel());
        settingsPanel.add(startBtn);

        add(settingsPanel, BorderLayout.CENTER);
        startBtn.addActionListener(e -> {
            try {
                problemType = typeBox.getSelectedIndex() + 1;
                int count = Integer.parseInt(countField.getText().trim());
                if (count <= 0) throw new NumberFormatException();

                pm.problemType = problemType;
                pm.problemCount = count;

                //문제생성
                Vector<String> list = vm.getOrderedEnglish();
                Collections.shuffle(list);
                while(list.size() < pm.problemCount) {
                    Vector<String> engListTemp = new Vector<>(list);
                    Collections.shuffle(engListTemp);
                    list.addAll(engListTemp);
                }
                pm.problems = new String[pm.problemCount];
                for (int i=0; i<pm.problemCount; i++) {
                    pm.problems[i] = list.get(i);
                }

                getContentPane().removeAll(); // 기존 UI 제거
                setLocationRelativeTo(null);
                setSize(500, 400);
                showNextProblem();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "문제 수를 올바르게 입력해주세요.");
            }
        });
    }

    private void showNextProblem() {
        if (currentProblemIndex >= pm.problemCount) {
            showResult();
            return;
        }

        String currentWord = pm.problems[currentProblemIndex];

        switch (pm.problemType) {
            case 1 -> choiceProblemUI(currentWord,currentProblemIndex+1);
            case 2 -> subjectiveProblemUI(currentWord,currentProblemIndex+1);
            case 3 -> {
                if (rand.nextInt(2)+1 == 1) choiceProblemUI(currentWord,currentProblemIndex+1);
                else subjectiveProblemUI(currentWord,currentProblemIndex+1);
            }
        }
    }

    private void showResult() {
        String result = "퀴즈가 종료되었습니다.\n정답: "+pm.rightCount+"\n오답: "+pm.wrongCount;
        JOptionPane.showMessageDialog(this,result);

        int option = JOptionPane.showConfirmDialog(null, "상세 오답노트를 만드시겠습니까?", "오답노트",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            this.vm.WAnotes2(this.pm.wrongProblems);
        } else {
            this.vm.WAnotes(this.pm.wrongWords);
        }
        this.vm.writeCorrectRate(this.pm, VocManager.i - 1);
        VocManager.i++;
        dispose();
    }

    private void choiceProblemUI(String problem, int problemNumber) {
        StringBuilder temp = new StringBuilder();

        JLabel questionLabel;
        getContentPane().removeAll();   // 기존 화면 지우기
        setLayout(new BorderLayout(10,10));

        ChoiceProblem cp = new ChoiceProblem(problemNumber, problem, vm);

        boolean isEngToKor = (rand.nextInt(2) == 0);

        if (isEngToKor) {
            questionLabel = new JLabel(
                    problemNumber + ". '" + problem + "' 의 뜻을 고르시오.",
                    SwingConstants.CENTER
            );
            questionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
            add(questionLabel, BorderLayout.NORTH);
        } else {
            questionLabel = new JLabel(
                    problemNumber + ". '" + vm.getVoc().get(problem).getKor() + "' 의 영단어를 고르시오.",
                    SwingConstants.CENTER
            );
            questionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
            add(questionLabel, BorderLayout.NORTH);
        }

        temp.append(questionLabel.getText() + "\n");

        // 보기 버튼들
        JPanel choicePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        add(choicePanel, BorderLayout.CENTER);

        JButton[] btn = new JButton[4];

        for(int i = 0; i < 4; i++) {
            String text = isEngToKor
                    ? vm.getVoc().get(cp.getWordList().get(i)).getKor()
                    : cp.getWordList().get(i);

            btn[i] = new JButton(i+1 + ") " +text);
            temp.append(i+1 + ") " + text + "\n");
            choicePanel.add(btn[i]);

            int choiceNumber = i + 1;

            btn[i].addActionListener(e -> {
                if (choiceNumber == cp.getAnswerNumber()) {
                    pm.rightCount++;
                    JOptionPane.showMessageDialog(this, "정답입니다!");
                } else {
                    pm.wrongCount++;
                    JOptionPane.showMessageDialog(this,
                            "오답입니다!\n정답: " + cp.getAnswerNumber() + "번");
                    temp.append("정답: " + cp.getAnswerNumber() + "번" + " (내 답: " + choiceNumber + "번)\n");
                    vm.rank(problem);
                    pm.wrongProblems.add(temp.toString());
                    pm.wrongWords.add(vm.voc.get(pm.problems[currentProblemIndex]));
                }

                currentProblemIndex++;
                showNextProblem();
            });
        }

        revalidate();
        repaint();
    }

    private void subjectiveProblemUI(String problem, int problemNumber) {
        StringBuilder temp;

        // 화면 초기화
        getContentPane().removeAll();
        setLayout(new BorderLayout(10, 10));

        SubjectiveProblem sp = new SubjectiveProblem(
                problemNumber,
                problem,
                vm,
                rand.nextInt(2)+1   // 1: 영->한, 2: 한->영
        );

        JLabel questionLabel = new JLabel();
        questionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        temp = new StringBuilder();
        if (sp.subjectiveType == 1) {
            questionLabel.setText(problemNumber + ". '" + problem + "' 의 뜻을 입력하세요.");
            temp.append(questionLabel.getText());
        } else {
            String korMeaning = vm.getVoc().get(problem).getKor();
            questionLabel.setText(problemNumber + ". '" + korMeaning + "' 의 영단어를 입력하세요.");
            temp.append(questionLabel.getText());
        }

        add(questionLabel, BorderLayout.NORTH);

        answerField = new JTextField();
        answerField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(answerField, BorderLayout.CENTER);

        JButton submitBtn = new JButton("제출");
        submitBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        add(submitBtn, BorderLayout.SOUTH);

        ActionListener event = e -> {
            String userInput = answerField.getText().trim();

            if (userInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "답을 입력하세요!");
                return;
            }

            if (sp.isCorrect(userInput)) {
                JOptionPane.showMessageDialog(this, "정답입니다!");
                pm.rightCount++;
            } else {
                if (sp.subjectiveType == 1) {
                    JOptionPane.showMessageDialog(
                            this,
                            "오답입니다.\n정답: " + vm.getVoc().get(problem).getKor()
                    );
                    temp.append("\n정답: " + vm.getVoc().get(problem).getKor() + " (내 답: " + userInput + ")\n");
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "오답입니다.\n정답: " + problem
                    );
                    temp.append("\n정답: " + problem + " (내 답: " + userInput + ")\n");
                }

                pm.wrongCount++;
                vm.rank(problem);
                pm.wrongProblems.add(temp.toString());
                pm.wrongWords.add(vm.voc.get(pm.problems[currentProblemIndex]));
            }

            // 다음 문제로 진행
            currentProblemIndex++;
            showNextProblem();
        };

        submitBtn.addActionListener(event);
        answerField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    event.actionPerformed(null);
            }
        });

        revalidate();
        repaint();
    }
}
