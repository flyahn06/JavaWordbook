package api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
    static Pattern pattern = Pattern.compile("\"text\":\"(.*)\"");

    private URL url;
    private HttpURLConnection con;
    private String APIKEY;

    public Translator(String envPath) {
        try (Scanner scan = new Scanner(new File(envPath))) {
            this.APIKEY = scan.nextLine().trim();
            // connection 생성용 URL 객체
            this.url = new URL("https://api-free.deepl.com/v2/translate");
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다!");
            this.APIKEY = null;
            this.url = null;
            this.con = null;
        } catch (IOException e) {
            System.out.println("접속할 수 없습니다!");
            this.APIKEY = null;
            this.url = null;
            this.con = null;
        }
    }

    public Translator() {
        // Default API key location: res/.key
        this("res/.key");
    }

    /**
     * DeepL API의 response body로부터 의미를 파싱합니다.
     * @param response
     * DeepL API의 response body 전체
     * @return
     * Response body에서 추출된 단어의 뜻
     */
    private String parseMeaningFromResponse(String response) {
        Matcher matcher = pattern.matcher(response);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 파라미터로 주어진 단어의 뜻을 리턴합니다.
     * @param engWord
     * 뜻을 검색할 영단어
     * @return
     * 영단어의 뜻
     */
    public String getMeaning(String engWord) {
        if (this.APIKEY == null) return null;

        try {
            this.con = (HttpURLConnection) url.openConnection();
            // Request header 설정
            this.con.setRequestMethod("POST");
            this.con.setRequestProperty("Content-Type", "application/json");
            this.con.setRequestProperty("Authorization", "DeepL-Auth-Key " + this.APIKEY);
            this.con.setDoOutput(true);
        } catch (IOException e) {
            return null;
        }

        // Request body 구성
        // 일반적으로 적절한 sanitize 없이 유저의 입력을 JSON의 일부분으로 넣으면
        // JSON injection이 발생할 수 있기에 보안적으로 안전한 방법은 아니나,
        // 로컬에서는 JSON이 평가되자 않고 DeepL API가 지원하는 기능 중
        // credential을 리턴하는 기능은 없어 큰 문제가 되지 않는다고 생각했습니다.
        String jsonInputString = "{\"text\": [\"";
        jsonInputString += engWord;
        jsonInputString +="\"],\"target_lang\": \"KO\", \"source_lang\": \"EN\"}";

        // OutputStream을 통해 요청을 보냄
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            return null;
        }

        // response body를 line by line으로 읽어옴
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return this.parseMeaningFromResponse(response.toString());

        } catch (IOException e) {
            return null;
        }

    }
}