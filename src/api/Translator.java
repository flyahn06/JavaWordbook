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
        this("res/.key");
    }

    private String parseMeaningFromResponse(String response) {
        Matcher matcher = pattern.matcher(response);
        return matcher.find() ? matcher.group(1) : null;
    }

    public String getMeaning(String engWord) {
        if (this.APIKEY == null) return null;

        try {
            this.con = (HttpURLConnection) url.openConnection();
            this.con.setRequestMethod("POST");
            this.con.setRequestProperty("Content-Type", "application/json");
            this.con.setRequestProperty("Authorization", "DeepL-Auth-Key " + this.APIKEY);
            this.con.setDoOutput(true);
        } catch (IOException e) {
            return null;
        }

        String jsonInputString = "{\"text\": [\"";
        jsonInputString += engWord;
        jsonInputString +="\"],\"target_lang\": \"KO\", \"source_lang\": \"EN\"}";

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            return null;
        }

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