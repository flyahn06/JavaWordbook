package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class VocManager {
    static Scanner scan = new Scanner(System.in);
    Vector<Word> voc = new Vector<>();
    String userName;

    VocManager(String userName) {
        this.userName = userName;
    }

    void addWord(String eng, String kor) {
        this.voc.add(new Word(eng, kor));
    }

    void makeVoc(String fileName) {
        try (Scanner file = new Scanner(new File(fileName))) {
            String line;
            String[] lineSplit;

            while (file.hasNextLine()) {
                line = file.nextLine();
                lineSplit = line.split("\t");
                this.addWord(lineSplit[0].trim(), lineSplit[1].trim());
            }

            System.out.printf("%s님의 단어장 생성완료\n", this.userName);
            this.menu();

        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다.");
        }
    }

    void menu() {
        int choice = 0;
        while (choice != 3) {
            System.out.println("\n------" + userName + "의 단어장 -------");
            System.out.println("1) 단어검색 2) 단어검색2 3) 종료");
            System.out.print("메뉴를 선택하세요 : ");
            choice = scan.nextInt();
            scan.nextLine();
            System.out.println();

            switch (choice) {
                case 1-> searchVoc();
                case 2-> searchVoc2();
                case 3-> System.out.println(userName + "의 단어장 프로그램을 종료합니다.");
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
}

