package main;

import api.Translator;
import problems.ProblemManager;

import java.io.*;
import java.util.*;

public class VocManager {
    static Scanner scan = new Scanner(System.in);
    public HashMap<String, Word> voc;
    Vector<String> orderedEnglish;
    String userName;
    static int i = 1; //몇 번째 오답노트인지 구별하기 위한 변수임
    String fileName;
    static Translator translator = new Translator();
    static final String isNumericRegex = "\\d+";

    VocManager(String userName) {
        this.userName = userName;
        this.voc = new HashMap<>();
        this.orderedEnglish = new Vector<>();
    }

    public HashMap<String, Word> getVoc() {
        return voc;
    }

    public Vector<String> getOrderedEnglish() {
        return new Vector<String>(this.orderedEnglish);
    }

    void addWord(String eng, String kor, int ranking) {
        this.voc.put(eng, new Word(eng, kor, ranking));

        if (!this.orderedEnglish.contains(eng))
            this.orderedEnglish.add(eng);
    }

    boolean makeVoc(String fileName) {
        int rank;
        this.fileName = fileName;
        try (Scanner file = new Scanner(new File(fileName))) {
            String line;
            String[] lineSplit;
            this.voc.clear();

            while (file.hasNextLine()) {
                line = file.nextLine();
                lineSplit = line.split("\t");

                if (lineSplit.length < 3) {
                    System.out.printf("파일 %s에 문제가 있습니다.\n\t행(%s)이 잘못된 형식입니다.\n", this.fileName, line);
                }

                try {
                    rank = Integer.parseInt(lineSplit[2].trim());
                } catch (NumberFormatException e) {
                    System.out.printf("파일 %s에 문제가 있습니다.\n\t행(%s)의 3번째 원소가 문자가 아닙니다.\n", this.fileName, line);
                    rank = 0;
                }

                this.addWord(lineSplit[0].trim(), lineSplit[1].trim(), rank);
            }

            System.out.printf("%s님의 단어장 생성완료\n", this.userName);

        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
            return false;
        }

        return true;
    }

    public String getFileName() {
        return this.fileName;
    }

    void printMenu() {
        String banner = String.format("-------- %s의 단어장 [ %s ] ---------", this.userName, this.fileName);
        System.out.printf("\n%s\n", banner);
        System.out.println("[검색] 1) 단어검색  2) 단어부분검색  3) 전체단어출력");
        System.out.println("[수정] 4) 단어추가  5) 단어수정  6) 단어삭제");
        System.out.println("[퀴즈] 7) 퀴즈 풀기");
        System.out.println("[파일] 8) 파일 저장하기  9) 파일 불러오기");
        System.out.println("[메뉴] 0) 메뉴 출력  99) 프로그램 종료");
        System.out.println("-".repeat(banner.length() + 1));
        System.out.println();
    }

    void addWordHelper() {
        String eng, kor;

        System.out.print("추가할 단어를 입력하세요: ");
        eng = scan.nextLine().trim();

        if (eng.isEmpty()) {
            System.out.println("단어를 잘 입력하세요!");
            return;
        }

        if (this.orderedEnglish.contains(eng)) {
            String option;
            System.out.print("[주의] 단어장에 단어가 이미 존재합니다. 계속 진행하면 기존의 단어가 대체됩니다. 진행하시겠습니까? (Y/N) ");
            option = scan.nextLine();

            if (!option.toLowerCase().trim().equals("y"))
                return;
        }

        System.out.print("단어의 뜻을 입력하세요(빈칸이면 검색): ");
        kor = scan.nextLine().trim();
        if (kor.equals("?") || kor.isEmpty()) {
            kor = VocManager.translator.getMeaning(eng);

            if (kor == null) {
                System.out.println("단어의 뜻을 찾을 수 없습니다!");
                return;
            }
            System.out.printf("검색된 단어의 뜻: %s\n", kor);
        }
        this.addWord(eng, kor, 0);
        System.out.println("단어가 잘 추가되었습니다.");
    }

    void problem() {
        ProblemManager pm = new ProblemManager(this);
        pm.generateProblems();
        writeCorrectRate(pm, i-1);
        wrongAnswers(pm);
    }

    void menu() {
        int choice = 0;
        boolean running = true;

        this.printMenu();

        while (running) {
            System.out.print("> ");
            try {
                choice = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("올바른 번호를 입력하세요!");
                scan.next();
                continue;
            }
            scan.nextLine();

            switch (choice) {
                case 1 -> searchVoc();
                case 2 -> searchVocPartial();
                case 3 -> printAllWords();
                case 4 -> addWordHelper();
                case 5 -> editWord();
                case 6 -> deleteWord();
                case 7 -> problem();
                case 8 -> fileSave();
                case 9 -> fileLoad();
                case 0 -> printMenu();
                case 99 -> {
                    System.out.println(userName + "의 단어장 프로그램을 종료합니다.");
                    running = false;
                }
                default -> System.out.println("올바른 번호를 입력하세요!");
            }
        }
    }

    public void wrongAnswers(ProblemManager PM) {
        System.out.print("문제오답노트를 만들기를 원하면 1을 입력하시오.  ");
        int choice = scan.nextInt();
        if(choice == 1) {
            WAnotes2(PM.wrongProblems);
        }
        WAnotes(PM.wrongWords);
    }

    //틀린 단어들의 벡터를 전달하면 그 단어들을 오답노트i.txt에 저장하는 메서드
    public void WAnotes(Vector<Word> wrongAnswers) { // 여기서 전해지는 파라미터는 오답들만 모아놓은 벡터
        String filename = "오답노트" + i + ".txt";
        filename = "res/" + filename;
        try (PrintWriter outfile = new PrintWriter(filename)) {
            for (Word j : wrongAnswers) {
                String str = j.getEng() + "\t" + j.getKor() + "\t" + j.getRanking();
                outfile.println(str);
            }
            System.out.println("오답노트가 만들어졌습니다.");
            i++;
        } catch (FileNotFoundException e) {
            System.out.println("오류");
        }
    }

    // 틀린 문제들을 인자로 전달하면 그 문제들을 문제오답노트i.txt에 저장하는 메서드
    public void WAnotes2(Vector<String> wp) {
        String filename = "문제오답노트" + i + ".txt";
        filename = "res/" + filename;
        try (PrintWriter outfile = new PrintWriter(filename)) {
            for (String str : wp) {
                outfile.println(str);
            }
            System.out.println("문제오답노트가 만들어졌습니다");
        } catch (FileNotFoundException e) {
            System.out.println("오류");
        }
    }

    //정답률 계산후 scores.txt에 정답률 append하는 메서드
    public void writeCorrectRate(ProblemManager PM, int i) {
        double correctRate = (double) PM.rightCount / PM.problemCount;
        String contentToAppend = i +", "+ String.format("%.2f", correctRate); //i, 정답률의 형태
        try (FileWriter fw = new FileWriter("res/scores.txt", true)) {
            fw.write("\n"+contentToAppend); //scores.txt에 contentToAppend를 append
        } catch (IOException e) {
            System.out.println("오류");
        }
    }

    public void printAllWords() {
        for (String w : this.orderedEnglish) {
            System.out.println(this.voc.get(w));
        }
    }

    public void deleteWord() {
        Word targetWord;
        String eng;

        System.out.print("삭제할 영단어를 입력하세요: ");
        eng = scan.nextLine().trim();
        targetWord = this.voc.get(eng);

        if (targetWord == null) {
            System.out.println("단어를 찾을 수 없습니다.");
            return;
        }

        this.voc.remove(eng, targetWord);
        this.orderedEnglish.remove(eng);
    }

    public void editWord() {
        Word targetWord;
        String temp;
        String eng;
        int option;
        int originalIndex;

        System.out.print("수정할 영단어를 입력하세요: ");
        eng = scan.nextLine().trim();
        targetWord = this.voc.get(eng);

        if (targetWord == null) {
            System.out.println("단어를 찾을 수 없습니다.");
            return;
        }

        System.out.println("\n------- 단어 수정 -------");
        System.out.println("1) 영단어수정  2) 한글뜻추가  3) 한글뜻수정");
        System.out.printf("%s 수정> ", eng);
        try {
            option = scan.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("올바른 번호를 입력하세요!");
            return;
        }

        scan.nextLine();

        // 여기서부터 변경하고자 하는 단어가 존재함이 보장됨
        switch (option) {
            case 1 -> {
                System.out.print("바뀔 단어를 입력하세요: ");
                temp = scan.nextLine();

                if (temp.isEmpty()) {
                    System.out.println("단어가 변경되지 않았습니다.");
                } else {
                    originalIndex = this.orderedEnglish.indexOf(targetWord.getEng());
                    this.voc.remove(targetWord.getEng(), targetWord);
                    this.orderedEnglish.remove(originalIndex);

                    targetWord.setEng(temp);
                    this.voc.put(temp, targetWord);
                    this.orderedEnglish.insertElementAt(temp, originalIndex);
                    System.out.println("단어가 잘 변경되었습니다.");
                }
            }
            case 2 -> {
                System.out.print("추가할 뜻을 입력하세요(,로 뜻 구분): ");
                temp = scan.nextLine();

                if (temp.isEmpty()) {
                    System.out.println("단어가 변경되지 않았습니다.");
                } else {
                    targetWord.setKor(temp, false);
                    System.out.println("단어가 잘 변경되었습니다.");
                }
            }
            case 3 -> {
                System.out.print("새로운 뜻을 입력하세요(,로 뜻 구분): ");
                temp = scan.nextLine();

                if (temp.isEmpty()) {
                    System.out.println("단어가 변경되지 않았습니다.");
                } else {
                    targetWord.setKor(temp);
                    System.out.println("단어가 잘 변경되었습니다.");
                }
            }
            default -> {
                System.out.println("잘못된 옵션입니다.");
            }
        }
    }

    public void searchVocPartial() {
        System.out.print("검색할 부분 단어를 입력하세요 (영단어): ");
        String sWord = scan.nextLine();

        for (Word word : this.voc.values()) {
            if (word.getEng().indexOf(sWord) == 0) {
                System.out.println(word);
            }
        }
    }

    public void searchVoc() {
        System.out.print("검색할 단어를 입력하세요 (영단어): ");
        String sWord = scan.nextLine();
        Word targetWord = this.voc.get(sWord);

        if (targetWord != null) {
            System.out.println("단어의 뜻: " + targetWord.getKor());
        } else {
            System.out.println("찾는 단어가 단어장에 없습니다.");
        }
    }

    public void fileWriter(PrintWriter outfile) {
        for (String w : this.orderedEnglish) {
            Word word = this.voc.get(w);
            outfile.println(word.getEng() + "\t" + word.getKor() + "\t" + word.getRanking());
        }
    }

    public void vocToFile(String filename) {
        try (PrintWriter outfile = new PrintWriter(filename)) {
            this.fileWriter(outfile);
            System.out.println("단어장이 '" + filename + "' 파일로 저장되었습니다.");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void fileSave() {
        int choice = 0;
        String filename;

        try {
            System.out.println("1) 파일 덮어쓰기 2) 새로운 파일로 저장하기");
            System.out.print("선택하세요: ");
            choice = scan.nextInt();
            scan.nextLine();

        } catch (InputMismatchException e) {
            scan.nextLine();
        }

        System.out.println();
        switch (choice) {
            case 1 -> this.vocToFile(this.fileName);
            case 2 -> {
                System.out.print("새 파일의 이름을 입력하세요: ");
                filename = scan.nextLine();
                filename = filename.trim().replaceAll("\\s+", "_");

                if (!(filename.indexOf(".txt") == filename.length() - 4))
                    filename = filename + ".txt";

                filename = "res/" + filename;
                this.vocToFile(filename);
            }
            default -> System.out.println("입력이 잘못되어 메뉴로 돌아갑니다.");
        }
    }

    public void fileLoad() {
        String filename, option;

        System.out.printf("%s의 변경사항을 저장하시겠습니까? ([Y]/N) ", this.fileName);
        option = scan.nextLine().toLowerCase().trim();

        if (option.isEmpty() || option.equals("y"))
            this.vocToFile(this.fileName);

        System.out.print("불러올 파일의 이름을 입력하세요: ");
        filename = scan.nextLine();
        filename = "res/" + filename;

        this.makeVoc(filename);
    }

    public void rank(String problem) {
        voc.get(problem).setRanking(voc.get(problem).getRanking() + 1);
    }
}