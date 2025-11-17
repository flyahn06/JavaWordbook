package main;

import java.util.Vector;

public class SubjectiveProblem extends Problem{
    int subjectiveType;
    boolean result;
    public SubjectiveProblem(int problemNumber, String problem, VocManager vm, int subjectiveType) {
        super(problemNumber, 1, problem, vm);
        this.subjectiveType = subjectiveType;
    }

    @Override
    public boolean isCorrect(String input) {
        if (subjectiveType == 1) {
            String[] inputArray = input.split(",\\s*");
            for (String s : inputArray) {
                result = this.vm.getVoc().get(problem).kor.contains(s.trim());
                if (!result) return false;
            }
            return true;
        }
        else {
            return input.trim().equalsIgnoreCase(problem);
        }
    }

    @Override
    public void showProblem1() {
        System.out.println(problemNumber + ". 다음 단어의 뜻을 쓰시오.");
        System.out.println("[ " + problem + " ]");
    }

    @Override
    public void showProblem2() {
        System.out.println(problemNumber + ". 다음 뜻을 가진 단어를 쓰시오.");
        System.out.println("[ " + this.vm.getVoc().get(problem).getKor() + " ]");
    }
}
