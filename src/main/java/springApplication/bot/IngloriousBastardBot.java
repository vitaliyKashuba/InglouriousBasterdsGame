package springApplication.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import springApplication.ibGame.IBGameMaster;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import springApplication.ibGame.IBPlayer;
import util.AppUtil;
import util.Randomizer;
import util.TgUtil;

@Component
public class IngloriousBastardBot extends TelegramLongPollingBot
{
    @Autowired
    private IBGameMaster gameMaster;

    private String botToken;

    IngloriousBastardBot()
    {
        botToken = AppUtil.getEnvironmentVariable("TOKEN");
    }

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
//                        gameMaster.addPlayer(new IBPlayer(senderId, senderName, IBPlayer.ClientType.TELEGRAM));
                        responceString = "welcome!";
                        break;
                    case "/init":
                        responceString = "Select game";
                        inlineKeyboardMarkup = TgUtil.getSelectGameKeyboardMarkup();
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
                        System.out.println("/debug");
                        responceString = "Select game";
                        inlineKeyboardMarkup = TgUtil.getSelectGameKeyboardMarkup();
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
                User sender = update.getCallbackQuery().getFrom();
                int senderId = sender.getId();
                String senderName = sender.getFirstName() + " " + sender.getLastName();

                String callback = update.getCallbackQuery().getData();

                switch(callback)
                {
                    case "start1":
                        gameMaster.startGame(senderId, IBGameMaster.GameMode.CLASSIC);
                        return;
                    case "start2":
                        gameMaster.startGame(senderId, IBGameMaster.GameMode.LIST);
                        return;
                    case "init_ib":
                        int room = gameMaster.initGame(senderId, senderName);
                        gameMaster.changeStatus(senderId, IBPlayer.Status.JOINED);
                        sendMsg(senderId, "Room " + String.valueOf(room) + " created!\nEnter character");
                        return;
                    default:
                        System.out.println("default switch");
                        break;

                }
            }
        }
    }

    public void sendMsg(long chatId, String msg)
    {
        sendMsg(chatId, msg, null);
    }

    public void sendMsg(long chatId, String msg, InlineKeyboardMarkup mk)
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
