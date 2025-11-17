package problems;

import main.VocManager;

public abstract class Problem {
    int problemNumber;
    int problemType; // 0:객관식, 1:주관식
    String problem;
    VocManager vm;

    public Problem(int problemNumber, int problemType, String problem, VocManager vm) {
        this.problemNumber = problemNumber;
        this.problemType = problemType;
        this.problem = problem;
        this.vm = vm;
    }

    public abstract boolean isCorrect(String input);

    public abstract void showProblem1(); // eng to kor

    public abstract void showProblem2(); // kor to eng
}
