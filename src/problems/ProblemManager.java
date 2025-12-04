package problems;

import main.VocManager;
import main.Word;

import java.util.*;

public class ProblemManager {
    public int problemCount;
    public int problemType;
    public String[] problems;
    public int rightCount;
    public int wrongCount;
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
            System.out.println(temp); // eng to kor 문제 출제
        }
        else {
            temp = choiceProblem.getProblem2();
            System.out.println(temp); // kor to eng 문제 출제
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
            System.out.println(temp); // eng to kor 문제 출제
        }
        else {
            temp = subjectiveProblem.getProblem2();
            System.out.println(temp); // kor to eng 문제 출제
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

    // 객관식+주관식 혼합 문제
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

        // 단어가 10개가 안 되어서
        // Top 10을 뽑을 수 없는 경우 최대 단어의 개수로 설정
        if (words.size() < 10) {
            System.out.println("주의: 단어 수가 10개 미만입니다.");
            System.out.printf("문항 수를 %d개로 조정합니다.\n", words.size());
            this.problemCount = words.size();
        } else {
            System.out.println("총 문항 수는 10개입니다.");
            this.problemCount = 10;
        }

        // Word의 rank를 기준으로 내림차순 정렬
        words.sort(new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return Integer.compare(o2.getRanking(), o1.getRanking());
            }
        });

        // 내림차순으로 정렬되어 있기 때문에
        // 앞에서 N개를 뽑으면 오답률 Top N이 됨
        for (int i = 0; i < this.problemCount; i++) {
            engList.add(words.get(i).getEng());
        }

        generateQuiz(engList);
    }

    /**
     * 주어진 Vector 객체를 사용해 문제를 만듧니다.
     * @param with
     * 문제를 만들 영단어의 vector입니다. 중복은 허용되나, generateQuiz가 동작하는 동안에는 이 Vector의 모든 element가 vm.voc.key에 있음이 보장되어야 합니다.
     */
    public void generateQuiz(Vector<String> with) {
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
