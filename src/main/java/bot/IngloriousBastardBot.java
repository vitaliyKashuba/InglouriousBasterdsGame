package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import util.AppUtil;
import util.GoogleSearchAPIUtil;
import util.Randomizer;
import util.TgUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngloriousBastardBot extends TelegramLongPollingBot
{
    private IBGameMaster gameMaster;
    private static String botToken;

    public IngloriousBastardBot()
    {
        gameMaster = IBGameMaster.getInstance();
        botToken = AppUtil.getEnvironmentVariable("TOKEN");
    }

    //TODO check nulls, user data. refactor
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
                            responceString += ("\nSelect mode to start game for room " + String.valueOf(adminRoomId));
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
                    gameMaster.randomizeCharacters(senderId);
                    players = gameMaster.getPlayersByRoomCreator(senderId);
                }

                switch(callback)
                {
                    case "start1":
                        for (IBPlayer p : players)
                        {
                            String ch = p.getCharacter();
                            String img = GoogleSearchAPIUtil.findImage(p.getCharacter());
                            sendImageFromUrl(p.getId(), img, ch);
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

    private void sendImageFromUrl(long chatId, String imgUrl, String caption) {
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
