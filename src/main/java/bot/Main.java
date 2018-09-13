package bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import util.AppUtil;
import util.GoogleSearchAPIUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Main
{
    public static void main(String args[]) throws IOException
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

//        List<String> images = GoogleSearchAPIUtil.searchForImages("darth vader");
//
//        for (String img : images)
//        {
//            System.out.println(img);
//        }

    }
}
