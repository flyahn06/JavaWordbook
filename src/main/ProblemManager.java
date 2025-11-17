package main;

import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class ProblemManager {
    int problemCount;
    int problemType;
    String[] problems;
    int rightCount;
    int wrongCount;
    VocManager vm;

    public ProblemManager(VocManager vm) {
        rightCount = 0;
        wrongCount = 0;
        this.vm = vm;
    }

    public void generateProblems() {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("1) 객관식 2) 주관식");
        System.out.print("> ");
        problemType = scan.nextInt();

        System.out.print("문제 수를 입력하세요: ");
        problemCount = scan.nextInt();

        Vector<String> engList = this.vm.getOrderedEnglish();
        Collections.shuffle(engList);
        for (int i=0; i<problemCount; i++) {
            problems[i] = engList.get(i);
        }

        switch (problemType) {
            case 1 -> {
                for (int i = 0; i < problemCount; i++) {
                    ChoiceProblem choiceProblem = new ChoiceProblem(i + 1, problems[i], vm);
                    if (rand.nextInt(2) == 0) choiceProblem.showProblem1();
                    else choiceProblem.showProblem2();
                    System.out.print("> ");
                    if (choiceProblem.isCorrect(scan.nextLine())) {
                        System.out.println("정답입니다.");
                        rightCount++;
                    }
                    else {
                        System.out.println("오답입니다. 정답은 "+choiceProblem.answerNumber+"번입니다.");
                        wrongCount++;
                        vm.rank(problems[i]);
                    }

                }
            }
            case 2 -> {
                for (int i=0; i < problemCount; i++) {
                    SubjectiveProblem subjectiveProblem = new SubjectiveProblem(i+1,problems[i], vm, rand.nextInt(2)+1);
                    if (subjectiveProblem.subjectiveType == 1) subjectiveProblem.showProblem1();
                    else subjectiveProblem.showProblem2();
                    System.out.println("> ");
                    if (subjectiveProblem.isCorrect(scan.nextLine())) {
                        System.out.println("정답입니다.");
                        rightCount++;
                    } else if (subjectiveProblem.subjectiveType==1) {
                        System.out.println("오답입니다. 정답은 " + this.vm.getVoc().get(subjectiveProblem.problem).kor + "입니다.");
                        wrongCount++;
                        vm.rank(problems[i]);
                    } else {
                        System.out.println("오답입니다. 정답은 " + problems[i]+"입니다.");
                        wrongCount++;
                        vm.rank(problems[i]);
                    }
                }
            }
        }
    }
}
