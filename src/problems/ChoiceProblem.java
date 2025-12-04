package problems;

import main.VocManager;

import java.util.*;

public class ChoiceProblem extends Problem {
    List<String> wordList = new ArrayList<>();
    List<String> wrongWordList = new ArrayList<>();
    int answerNumber;
    static Random rand = new Random();

    public ChoiceProblem(int problemNumber, String problem, VocManager vm) {
        super(problemNumber,0,problem, vm);
        Vector<String> engList = this.vm.getOrderedEnglish();
        int randomNumber;

        for (int i=0; i<3; i++) {
            randomNumber = rand.nextInt(engList.size());
            if (!problem.equals(engList.get(randomNumber)) && !wrongWordList.contains(engList.get(randomNumber)))
                wrongWordList.add(engList.get(i));
            else i--;
        }
        wordList.add(problem); // wordList에 정답단어 추가
        wordList.addAll(wrongWordList); // wordList에 오답단어 추가
        Collections.shuffle(wordList); // wordList 섞기
        answerNumber = wordList.indexOf(problem) + 1;
    }

    public List<String> getWordList() { return wordList; }

    public int getAnswerNumber() {
        return answerNumber;
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
        System.out.println(this.getProblem1());
    }

    //뜻을 묻는 문제를 반환
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
        System.out.println(this.getProblem2());
    }

    //영단어를 묻는 문제를 반환
    @Override
    public String getProblem2() {
        String str = problemNumber + ". 다음 중 이 뜻을 가진 단어를 고르시오.";
        str += "\n" + "[ " + this.vm.getVoc().get(problem).getKor() + " ]";
        for (int i=0; i<4; i++) {
            str += "\n" + (i+1) + ") " + wordList.get(i);
        }
        return str;
    }
}
