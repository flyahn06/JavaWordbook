package main;

public class ChoiceProblem extends Problem {
    String[] choices;

    public ChoiceProblem(int number, String word, String[] meanings) {
        super(number,0,word);
        this.choices = meanings;
    }

    @Override
    public boolean isCorrect(String input) {
        try {
            int userChoice = Integer.parseInt(input);
            return userChoice == answer;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void showProblem1() {
        System.out.println(problemNumber + ". 다음 중 이 단어의 뜻을 고르시오.");
        System.out.println(problem);
    }

    @Override
    public void showProblem2() {
        System.out.println(problemNumber + ". 다음 중 이 단어의 뜻을 고르시오.");
        System.out.println(problem);
    }

}
