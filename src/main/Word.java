package main;

public class Word {
    String eng;
    String kor;

    public Word(String eng, String kor) {
        super();
        this.eng = eng;
        this.kor = kor;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }

    @Override
    public String toString() {
        return eng + " : " + kor;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String otherEng) {
            return otherEng.equals(this.eng);
        }
        return false;
    }
}
