package handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WeatherHandler {

    private static Map<String, String> cities = null;
    static {
        String fileName = "C:\\Users\\Sergey\\IdeaProjects\\TelegramBot\\src\\cities.csv";
        cities = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));){
            String city = bufferedReader.readLine();
            while (city != null) {
                cities.put(city.split(";")[1].toUpperCase(Locale.ROOT),
                        city.split(";")[0]);
                city = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> getCities() {
        return cities;
    }


    public static void setCities(Map<String, String> cities) {
        WeatherHandler.cities = cities;
    }
}
