package com.kopicat.myapplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TestJava {

    public static void main(String[] args) {
        System.out.println("Hello");
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

        String urlString2 = "https://api.telegram.org/bot%s/getUpdates";

        String apiToken = "888933117:AAHQsKd0FUXU7DDi0ayrE5X1rJyYGBpY4Js";
        String chatId = "-1001353974889";
        String text = "Helloworld!";

        urlString = String.format(urlString, apiToken, chatId, text);
        urlString2 = String.format(urlString2, apiToken);
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            String response = sb.toString();
// Do what you want with response
            System.out.println(response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
