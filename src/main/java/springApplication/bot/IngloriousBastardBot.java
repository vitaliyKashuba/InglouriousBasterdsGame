package springApplication.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import springApplication.game.Player;
import springApplication.ibGame.IBGameMaster;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import util.AppUtil;
import util.TgUtil;

@Component
public class IngloriousBastardBot extends TelegramLongPollingBot
{
    @Autowired
    private IBGameMaster ibGameMaster;

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
                        responceString = "welcome!";
                        break;
                    case "/init":
                        responceString = "Select game";
                        inlineKeyboardMarkup = TgUtil.getSelectGameKeyboardMarkup();
                        break;
                    case "/join":
                        //TODO
                        // get game type from room keeper
                        // add somewhere ?
                        ibGameMaster.addPlayerIfNull(senderId, senderName);
                        ibGameMaster.changeStatus(senderId, Player.IBStatus.JOINREQUEST);
                        ibGameMaster.removeOldRoomIfExist(senderId);
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
                //TODO separate games
                Player player = ibGameMaster.getPlayer(senderId);
                switch (player.getIbStatus())
                {
                    case NONE:
                        responceString = "use commands /";
                        break;
                    case JOINREQUEST:   // TODO get game type from room keeper
                        try
                        {
                            ibGameMaster.enterRoom(senderId, Integer.parseInt(receivedMessage));
                        } catch (NumberFormatException e)
                        {
                            responceString = "Enter valid room number";
                            break;
                        } catch (IllegalArgumentException e)
                        {
                            responceString = "Room not exist";
                            break;
                        }
                        ibGameMaster.changeStatus(senderId, Player.IBStatus.JOINED);
                        responceString = "enter character";
                        break;
                    case JOINED:
                        ibGameMaster.setCharacter(senderId, receivedMessage);
                        ibGameMaster.changeStatus(senderId, Player.IBStatus.READY);
                        responceString = "waiting for party ready";
                        if (ibGameMaster.isAdmin(senderId))
                        {
                            int adminRoomId = ibGameMaster.getAdminRoomId(senderId);
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
                    case "start1":  // start ib game in classic mode
                        ibGameMaster.startGame(senderId, IBGameMaster.GameMode.CLASSIC);
                        return;
                    case "start2":  // start ib in list mode
                        ibGameMaster.startGame(senderId, IBGameMaster.GameMode.LIST);
                        return;
                    case "init_ib":
                        int room = ibGameMaster.initGame(senderId, senderName);
                        ibGameMaster.changeStatus(senderId, Player.IBStatus.JOINED);
                        sendMsg(senderId, "Room " + room + " created!\nEnter character");
                        return;
                    case "init_spyfall":
                        // TODO init spyfall
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
