package springApplication.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import springApplication.game.EGame;
import springApplication.game.RoomsKeeper;
import springApplication.ibGame.IBGameMaster;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import springApplication.spyfallGame.SpyfallGameMaster;
import util.AppUtil;
import util.GoogleSearchAPIUtil;
import util.TgUtil;

@Component
public class IngloriousBastardBot extends TelegramLongPollingBot
{
    @Autowired
    private RoomsKeeper roomsKeeper;

    @Autowired
    private UserStateSaver stateSaver;

    @Autowired
    private IBGameMaster ibGameMaster;

    @Autowired
    private SpyfallGameMaster spyfallGameMaster;

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
                        stateSaver.setStatus(senderId, UserStateSaver.Status.JOINREQUEST);
                        responceString = "enter room number";
                        break;
                    case "/help":
                        responceString = "help";
                        break;
                    case "/debug":
                        System.out.println("/debug");
                        sendImageFromUrl(senderId, GoogleSearchAPIUtil.findImage("darth vader"));
                        break;
                    default:
                        responceString = "unrecognized command";
                        break;
                }

            } else
            {
                switch (stateSaver.getStatus(senderId))
                {
                    case NEW_PLAYER:
                        responceString = "use commands /";
                        break;
                    case JOINREQUEST:
                        try
                        {
                            int roomToJoin = Integer.parseInt(receivedMessage);
                            switch(roomsKeeper.getGameByRoomId(roomToJoin))
                            {
                                case INGLORIOUS_BASTERDS:
                                    ibGameMaster.join(senderId, senderName, roomToJoin);
                                    stateSaver.setPlayersGame(senderId, EGame.INGLORIOUS_BASTERDS);
                                    stateSaver.setStatus(senderId, UserStateSaver.Status.JOINED);
                                    responceString = "enter character";
                                    break;
                                case SPYFALL:
                                    spyfallGameMaster.join(senderId, senderName, roomToJoin);
                                    stateSaver.setPlayersGame(senderId, EGame.SPYFALL);
                                    stateSaver.setStatus(senderId, UserStateSaver.Status.JOINED);
                                    responceString = "waiting for game start";
                                    break;
                            }
                        } catch (NumberFormatException e)
                        {
                            responceString = "Enter valid room number";
                            break;
                        } catch (IllegalArgumentException e)
                        {
                            responceString = "Room not exist";
                            break;
                        }
                        break;
                    case JOINED:
                        switch(stateSaver.getPlayersGame(senderId))
                        {
                            case INGLORIOUS_BASTERDS:
                                ibGameMaster.setCharacter(senderId, receivedMessage);
                                responceString = "waiting for party ready";
                                if (ibGameMaster.isAdmin(senderId))
                                {
                                    int adminRoomId = ibGameMaster.getAdminRoomId(senderId);
                                    responceString += ("\nSelect mode to start game for room " + adminRoomId);
                                    inlineKeyboardMarkup = TgUtil.getStartIBKeyboardMarkup();
                                }
                                break;
                            case SPYFALL:
                                if (spyfallGameMaster.isAdmin(senderId))
                                {
                                    try
                                    {
                                        int limit = Integer.parseInt(receivedMessage);
                                        spyfallGameMaster.setLocationsLimit(senderId, limit);
                                    } catch (NumberFormatException e)
                                    {
                                        responceString = "Enter valid number";
                                        break;
                                    } catch (IllegalArgumentException e)
                                    {
                                        responceString = "limit too small to apply";
                                        break;
                                    }
                                    responceString = "locations limit set";
                                } else
                                {
                                    responceString = "waiting for game start";
                                }
                                break;
                        }
                        stateSaver.setStatus(senderId, UserStateSaver.Status.READY);
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
                        break;
                    case "start2":  // start ib in list mode
                        ibGameMaster.startGame(senderId, IBGameMaster.GameMode.LIST);
                        break;
                    case "init_ib":
                        int room = ibGameMaster.initGame(senderId, senderName);
                        stateSaver.setPlayersGame(senderId, EGame.INGLORIOUS_BASTERDS);
                        stateSaver.setStatus(senderId, UserStateSaver.Status.JOINED);
                        sendMsg(senderId, "Room " + room + " created!\nEnter character");
                        break;
                    case "init_spyfall":
                        System.out.println("init spyfall");
                        room = spyfallGameMaster.initGame(senderId, senderName);
                        stateSaver.setPlayersGame(senderId, EGame.SPYFALL);
                        stateSaver.setStatus(senderId, UserStateSaver.Status.JOINED);
                        String message = "Room " + room + " created!\n" +
                                "Wait for party and press start button to start\n" +
                                "You can delimit locations by entering number";
                        InlineKeyboardMarkup inlineKeyboardMarkup = TgUtil.getStartSpyfallKeyboardMarkup();
                        sendMsg(senderId, message, inlineKeyboardMarkup);
                        break;
                    case "start_spyfall":
                        spyfallGameMaster.startGame(senderId);
                        break;
                    default:
                        System.out.println("default switch, smth wrong");
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

    public void sendImageFromUrl(int chatId, String url)
    {
        sendImageFromUrl(chatId, url, null);
    }

    public void sendImageFromUrl(int chatId, String url, String caption) {
        SendPhoto sendPhotoRequest = new SendPhoto()
                .setChatId(String.valueOf(chatId))
                .setPhoto(url);
        if (caption != null)
        {
            sendPhotoRequest.setCaption(caption);
        }
        try
        {
            execute(sendPhotoRequest);
        } catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

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
