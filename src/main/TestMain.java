package main;

import java.util.Scanner;

public class TestMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name, filePath;

        System.out.print("이름을 입력하세요: ");
        name = scanner.nextLine().trim();
        System.out.print("단어 파일 위치를 입력하세요[res/words.txt]: ");
        filePath = scanner.nextLine().trim();
        filePath = filePath.isEmpty() ? "res/words.txt" : filePath;

        VocManager manager = new VocManager(name);

        if (manager.makeVoc(filePath)) {
            manager.menu();
        }
    }
}
