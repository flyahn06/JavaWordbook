package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;

public class VocManager {
    static Scanner scan = new Scanner(System.in);
    HashMap<String, Word> voc;
    Vector<String> orderedEnglish;
    String userName;
    int i = 1; //몇 번째 오답노트인지 구별하기 위한 변수임

    VocManager(String userName) {
        this.userName = userName;
        this.voc = new HashMap<>();
        this.orderedEnglish = new Vector<>();
    }

    public HashMap<String, Word> getVoc() {
        return voc;
    }

    public Vector<String> getOrderedEnglish() {
        return orderedEnglish;
    }

    void addWord(String eng, String kor, String ranking) {
        this.voc.put(eng, new Word(eng, kor, ranking));
        this.orderedEnglish.add(eng);
    }

    boolean makeVoc(String fileName) {
        try (Scanner file = new Scanner(new File(fileName))) {
            String line;
            String[] lineSplit;

            while (file.hasNextLine()) {
                line = file.nextLine();
                lineSplit = line.split("\t");
                this.addWord(lineSplit[0].trim(), lineSplit[1].trim(), lineSplit[2].trim());
            }

            System.out.printf("%s님의 단어장 생성완료\n", this.userName);

        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
            return false;
        }

        return true;
    }

    void printMenu() {
        System.out.println("\n------" + userName + "의 단어장 -------");
        System.out.println("1) 단어검색");
        System.out.println("2) 단어검색2");
        System.out.println("3) 단어수정");
        System.out.println("4) 단어삭제");
        System.out.println("5) 전체단어출력");
        System.out.println("6) 파일 저장하기");
        System.out.println("7) 파일 불러오기");
        System.out.println("0) 메뉴 출력");
        System.out.println("99) 종료");
        System.out.println("-".repeat(20));
        System.out.println();
    }

    void menu() {
        int choice = 0;
        boolean running = true;

        this.printMenu();

        while (running) {
            System.out.print("메뉴를 선택하세요: ");
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
                case 2 -> searchVoc2();
                case 3 -> editWord();
                case 4 -> deleteWord();
                case 5 -> printAllWords();
                case 6 -> fileSave();
                case 7 -> fileLoad();
                case 0 -> printMenu();
                case 99 -> {
                    System.out.println(userName + "의 단어장 프로그램을 종료합니다.");
                    running = false;
                }
                default -> System.out.println("올바른 번호를 입력하세요!");
            }
        }
    }

    public void WAnotes(Vector<Word> wrongAnswers) { // 여기서 전해지는 파라미터는 오답들만 모아놓은 벡터
        String filename = "오답노트" + i + ".txt";
        try (PrintWriter outfile = new PrintWriter(filename)) {
            for (Word j : wrongAnswers) {
                String str = j.getEng() + "\t" + j.getKor();
                outfile.println(str);
            }
            System.out.println("오답노트가 만들어졌습니다.");
            i++;
        } catch (FileNotFoundException e) {
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

        System.out.println("1) 영단어수정 2) 한글뜻수정");
        System.out.print("메뉴를 입력하세요: ");
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
                System.out.print("새로운 단어를 입력하세요: ");
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
                System.out.print("새로운 뜻을 입력하세요: ");
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

    public void searchVoc2() {
        System.out.println("------ 단어 검색 2 ------");
        System.out.print("검색할 부분 단어를 입력하세요 (영단어) : ");
        String sWord = scan.nextLine();

        for (Word word : this.voc.values()) {
            if (word.getEng().indexOf(sWord) == 0) {
                System.out.println(word);
            }
        }
    }

    public void searchVoc() {
        System.out.println("------ 단어 검색 ------");
        System.out.print("검색할 단어를 입력하세요 (영단어) : ");
        String sWord = scan.nextLine();
        Word targetWord = this.voc.get(sWord);

        if (targetWord != null) {
            System.out.println("단어의 뜻: " + targetWord.getKor());
        }
    }

    public void fileWriter(PrintWriter outfile) {
        for (String w : this.orderedEnglish) {
            outfile.println(this.voc.get(w).getEng() + "\t" + this.voc.get(w).getKor());
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
            case 1 -> this.vocToFile("res/words.txt");
            case 2 -> {
                System.out.print("새 파일의 이름을 입력하세요: ");
                filename = scan.nextLine();
                filename = filename.trim().replaceAll("\\s+", "_");

                if (!(filename.indexOf(".txt") == filename.length() - 4))
                    filename = "res/" + filename + ".txt";

                this.vocToFile(filename);
            }
            default -> System.out.println("입력이 잘못되어 메뉴로 돌아갑니다.");
        }
    }
    //out.close();

    public void fileLoad() {
        String filename;

        System.out.print("불러올 파일의 이름을 입력하세요: ");
        filename = scan.nextLine();
        filename = "res/" + filename + ".txt";

        voc.clear();
        this.makeVoc(filename);
    }

    public void rank(String problem) {
        voc.get(problem).setRanking(voc.get(problem).getRanking() + 1);
        this.vocToFile("res/words.txt");
    }
}