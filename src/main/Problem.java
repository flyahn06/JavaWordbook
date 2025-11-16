package main;

public abstract class Problem {
    int problemNumber;
    int problemType; // 0:객관식, 1:주관식
    String problem;

    public Problem(int problemNumber, int problemType, String problem) {
        this.problemNumber = problemNumber;
        this.problemType = problemType;
        this.problem = problem;
    }

    public abstract boolean isCorrect(String input);

    public abstract void showProblem1(); // eng to kor

    public abstract void showProblem2(); // kor to eng
}
