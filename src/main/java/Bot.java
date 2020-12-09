import Weather.Weather;
import handlers.WeatherHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Bot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "seregin_java_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = "";

        if (update.hasMessage()) {
            if (update.getMessage().equals("Погода")) {
                sendMsg(update.getMessage().getChatId().toString(), "Напишите название города:");
            }
        }
        //TODO сделать так, чтобы это условие работало только если было написано Погода (кнопка)
        String cityName = update.getMessage().getText().toUpperCase(Locale.ROOT);
        if (WeatherHandler.getCities().containsKey(cityName)) {
            message = "https://yandex.ru/pogoda/" + WeatherHandler.getCities().get(cityName);
            Weather weather = new Weather(message);
            try {
                sendMsg(update.getMessage().getChatId().toString(),
                        weather.weatherFromSite() + "\n" + "https://yandex.ru/pogoda/"
                                + WeatherHandler.getCities().get(cityName));
            } catch (IOException e) {
                sendMsg(update.getMessage().getChatId().toString(), "Такого города нет");
            }
        }

    }


    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        setButtons(sendMessage);
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

    public synchronized void setButtons(SendMessage sendMessage) {

        // Создаем клавиуатуру

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Погода"));
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }
}
