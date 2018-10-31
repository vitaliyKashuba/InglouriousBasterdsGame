package springApplication.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import springApplication.game.IBGameMaster;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import springApplication.game.IBPlayer;
import util.AppUtil;
import util.GoogleSearchAPIUtil;
import util.Randomizer;
import util.TgUtil;


import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IngloriousBastardBot extends TelegramLongPollingBot
{
    @Autowired
    private IBGameMaster gameMaster;

//    @Value("${bot.token}")
    private String botToken;

//    @Value("${bot.username}")
//    private String username;

    IngloriousBastardBot()
    {
        botToken = AppUtil.getEnvironmentVariable("TOKEN");
    }

//    @PostConstruct
//    public void start() {
//        System.out.println("username: " + username + " token: " + botToken);
//    }

    //TODO check nulls, user data. refactor
    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String receivedMessage = update.getMessage().getText();

            String responceString = "";
            InlineKeyboardMarkup inlineKeyboardMarkup = null;

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
                        gameMaster.removeOldRoomIfExist(senderId);
                        responceString = "enter room number";
                        break;
                    case "/help":
                        responceString = "help";
                        break;
                    case "/debug":
                        responceString = Randomizer.getRandomCharacter();
                        System.out.println("/debug");
//                        inlineKeyboardMarkup = TgUtil.getStartGameKeyboardMarkup();
//                        String img = GoogleSearchAPIUtil.findImage(responceString);
//                        sendImageFromUrl(update.getMessage().getChatId(), img, responceString);
//                        return;
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
                        try
                        {
                            gameMaster.enterRoom(senderId, Integer.parseInt(receivedMessage));
                        } catch (NumberFormatException e)
                        {
                            responceString = "Enter valid room number";
                            break;
                        } catch (IllegalArgumentException e)
                        {
                            responceString = "Room not exist";
                            break;
                        }
                        gameMaster.changeStatus(senderId, IBPlayer.Status.JOINED);
                        responceString = "enter character";
                        break;
                    case JOINED:
                        gameMaster.setCharacter(senderId, receivedMessage);
                        gameMaster.changeStatus(senderId, IBPlayer.Status.READY);
                        responceString = "waiting for party ready";
                        if (gameMaster.isAdmin(senderId))
                        {
                            int adminRoomId = gameMaster.getAdminRoomId(senderId);
                            responceString += ("\nSelect mode to start springApplication.game for room " + String.valueOf(adminRoomId));
                            inlineKeyboardMarkup = TgUtil.getStartGameKeyboardMarkup();
                        }
                        break;
                }
            }

            if (responceString.length() > 0)
            {
                sendMsg(update.getMessage().getChatId(), responceString, inlineKeyboardMarkup);
            }

        } else {
            if (update.hasCallbackQuery())
            {
                int senderId = update.getCallbackQuery().getFrom().getId();
                String callback = update.getCallbackQuery().getData();
                List<IBPlayer> players = null;

                if(callback.startsWith("start"))
                {
                    int roomId = gameMaster.randomizeCharacters(senderId);
                    players = gameMaster.getPlayersByRoomCreator(senderId);

                    gameMaster.startGame(roomId);
                }

                switch(callback)
                {
                    case "start1":
                        for (IBPlayer p : players)
                        {
                            String ch = p.getCharacter();
                            String img = GoogleSearchAPIUtil.findImage(p.getCharacter());
//                            sendImageFromUrl(p.getId(), img, ch);                                                       // TODO remake to telegram bots  v 4.1
                        }
                        return;
                    case "start2":
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
                        return;
                    default:
                        System.out.println("default switch");
                        break;

                }
            }
        }
    }

    private void sendMsg(long chatId, String msg)
    {
        sendMsg(chatId, msg, null);
    }

    private void sendMsg(long chatId, String msg, InlineKeyboardMarkup mk)
    {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(msg);

        if (mk != null)
        {
            message.setReplyMarkup(mk);
        }

        try
        {
            execute(message);
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

//    private void sendImageFromUrl(long chatId, String imgUrl, String caption) {
//        SendPhoto sendPhotoRequest = new SendPhoto()
//                .setChatId(chatId)
//                .setPhoto(imgUrl);
//        if (caption != null)
//        {
//            sendPhotoRequest.setCaption(caption);
//        }
//        try
//        {
//            sendPhoto(sendPhotoRequest);
//        } catch (TelegramApiException e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Override
    public String getBotUsername()
    {
        return "IngloriousBasterdsBot";
    }

    @Override
    public String getBotToken()
    {
        return botToken;
    }

}
