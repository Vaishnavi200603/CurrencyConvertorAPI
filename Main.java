package org.example;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean start = true;

        HashMap<Integer, String> map = new HashMap<>();

        map.put(1, "INR");
        map.put(2, "USD");
        map.put(3, "EUR");
        map.put(4, "CAD");
        map.put(5, "JPY");
        map.put(6, "CNY");

        do{
            int from, to;
            String fromCurr, toCurr;

            System.out.println("Currency Converting From??");
            System.out.println("1:INR \t 2:USD \t 3:EUR \t 4:CAD \t 5:JPY \t 6:CNY");
            from = sc.nextInt();
            while(from < 1 || from > 6){
                System.out.println("Please Select Valid Number (1-6)");
                System.out.println("1:INR \t 2:USD \t 3:EUR \t 4:CAD \t 5:JPY \t 6:CNY");
                from = sc.nextInt();
            }
            fromCurr = map.get(from);

            System.out.println("Currency Converting To??");
            System.out.println("1:INR \t 2:USD \t 3:EUR \t 4:CAD \t 5:JPY \t 6:CNY");
            to = sc.nextInt();
            while(to < 1 || to > 6){
                System.out.println("Please Select Valid Number (1-6)");
                System.out.println("1:INR \t 2:USD \t 3:EUR \t 4:CAD \t 5:JPY \t 6:CNY");
                to = sc.nextInt();
            }
            toCurr = map.get(to);

            //Amount
            System.out.print("Enter Amount To Convert : ");
            float amount = sc.nextFloat();

            //Method that convert the currency with the help of API
            currencyConverter(fromCurr, toCurr, amount);

            System.out.println("Would You Like To Make Another Conversion?");
            System.out.println("1:YES \t Any Other Number:NO");
            if(sc.nextInt() != 1){
                start = false;
            }
        }while(start);

        System.out.println("THANK-YOU FOR USING CURRENCY CONVERTER");
    }

    public static void currencyConverter(String fromCurr, String toCurr, float amount) throws IOException {
        DecimalFormat f = new DecimalFormat("00.00");
        //app.freecurrenecyapi.com
        String get_URL =
                "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_IA8WluWGDsse5vFkhWqAiRntZk6TY0qTf53nuHY2&currencies=" +
                toCurr + "&base_currency=" + fromCurr + "&amount=" + amount;
        URL url = new URL(get_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        //if success
        if(responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }in.close();

           // System.out.println("Response : " + response);

            JSONObject object = new JSONObject(response.toString());
            Double exchangeRate = object.getJSONObject("data").getDouble(toCurr);

            System.out.println(object.getJSONObject("data"));
            //for debugging purpose
            System.out.println(exchangeRate);
            System.out.println();
            System.out.println(f.format(amount) + fromCurr + " = " + f.format(amount/exchangeRate) + toCurr);
        }
        else{
            System.out.println("GET REQUEST FAILED !!!");
        }
    }
}
