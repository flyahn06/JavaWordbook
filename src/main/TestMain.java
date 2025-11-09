package main;

public class TestMain {
    public static void main(String[] args) {
        VocManager manager = new VocManager("홍길동");
        manager.makeVoc("res/words.txt");
    }
}
