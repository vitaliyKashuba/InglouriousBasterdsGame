package web;

import bot.IngloriousBastardBot;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
public class Main
{

    public static void main(String[] args) throws Exception {

        SpringApplication.run(Main.class, args);

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try
        {
            botsApi.registerBot(new IngloriousBastardBot());
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }
}
