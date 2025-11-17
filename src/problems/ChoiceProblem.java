package problems;

import main.VocManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class ChoiceProblem extends Problem {
    List<String> wordList = new ArrayList<>();
    List<String> wrongWordList = new ArrayList<>();
    int answerNumber;

    public ChoiceProblem(int problemNumber, String problem, VocManager vm) {
        super(problemNumber,0,problem, vm);// word: 정답단어
        Vector<String> engList = this.vm.getOrderedEnglish();
        Collections.shuffle(engList);
        for (int i=0; i<3; i++) {
            if (!problem.equals(engList.get(i))) wrongWordList.add(engList.get(i));
            else i--;
        }
        wordList.add(problem);
        wordList.addAll(wrongWordList);
        Collections.shuffle(wordList);
        answerNumber = wordList.indexOf(problem) + 1;
    }


    @Override
    public boolean isCorrect(String input) {
        try {
            int userChoice = Integer.parseInt(input);
            return userChoice == answerNumber;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void showProblem1() {
        System.out.println(problemNumber + ". 다음 중 이 단어의 뜻을 고르시오.");
        System.out.println("[ " + problem + " ]");
        for (int i=0; i<4; i++) {
            System.out.println(i+1 + ") " + this.vm.getVoc().get(wordList.get(i)).getKor());
        }
    }

    @Override
    public String getProblem1() {
        String str = problemNumber + ". 다음 중 이 단어의 뜻을 고르시오.";
        str += "\n" + "[ " + problem + " ]";
        for (int i=0; i<4; i++) {
            str += "\n" + (i+1) + ") " + this.vm.getVoc().get(wordList.get(i)).getKor();
        }
        return str;
    }

    @Override
    public void showProblem2() {
        System.out.println(problemNumber + ". 다음 중 이 뜻을 가진 단어를 고르시오.");
        System.out.println("[ " + this.vm.getVoc().get(problem).getKor() + " ]");
        for (int i=0; i<4; i++) {
            System.out.println(i+1 + ") " + wordList.get(i));
        }
    }

    public String getProblem2() {
        String str = problemNumber + ". 다음 중 이 뜻을 가진 단어를 고르시오.";
        str += "\n" + "[ " + this.vm.getVoc().get(problem).getKor() + " ]";
        for (int i=0; i<4; i++) {
            str += "\n" + (i+1) + ") " + wordList.get(i);
        }
        return str;
    }
}
