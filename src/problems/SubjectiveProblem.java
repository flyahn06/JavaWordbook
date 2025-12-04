package problems;

import main.VocManager;

public class SubjectiveProblem extends Problem{
    public int subjectiveType;
    boolean result;
    public SubjectiveProblem(int problemNumber, String problem, VocManager vm, int subjectiveType) {
        super(problemNumber, 1, problem, vm);
        this.subjectiveType = subjectiveType;
    }

    @Override
    public boolean isCorrect(String input) {
        if (input.isEmpty()) return false;
        if (subjectiveType == 1) {
            String[] inputArray = input.split(",\\s*");
            for (String s : inputArray) {
                result = this.vm.getVoc().get(problem).getKor().contains(s.trim()); //사용자가 입력한 단어뜻들이 단어장에 적힌 단어뜻들 중에 있는지 확인
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

    //뜻을 묻는 문제를 반환
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

    //영단어를 묻는 문제를 반환
    @Override
    public String getProblem2() {
        String str = problemNumber + ". 다음 뜻을 가진 단어를 쓰시오.";
        str += "\n" + "[ " + this.vm.getVoc().get(problem).getKor() + " ]";
        return str;
    }
}
