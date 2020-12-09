import Weather.Weather;
import handlers.WeatherHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Bot extends TelegramLongPollingBot {

    private static String prefixMes = "";
    private static String lastPrefixMes = "";
    private static final String WEATHER = "Weather";
    private static final String NEWS = "News";
    private static final String EXIT = "Exit";
    static List<String> commands = new ArrayList<>();

    static {
        commands.add(WEATHER);
        commands.add(NEWS);
        commands.add(EXIT);
    }



    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Message: " + update.getMessage().getText()
                            + " prefix: " + prefixMes);
        Message mes = update.getMessage();
        String chatId = update.getMessage().getChatId().toString();
        if (commands.contains(mes.getText())) {
            prefixMes = mes.getText();
        }

        if (prefixMes.equals("")) {
            sendMsg(chatId, "Выберите из списка ниже DEFAULT");
        }

        if (prefixMes.equals(WEATHER)) {
            prefixMes = WEATHER;
            sendMsg(chatId, "Напишите название города или нажмите exit ВЕЗЕР");
            weatherAnswer(update, chatId);
        }

        if (prefixMes.equals(NEWS)) {
            prefixMes = NEWS;
            sendMsg(chatId, "Напишите название города или нажмите exit НЬЮС");
            newsAnswer(update, chatId);
        }
        if (prefixMes.equals(EXIT)) {
            sendMsg(chatId, "Выберите из списка ниже");
            prefixMes = "";
        }


    }

    public String switchForCases(Message mes, String chatId) {
        switch (prefixMes) {
            case (WEATHER):
                sendMsg(chatId, "Напишите название города или нажмите exit ВЕЗЕР");
                prefixMes = WEATHER;
                break;
            case (NEWS):
                sendMsg(chatId, "Напишите название города или нажмите exit НЬЮС");
                prefixMes = NEWS;
                break;
            case (EXIT):
                sendMsg(chatId, "Выберите из списка ниже EXIT");
                prefixMes = "";
                break;
            default:
                sendMsg(chatId, "Выберите из списка ниже DEFAULT");
                prefixMes = "";
                break;
        }
        return prefixMes;
    }


    private synchronized void weatherAnswer(Update update, String chatId) {
        String cityName = update.getMessage().getText().toUpperCase(Locale.ROOT);
        System.out.println("WEATHERANSWER " + cityName);
        if (WeatherHandler.getCities().containsKey(cityName)) {
            String URL = "https://yandex.ru/pogoda/" + WeatherHandler.getCities().get(cityName);
            Weather weather = new Weather(URL);
            try {
                sendMsg(chatId, weather.weatherFromSite() + "\n" + URL);
            } catch (IOException e) {
                sendMsg(chatId, "Такого города нет");
            }
        }
    }

    private synchronized void newsAnswer(Update update, String chatId) {
        String cityName = update.getMessage().getText().toUpperCase(Locale.ROOT);
        if (WeatherHandler.getCities().containsKey(cityName)) {
            String URL = "https://yandex.ru/pogoda/" + WeatherHandler.getCities().get(cityName);
            Weather weather = new Weather(URL);
            try {
                sendMsg(chatId, weather.weatherFromSite() + "\n" + URL);
            } catch (IOException e) {
                sendMsg(chatId, "Такого города нет");
            }
        }
    }


    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        if (prefixMes.equals("")) {
            Buttons.setButtonsMain(sendMessage);
        }
        else if (prefixMes.equals(WEATHER)) {
            Buttons.setButtonsWeather(sendMessage);
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public String getBotToken() {
        return "1489982502:AAGTOf5eY3oOFx3Ou3gEMbolNJVXRlfQisc";
        //Токен бота
    }

    @Override
    public String getBotUsername() {
        return "seregin_java_bot";
    }




}
