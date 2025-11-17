package problems;

import main.VocManager;

import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class ProblemManager {
    public int problemCount;
    int problemType;
    String[] problems;
    public int rightCount;
    int wrongCount;
    VocManager vm;
    Vector<String> wrongProblems = new Vector<String>();
    Vector<Word> wrongWords = new Vector<Word>();

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
        problems = new String[problemCount];
        for (int i=0; i<problemCount; i++) {
            problems[i] = engList.get(i);
        }

        scan.nextLine();

        switch (problemType) {
            case 1 -> {
                for (int i = 0; i < problemCount; i++) {
                    ChoiceProblem choiceProblem = new ChoiceProblem(i + 1, problems[i], vm);
                    if (rand.nextInt(2) == 0) {
                        //choiceProblem.showProblem1();
                        String temp = choiceProblem.getProblem1();
                        System.out.println(temp);
                    }
                    else {
                        //choiceProblem.showProblem2();
                        String temp = choiceProblem.getProblem2();
                        System.out.println(temp);
                    }
                    System.out.print("> ");
                    if (choiceProblem.isCorrect(scan.nextLine())) {
                        System.out.println("정답입니다.");
                        rightCount++;
                    }
                    else {
                        System.out.println("오답입니다. 정답은 "+choiceProblem.answerNumber+"번입니다.");
                        temp += "\n" + "오답입니다. 정답은 "+choiceProblem.answerNumber+"번입니다." + "\n"
                        wrongCount++;
                        vm.rank(problems[i]);
                        wrongProblems.add(temp);
                        wrongWords.add(vm.voc.get(problems[i]))
                    }

                }
            }
            case 2 -> {
                for (int i=0; i < problemCount; i++) {
                    SubjectiveProblem subjectiveProblem = new SubjectiveProblem(i+1,problems[i], vm, rand.nextInt(2)+1);
                    if (subjectiveProblem.subjectiveType == 1) {
                        //subjectiveProblem.showProblem1();
                        String temp = subjectiveProblem.getProblem1();
                        System.out.println(temp);
                    }
                    else {
                        //subjectiveProblem.showProblem2();
                        String temp = subjectiveProblem.getProblem2();
                        System.out.println(temp);
                    }
                    System.out.print("> ");
                    if (subjectiveProblem.isCorrect(scan.nextLine())) {
                        System.out.println("정답입니다.");
                        rightCount++;
                    } else if (subjectiveProblem.subjectiveType==1) {
                        System.out.println("오답입니다. 정답은 " + this.vm.getVoc().get(subjectiveProblem.problem).getKor() + "입니다.");
                        temp += "\n" + "오답입니다. 정답은 " + this.vm.getVoc().get(subjectiveProblem.problem).getKor() + "입니다.";
                        wrongCount++;
                        vm.rank(problems[i]);
                        wrongProblems.add(temp);
                        wrongWords.add(vm.voc.get(problems[i]))
                    } else {
                        System.out.println("오답입니다. 정답은 " + problems[i]+"입니다.");
                        temp += "\n" + "오답입니다. 정답은 " + problems[i]+"입니다.";
                        wrongCount++;
                        vm.rank(problems[i]);
                        wrongProblems.add(temp);
                        wrongWords.add(vm.voc.get(problems[i]))
                    }
                }
            }
        }

        vm.vocToFile(vm.getFileName());
    }
}
