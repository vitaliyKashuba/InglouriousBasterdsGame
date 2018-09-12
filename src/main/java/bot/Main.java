package bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import util.AppUtil;

public class Main
{
    public static void main(String args[])
    {
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
