package problems;

import main.VocManager;
import main.Word;

import java.util.*;

public class ProblemManager {
    public int problemCount;
    int problemType;
    String[] problems;
    public int rightCount;
    int wrongCount;
    VocManager vm;
    public Vector<String> wrongProblems = new Vector<String>();
    public Vector<Word> wrongWords = new Vector<Word>();
    String temp = "";
    static Random rand = new Random();
    static Scanner scan = new Scanner(System.in);


    public ProblemManager(VocManager vm) {
        rightCount = 0;
        wrongCount = 0;
        this.vm = vm;
    }

    public void choiceProblems(int i) {
        String userAnswer;
        ChoiceProblem choiceProblem = new ChoiceProblem(i + 1, problems[i], vm);
        if (rand.nextInt(2) == 0) {
            temp = choiceProblem.getProblem1();
            System.out.println(temp);
        }
        else {
            temp = choiceProblem.getProblem2();
            System.out.println(temp);
        }
        System.out.print("> ");
        userAnswer = scan.nextLine();
        if (choiceProblem.isCorrect(userAnswer)) {
            System.out.println("정답입니다.");
            rightCount++;
        }
        else {
            System.out.println("오답입니다. 정답은 "+choiceProblem.answerNumber+"번입니다.");
            temp += ("\n" + "오답입니다. 정답은 "+choiceProblem.answerNumber+"번입니다. (내 답: " + userAnswer + ")\n");
            wrongCount++;
            vm.rank(problems[i]);
            wrongProblems.add(temp);
            wrongWords.add(vm.voc.get(problems[i]));
        }
    }

    private void subjectiveProblems(int i) {
        String userAnswer;
        SubjectiveProblem subjectiveProblem = new SubjectiveProblem(i+1,problems[i], vm, rand.nextInt(2)+1);
        if (subjectiveProblem.subjectiveType == 1) {
            temp = subjectiveProblem.getProblem1();
            System.out.println(temp);
        }
        else {
            temp = subjectiveProblem.getProblem2();
            System.out.println(temp);
        }
        System.out.print("> ");
        userAnswer = scan.nextLine();
        if (subjectiveProblem.isCorrect(userAnswer)) {
            System.out.println("정답입니다.");
            rightCount++;
        } else if (subjectiveProblem.subjectiveType==1) {
            System.out.println("오답입니다. 정답은 " + this.vm.getVoc().get(subjectiveProblem.problem).getKor() + "입니다.");
            temp += "\n" + "오답입니다. 정답은 " + this.vm.getVoc().get(subjectiveProblem.problem).getKor() + "입니다. (내 답: " + userAnswer + ")\n";
            wrongCount++;
            vm.rank(problems[i]);
            wrongProblems.add(temp);
            wrongWords.add(vm.voc.get(problems[i]));
        } else {
            System.out.println("오답입니다. 정답은 " + problems[i]+"입니다.");
            temp += "\n" + "오답입니다. 정답은 " + problems[i]+"입니다. (내 답: " + userAnswer + ")\n";
            wrongCount++;
            vm.rank(problems[i]);
            wrongProblems.add(temp);
            wrongWords.add(vm.voc.get(problems[i]));
        }
    }

    public void mixedProblems(int i) {
        if (rand.nextInt(2) == 0)
            choiceProblems(i);
        else
            subjectiveProblems(i);
    }

    public void startQuiz() {
        if (this.vm.getOrderedEnglish().size() < 4) {
            problemType = 2;
            System.out.println("단어 수가 충분하지 않아 객관식을 출제할 수 없습니다.");
            System.out.println("주관식 문제만 출제합니다.");
        } else {
            while (true) {
                System.out.println("1) 객관식 2) 주관식 3) 객관식+주관식");
                System.out.print("> ");

                try {
                    problemType = scan.nextInt();

                    if (problemType == 1 || problemType == 2 || problemType == 3) break;
                    else System.out.println("입력이 잘못되었습니다.");

                } catch (InputMismatchException e) {
                    System.out.println("입력이 잘못되었습니다.");
                    scan.nextLine();
                }
            }
        }

        while (true) {
            System.out.print("문제 수를 입력하세요: ");

            try {
                problemCount = scan.nextInt();

                if (problemCount > 0) break;
                else System.out.println("입력이 잘못되었습니다. 문제 수는 1 이상이어야 됩니다.");
            } catch (InputMismatchException e) {
                System.out.println("입력이 잘못되었습니다.");
                scan.nextLine();
            }
        }

        scan.nextLine();

        generateQuiz(this.vm.getOrderedEnglish());
    }

    public void startQuizTop10() {
        // 주관식 고정
        Vector<String> engList = new Vector<>();

        System.out.println("오답률 Top 10 집중학습은 주관식으로만 진행됩니다.");
        this.problemType = 2;

        Vector<Word> words = new Vector<>(this.vm.getVoc().values());

        if (words.size() < 10) {
            System.out.println("주의: 단어 수가 10개 미만입니다.");
            System.out.printf("문항 수를 %d개로 조정합니다.\n", words.size());
            this.problemCount = words.size();
        } else {
            System.out.println("총 문항 수는 10개입니다.");
            this.problemCount = 10;
        }

        words.sort(new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return Integer.compare(o2.getRanking(), o1.getRanking());
            }
        });

        for (int i = 0; i < this.problemCount; i++) {
            engList.add(words.get(i).getEng());
        }

        generateQuiz(engList);
    }

    private void generateQuiz(Vector<String> with) {
        Collections.shuffle(with);

        while(with.size() < problemCount) {
            Vector<String> engListTemp = new Vector<>(with);
            Collections.shuffle(engListTemp);
            with.addAll(engListTemp);
        }

        problems = new String[problemCount];
        for (int i=0; i<problemCount; i++) {
            problems[i] = with.get(i);
        }


        switch (problemType) {
            case 1 -> {
                for (int i = 0; i < problemCount; i++) {
                    choiceProblems(i);
                }
            }
            case 2 -> {
                for (int i = 0; i < problemCount; i++) {
                    subjectiveProblems(i);
                }
            }
            case 3 -> {
                for (int i = 0; i < problemCount; i++) {
                    mixedProblems(i);
                }
            }
        }

        vm.vocToFile(vm.getFileName());
    }
}
