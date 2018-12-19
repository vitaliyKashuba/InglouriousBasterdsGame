package springApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
//import org.telegram.telegrambots.starter.EnableTelegramBots;

@SpringBootApplication
//@EnableTelegramBots
public class Main
{

    public static void main(String[] args)
    {
        ApiContextInitializer.init();

//        TelegramBotsApi botsApi = new TelegramBotsApi();

        SpringApplication.run(Main.class, args);

//        ApiContextInitializer.init();

//        try
//        {
//            botsApi.registerBot(bot);
//        } catch (TelegramApiException e)
//        {
//            e.printStackTrace();
//        }
    }
}
