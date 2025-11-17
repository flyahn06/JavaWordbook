package main;

import api.Translator;

import java.util.Objects;
import java.util.Vector;

public class Word {
    String eng;
    Vector<String> kor;
    int ranking;
    Translator translator;

    public Word(String eng, String kor, String ranking, Translator translator) {
        super();
        this.eng = eng;
        this.kor = new Vector<>();
        this.ranking = Integer.parseInt(ranking);
        this.translator = translator;
        this.setKor(kor);
    }

    public String getEng() { return eng; }

    public String getKor() {
        String str = "";

        if (this.kor.isEmpty()) return str;

        for (int i = 0; i < this.kor.size() - 1; i++) {
            str += this.kor.get(i) + ", ";
        }

        str += this.kor.getLast();

        return str;
    }

    public int getRanking() { return ranking; }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public void setKor(String kor) {
        if (kor.equals("?") || kor.isEmpty()) {
            Vector<String> meaning = new Vector<>();
            meaning.add(this.translator.getMeaning(this.getEng()));
            this.kor = meaning;
            System.out.println("검색된 단어 뜻: " + meaning.get(0));
            return;
        }
        for (String k: kor.split(",")) {
            this.kor.add(k.trim());
        }
    }


    public void setRanking(int ranking) { this.ranking = ranking; }

    @Override
    public String toString() {
        return this.getEng() + " : " + this.getKor();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String otherEng) {
            return otherEng.equals(this.eng);
        } else if (obj instanceof Word word) {
            return word.eng.equals(this.eng);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eng, kor);
    }
}
