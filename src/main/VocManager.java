package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

public class VocManager {
    static Scanner scan = new Scanner(System.in);
    Vector<Word> voc = new Vector<>();
    String userName;
    int i = 1; //몇 번째 오답노트인지 구별하기 위한 변수임

    VocManager(String userName) {
        this.userName = userName;
    }

    void addWord(String eng, String kor) {
        this.voc.add(new Word(eng, kor));
    }

    boolean makeVoc(String fileName) {
        try (Scanner file = new Scanner(new File(fileName))) {
            String line;
            String[] lineSplit;

            while (file.hasNextLine()) {
                line = file.nextLine();
                lineSplit = line.split("\t");
                this.addWord(lineSplit[0].trim(), lineSplit[1].trim());
            }

            System.out.printf("%s님의 단어장 생성완료\n", this.userName);

        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
            return false;
        }

        return true;
    }

    void menu() {
        int choice = 0;
        boolean running = true;

        while (running) {
            System.out.println("\n------" + userName + "의 단어장 -------");
            System.out.println("1) 단어검색 2) 단어검색2 3) 단어수정 4) 단어삭제 5) 전체단어출력 6) 파일 저장하기 7) 파일 불러오기 0) 종료");
            System.out.print("메뉴를 선택하세요 : ");
            choice = scan.nextInt();
            scan.nextLine();
            System.out.println();

            switch (choice) {
                case 1 -> searchVoc();
                case 2 -> searchVoc2();
                case 3 -> editWord();
                case 4 -> deleteWord();
                case 5 -> printAllWords();
                case 6 -> fileSave();
                case 7 -> fileLoad();
                case 0 -> {
                    System.out.println(userName + "의 단어장 프로그램을 종료합니다.");
                    running = false;
                }
            }
        }
    }

    public void WAnotes(Vector<Word> wrongAnswers) { //여기서 전해지는 파라미터는 오답들만 모아놓은 벡터
        String filename = "오답노트" + i + ".txt";
        try (PrintWriter outfile = new PrintWriter(filename)) {
            for(Word j : wrongAnswers) {
                String str = j.eng + "\t" + j.kor;
                outfile.println(str);
            }
            System.out.println("오답노트가 만들어졌습니다.");
            i++;
        } catch (FileNotFoundException e) {
            System.out.println("오류");
        }
    }

    public void printAllWords() {
        for (Word word: this.voc) {
            System.out.println(word);
        }
    }

    public void deleteWord() {
        Word targetWord;

        System.out.print("삭제할 영단어를 입력하세요: ");
        targetWord = this.searchVoc(scan.nextLine().trim());

        if (targetWord == null) {
            System.out.println("단어를 찾을 수 없습니다.");
            return;
        }


        System.out.println(this.voc.remove(targetWord));
    }

    public void editWord() {
        Word targetWord;

        System.out.print("수정할 영단어를 입력하세요: ");
        targetWord = this.searchVoc(scan.nextLine().trim());

        if (targetWord == null) {
            System.out.println("단어를 찾을 수 없습니다.");
            return;
        }

        System.out.println("1) 영단어수정 2) 한글뜻수정");
        System.out.print("메뉴를 입력하세요: ");

        switch (scan.nextInt()) {
            case 1 -> {
                System.out.print("새로운 단어를 입력하세요: ");
                scan.nextLine();
                targetWord.setEng(scan.nextLine());
            }
            case 2 -> {
                System.out.print("새로운 뜻을 입력하세요: ");
                scan.nextLine();
                targetWord.setKor(scan.nextLine());
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

        for (Word word: this.voc) {
            if (word.eng.indexOf(sWord) == 0) {
                System.out.println(word);
            }
        }
    }

    public void searchVoc() {
        System.out.println("------ 단어 검색 ------");
        System.out.print("검색할 단어를 입력하세요 (영단어) : ");
        String sWord = scan.nextLine();

        for (Word s: this.voc) {
            if (s.eng.equals(sWord)) {
                System.out.println("단어의 뜻: " + s.kor);
            }
        }
    }

    public Word searchVoc(String target) {
        for (Word s: this.voc) {
            if (s.eng.equals(target)) {
                return s;
            }
        }

        return null;
    }

    public void fileWriter(PrintWriter outfile) {
        for (Word w : this.voc) {
            outfile.println(w.getEng() + "\t" + w.getKor());
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

    public void fileSave(){
        int choice = 0;
        String filename;

        System.out.println("1) 파일 덮어쓰기 2) 새로운 파일로 저장하기");
        System.out.print("선택하세요: ");
        choice = scan.nextInt();
        scan.nextLine();
        System.out.println();

        switch (choice) {
            case 1 -> this.vocToFile("res/words.txt");
            case 2-> {
                System.out.print("새 파일의 이름을 입력하세요: ");
                filename = scan.nextLine();
                filename = filename.trim().replaceAll("\\s+", "_");

                if (!(filename.indexOf(".txt") == filename.length() - 4))
                    filename = "res/"+filename+".txt";

                this.vocToFile(filename);
            }
            default -> System.out.println("입력이 잘못되었습니다.");
        }
    }

    public void fileLoad() {
        String filename;

        System.out.print("불러올 파일의 이름을 입력하세요: ");
        filename = scan.nextLine();
        filename = "res/"+filename+".txt";

        voc.clear();
        this.makeVoc(filename);
    }
}