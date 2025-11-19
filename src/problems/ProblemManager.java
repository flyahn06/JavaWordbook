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

    public ProblemManager(VocManager vm) {
        rightCount = 0;
        wrongCount = 0;
        this.vm = vm;
    }

    public void generateProblems() {
        Scanner scan = new Scanner(System.in);
        Random rand = new Random();
        String userAnswer;

        while (true) {
            System.out.println("1) 객관식 2) 주관식 3) 객관식+주관식");
            System.out.print("> ");

            try {
                problemType = scan.nextInt();

                if (problemType==1 || problemType==2 || problemType==3) break;
                else System.out.println("입력이 잘못되었습니다.");

            } catch (InputMismatchException e) {
                System.out.println("입력이 잘못되었습니다.");
                scan.nextLine();
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

        Vector<String> engList = this.vm.getOrderedEnglish();
        Collections.shuffle(engList);

        while(engList.size() < problemCount) {
            Vector<String> engListTemp = this.vm.getOrderedEnglish();
            Collections.shuffle(engListTemp);
            engList.addAll(engListTemp);
        }

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
                        temp = choiceProblem.getProblem1();
                        System.out.println(temp);
                    }
                    else {
                        //choiceProblem.showProblem2();
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
            }
            case 2 -> {
                for (int i=0; i < problemCount; i++) {
                    SubjectiveProblem subjectiveProblem = new SubjectiveProblem(i+1,problems[i], vm, rand.nextInt(2)+1);
                    if (subjectiveProblem.subjectiveType == 1) {
                        //subjectiveProblem.showProblem1();
                        temp = subjectiveProblem.getProblem1();
                        System.out.println(temp);
                    }
                    else {
                        //subjectiveProblem.showProblem2();
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
            }
            case 3 -> {
                for (int i=0; i < problemCount; i++) {
                    if (rand.nextInt(2) == 0) {
                        ChoiceProblem choiceProblem = new ChoiceProblem(i + 1, problems[i], vm);
                        if (rand.nextInt(2) == 0) {
                            //choiceProblem.showProblem1();
                            temp = choiceProblem.getProblem1();
                            System.out.println(temp);
                        }
                        else {
                            //choiceProblem.showProblem2();
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
                    else {
                        SubjectiveProblem subjectiveProblem = new SubjectiveProblem(i+1,problems[i], vm, rand.nextInt(2)+1);
                        if (subjectiveProblem.subjectiveType == 1) {
                            //subjectiveProblem.showProblem1();
                            temp = subjectiveProblem.getProblem1();
                            System.out.println(temp);
                        }
                        else {
                            //subjectiveProblem.showProblem2();
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
                }
            }
        }

        vm.vocToFile(vm.getFileName());
    }
}
