package problems;

import main.VocManager;

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
                result = this.vm.getVoc().get(problem).getKor().contains(s.trim());
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
        System.out.println(this.getProblem1());
    }

    @Override
    public String getProblem1() {
        String str = problemNumber + ". 다음 단어의 뜻을 쓰시오.";
        str += "\n" + "[ " + problem + " ]";
        return str;
    }

    @Override
    public void showProblem2() {
        System.out.println(this.getProblem2());
    }

    public String getProblem2() {
        String str = problemNumber + ". 다음 뜻을 가진 단어를 쓰시오.";
        str += "\n" + "[ " + this.vm.getVoc().get(problem).getKor() + " ]";
        return str;
    }
}
