package bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.Superhero;
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
            System.out.println("error2");
        }


//        faker.lordOfTheRings().character();
//        faker.harryPotter().character()
//        faker.hobbit().character()
//
//        Faker faker = new Faker();
//        for (int i = 0; i < 25; i++)
//        {
////            Superhero s = faker.superhero();
//            System.out.println(faker.);
//        }

    }
}
