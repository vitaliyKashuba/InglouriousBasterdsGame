package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Contact;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import util.AppUtil;
import util.GoogleSearchAPIUtil;
import util.Randomizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngloriousBastardBot extends TelegramLongPollingBot
{
    private IBGameMaster gameMaster;
    private Map<String, String> env;
    private static String botToken;

    public IngloriousBastardBot()
    {
        gameMaster = IBGameMaster.getInstance();
        botToken = AppUtil.getEnvironmentVariable("TOKEN");
    }

    //TODO check nulls, user data. refactor
    //TODO split to handleCommands() amd handleMessages()
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String receivedMessage = update.getMessage().getText();
            String responceString = "";

            User sender = update.getMessage().getFrom();
            int senderId = sender.getId();
            String senderName = sender.getFirstName() + " " + sender.getLastName();

            if (receivedMessage.startsWith("/"))
            {
                switch(receivedMessage) //TODO add /stats command
                {
                    case "/start":
                        gameMaster.addPlayer(new IBPlayer(senderId, senderName));
                        responceString = "welcome!";
                        break;
                    case "/init":
                        int room = gameMaster.initGame(senderId, senderName);
                        gameMaster.changeStatus(senderId, IBPlayer.Status.JOINED);
                        responceString = "Room " + String.valueOf(room) + " created!\nEnter character";
                        break;
                    case "/join":
                        gameMaster.addPlayerIfNull(senderId, senderName);
                        gameMaster.changeStatus(senderId, IBPlayer.Status.JOINREQUEST);
                        responceString = "enter room number";
                        break;
                    case "/help":
                        responceString = "help";
                        break;
                    case "/go":
                        if (gameMaster.isAdmin(senderId))
                        {
                            gameMaster.randomizeCharacters(senderId);
                            List<IBPlayer> players = gameMaster.getPlayersByRoomCreator(senderId);
                            for (IBPlayer p : players)
                            {
//                                responceString = p.getCharacter();
//                                sendMsg(p.getId(), responceString);
                                String ch = p.getCharacter();
                                List<String> images = GoogleSearchAPIUtil.searchForImages(ch);
                                String img = images.get(Randomizer.getRandomIndex(images.size()));
                                sendImageFromUrl(p.getId(), img, ch);
                            }

                            return;
                        }
                        else {
                            responceString = "only room creators allows run this command";
                        }
                        break;
                    case "/debug":
                        gameMaster.randomizeCharacters(senderId);
                        List<IBPlayer> players = gameMaster.getPlayersByRoomCreator(senderId);
                        for (IBPlayer p : players)
                        {
                            Map<String,String> teammates = new HashMap<>();
                            for (IBPlayer pl : players)
                            {
                                if(pl.getId() != p.getId())
                                {
                                    teammates.put(pl.getName(), pl.getCharacter());
                                }
                            }
                            sendMsg(p.getId(), teammates.toString());
                        }
                        break;
                    default:
                        responceString = "unrecognized command";
                        break;
                }

            } else
            {
                IBPlayer player = gameMaster.getPlayer(senderId);
                switch (player.getStatus())
                {
                    case NONE:
                        responceString = "use commands /";
                        break;
                    case JOINREQUEST:
                        gameMaster.enterRoom(senderId, Integer.parseInt(receivedMessage));
                        gameMaster.changeStatus(senderId, IBPlayer.Status.JOINED);
                        responceString = "enter character";
                        break;
                    case JOINED:
                        gameMaster.setCharacter(senderId, receivedMessage);
                        gameMaster.changeStatus(senderId, IBPlayer.Status.READY);
                        responceString = "waiting for party ready";
                        break;
                }
            }

            if (responceString.length() > 0)
            {
                sendMsg(update.getMessage().getChatId(), responceString);
            }
        }
    }

    private void sendMsg(long chatId, String msg)
    {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);
        try
        {
            execute(message);
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public void sendImageFromUrl(long chatId, String imgUrl, String caption) {
        SendPhoto sendPhotoRequest = new SendPhoto()
                .setChatId(chatId)
                .setPhoto(imgUrl);
        if (caption != null)
        {
            sendPhotoRequest.setCaption(caption);
        }
        try
        {
            sendPhoto(sendPhotoRequest);
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    public String getBotUsername()
    {
        return "IngloriousBasterdsBot";
    }

    public String getBotToken()
    {
        return botToken;
    }

}
