package springapp.bot;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import springapp.db.service.CharacterStatsService;
import springapp.db.service.DbLoggerService;
import springapp.game.EGame;
import springapp.game.RoomsKeeper;
import springapp.ibGame.IBGameMaster;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import springapp.mafiaGame.MafiaGameMaster;
import springapp.spyfallGame.SpyfallGameMaster;
import util.AppUtil;
import util.Convertor;
import util.TgUtil;

import java.io.File;
import java.util.List;

import static util.TgUtil.Callbacks;

@Slf4j
@Component
public class IngloriousBastardBot extends TelegramLongPollingBot {
    @Autowired
    private RoomsKeeper roomsKeeper;

    @Autowired
    private UserStateSaver stateSaver;

    @Autowired
    private IBGameMaster ibGameMaster;

    @Autowired
    private SpyfallGameMaster spyfallGameMaster;

    @Autowired
    private MafiaGameMaster mafiaGameMaster;

    @Autowired
    private DbLoggerService dbLogger;

    @Autowired
    private CharacterStatsService statsService;

    private final String botToken;

    IngloriousBastardBot() {
        botToken = AppUtil.getEnvironmentVariable("TOKEN");
    }

    //TODO try to refactor?
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage()
                .hasText())                                                       // text message handling
        {
            String receivedMessage = update.getMessage().getText();

            String responceString = "";
            InlineKeyboardMarkup inlineKeyboardMarkup = null;

            User sender = update.getMessage().getFrom();
            int senderId = sender.getId();
            String senderName = sender.getFirstName() + " " + sender.getLastName();

            if (receivedMessage.startsWith("/"))                                                                        // slash-started commands parsing
            {
                switch (receivedMessage) {
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
                    case "/qr":
                        sendMsg(senderId, "menu", TgUtil.getMainMenuKeyboardMarkup());
                        break;
                    case "/debug":
                        System.out.println("/debug");
                        //                        pinMessage(senderId);
                        break;
                    case "/stats":
                        responceString = statsService.getStatsInTgReadebleFormat();
                        break;
                    default:
                        responceString = "unrecognized command";
                        break;
                }

            } else                                                                                                      // end of slash-started commands parsing
            {                                                                                                           // plain text parsing
                switch (stateSaver.getStatus(senderId)) {
                    case NEW_PLAYER:
                        responceString = "use commands /";
                        break;
                    case JOINREQUEST:                                                                                   // after '/join' command plain text goes here
                        try {
                            int roomToJoin = Integer.parseInt(receivedMessage);
                            switch (roomsKeeper.getGameByRoomId(roomToJoin)) {
                                case INGLORIOUS_BASTERDS:                                                               // join into IB game
                                    ibGameMaster.join(senderId, senderName, roomToJoin);
                                    setJoined(senderId, EGame.INGLORIOUS_BASTERDS);
                                    responceString = "enter character";
                                    break;
                                case SPYFALL:                                                                           // join into Spyfall
                                    spyfallGameMaster.join(senderId, senderName, roomToJoin);
                                    setJoined(senderId, EGame.SPYFALL);
                                    responceString = "waiting for game start";
                                    break;
                                case MAFIA:                                                                             // join into mafia
                                    mafiaGameMaster.join(senderId, senderName, roomToJoin);
                                    setJoined(senderId, EGame.MAFIA);
                                    responceString = "waiting for game start";
                                    break;
                                default:
                                    log.error("default switch while entering room");
                                    break;
                            }
                            dbLogger.log(senderName + " joined room " + roomToJoin, "INFO", "onUpdateReceived");
                        } catch (NumberFormatException e)                                                               // user send text to room number request
                        {
                            responceString = "Enter valid room number";
                            dbLogger.log(senderName + " enters unparseble room number ", "WARN");
                            break;
                        } catch (IllegalArgumentException e)                                                            // can't enter room. threw by BasicGameMaster.enterRoom method
                        {
                            responceString = "Room not exist";
                            dbLogger.log(senderName + " enters unexisting room number ", "WARN");
                            break;
                        }
                        break;
                    case JOINED:                                                                                        // after player enter room number and sucessfully joined game plain text goes here
                        switch (stateSaver.getPlayersGame(senderId)) {
                            case INGLORIOUS_BASTERDS:                                                                   // setting IB character
                                ibGameMaster.setCharacter(senderId, receivedMessage);
                                responceString = "waiting for party ready";
                                if (ibGameMaster.isAdmin(senderId)) {
                                    int adminRoomId = ibGameMaster.getAdminRoomId(senderId);
                                    responceString = "Select mode to start game for room " + adminRoomId;
                                    inlineKeyboardMarkup = TgUtil.getStartIBKeyboardMarkup();
                                }
                                break;
                            case SPYFALL:
                                if (spyfallGameMaster.isAdmin(senderId)) {                                                                                       // admin can set locations limit of spyfall here
                                    try {
                                        int limit = Integer.parseInt(receivedMessage);
                                        spyfallGameMaster.setLocationsLimit(senderId, limit);
                                    } catch (NumberFormatException e) {
                                        responceString = "Enter valid number";
                                        break;
                                    } catch (IllegalArgumentException e) {
                                        responceString = "limit too small to apply";
                                        break;
                                    }
                                    responceString = "locations limit set";
                                } else {
                                    responceString = "waiting for game start";
                                }
                                break;
                            default:
                                log.error("default switch while parsing joined players msg");
                                dbLogger.log("default switch while parsing joined players msg",
                                             "ERROR",
                                             "onUpdateReceived");
                                break;
                        }
                        stateSaver.setStatus(senderId, UserStateSaver.Status.READY);
                        break;
                    case MAFIA_SET_ROLES:                                                                               // admin add new non-standart role
                        mafiaGameMaster.addRole(senderId, receivedMessage);
                        String message = mafiaGameMaster.getPlayersCount(senderId) + " players joined. Roles:\n" +
                                         Convertor.convertListForTelegram(mafiaGameMaster.getRoles(senderId));
                        sendMsg(senderId, message, TgUtil.getAllRolesButtonsForMafiaKeyboardMarkup());
                        break;
                    default:
                        log.error("default switch in message parsing, suppose it's error here");
                        break;
                }
            }                                                                                                           // end of plain text parsing

            if (responceString.length() > 0) {
                sendMsg(update.getMessage().getChatId(), responceString, inlineKeyboardMarkup);
            }

        } else {                                                                                                               // end of text message handling
            if (update.hasCallbackQuery())                                                                              // calback handling
            {
                User sender = update.getCallbackQuery().getFrom();
                int senderId = sender.getId();
                String senderName = sender.getFirstName() + " " + sender.getLastName();

                String callback = update.getCallbackQuery().getData();

                switch (callback) {
                    case Callbacks.START_IB_CLASSIC:
                        ibGameMaster.startGame(senderId, IBGameMaster.GameMode.CLASSIC);
                        break;
                    case Callbacks.START_IB_LIST:
                        ibGameMaster.startGame(senderId, IBGameMaster.GameMode.LIST);
                        break;
                    case Callbacks.INIT_IB:
                        int room = ibGameMaster.initGame(senderId, senderName);
                        setJoined(senderId, EGame.INGLORIOUS_BASTERDS);
                        sendMsg(senderId, "Room " + room + " created!\nEnter character");
                        break;
                    case Callbacks.INIT_SPYFALL:
                        room = spyfallGameMaster.initGame(senderId, senderName);
                        setJoined(senderId, EGame.SPYFALL);
                        String message =
                                "Room " + room + " created!\n" + "Wait for party and press start button to start\n" +
                                "You can delimit locations by entering number";
                        InlineKeyboardMarkup inlineKeyboardMarkup = TgUtil.getStartSpyfallKeyboardMarkup();
                        sendMsg(senderId, message, inlineKeyboardMarkup);
                        break;
                    case Callbacks.START_SPYFALL:
                        spyfallGameMaster.startGame(senderId);
                        break;
                    case Callbacks.INIT_MAFIA:
                        room = mafiaGameMaster.initGame(senderId, senderName);
                        setJoined(senderId, EGame.MAFIA);
                        message = "Room " + room + " created!\n" + "Wait for party and press button to set roles";
                        inlineKeyboardMarkup = TgUtil.getSetRolesForMafiaKeyboardMarkup();
                        sendMsg(senderId, message, inlineKeyboardMarkup);
                        break;
                    case Callbacks.MAFIA_AUTOSET_ROLES:
                        sendMsg(senderId, "not implemented yet");//TODO implement
                        break;
                    case Callbacks.MAFIA_SET_ROLES:                                                                     // show keyboard with all default mafia roles to fill in room
                        inlineKeyboardMarkup = TgUtil.getAllRolesButtonsForMafiaKeyboardMarkup();
                        int roomSize = mafiaGameMaster.getPlayersCount(senderId);
                        message = (roomSize - 1) + " players joined the room. Add roles, then press start.\n" +
                                  "You can type any addictive role";
                        sendMsg(senderId, message, inlineKeyboardMarkup);
                        stateSaver.setStatus(senderId, UserStateSaver.Status.MAFIA_SET_ROLES);
                        break;
                    case TgUtil.Callbacks.START_MAFIA:
                        mafiaGameMaster.startGame(senderId);
                        //TODO start mafia here
                        break;
                    case Callbacks.QR_BOT:
                        sendImageUploadingAFile(senderId, AppUtil.getBotInviteQR());
                        break;
                    case Callbacks.QR_WEB:
                        sendImageUploadingAFile(senderId, AppUtil.getWebUrlQR());
                        break;
                    default:
                        if (callback.startsWith(TgUtil.ADD_MAFIA_ROLE_CALLBACK_PREFIX))                                 // add role and update previous message with room stats
                        {
                            mafiaGameMaster.addRole(senderId,
                                                    callback.substring(TgUtil.ADD_MAFIA_ROLE_CALLBACK_PREFIX.length()));
                            int messageId = update.getCallbackQuery().getMessage().getMessageId();
                            int playersCount = mafiaGameMaster.getPlayersCount(senderId);
                            List<String> roles = mafiaGameMaster.getRoles(senderId);
                            message = (playersCount - 1) + " players joined. " + roles.size() + " Roles:\n" +
                                      Convertor.convertListForTelegram(roles);
                            editMessage(senderId,
                                        messageId,
                                        message,
                                        TgUtil.getAllRolesButtonsForMafiaKeyboardMarkup());
                        } else {
                            log.error(callback + "\ndefault calback switch, smth wrong");
                        }
                        break;
                }
            }                                                                                                           // end of calback handling
        }
    }                                                                                                                   // end of onUpdatesRecieved method

    /**
     * should call if after player joined game
     * need to realize bot scenarios
     */
    private void setJoined(int playerId, EGame game) {
        stateSaver.setPlayersGame(playerId, game);
        stateSaver.setStatus(playerId, UserStateSaver.Status.JOINED);
    }

    public int sendMsg(long chatId, String msg) {
        return sendMsg(chatId, msg, null);
    }

    public int sendMsg(long chatId, String msg, @Nullable InlineKeyboardMarkup mk) {
        SendMessage message = new SendMessage().setChatId(chatId).setText(msg);

        if (mk != null) {
            message.setReplyMarkup(mk);
        }

        return tryToExecuteApiMethod(message);
    }

    public void sendImageFromUrl(int chatId, String url) {
        sendImageFromUrl(chatId, url, null);
    }

    public void sendImageFromUrl(int chatId, String url, @Nullable String caption) {
        SendPhoto sendPhotoRequest = new SendPhoto().setChatId(String.valueOf(chatId)).setPhoto(url);
        if (caption != null) {
            sendPhotoRequest.setCaption(caption);
        }

        tryToExecuteApiMethod(sendPhotoRequest);
    }

    public void sendImageUploadingAFile(int chatId, File file) {
        SendPhoto sendPhotoRequest = new SendPhoto().setChatId(String.valueOf(chatId)).setPhoto(file);

        tryToExecuteApiMethod(sendPhotoRequest);
    }

    public void editMessage(int chatId, int messageId, String message) {
        editMessage(chatId, messageId, message, null);
    }

    public void editMessage(int chatId, int messageId, String message, @Nullable InlineKeyboardMarkup mk) {
        EditMessageText editMessage =
                new EditMessageText().setChatId(new Long(chatId)).setMessageId(messageId).setText(message);

        if (mk != null) {
            editMessage.setReplyMarkup(mk);
        }

        tryToExecuteApiMethod(editMessage);
    }

    /**
     * execute call for telegram api messages
     * used to avoid try-catch calling in send\edit methods
     * <p>
     * ! can't move @Nullable fields check here, because they declared in child classes (SendMessage, EditMessageText etc)
     *
     * @return id of sended messages. can used it to update message
     */
    private int tryToExecuteApiMethod(BotApiMethod method) {
        int msgId = 0;  // no sense, but need init it. probably impossible to catch exception below
        try {
            var x = execute(method);
            msgId = ((Message) x).getMessageId();
        } catch (TelegramApiException e)    // never catched this, no idea about of conditions of this exception
        {
            e.printStackTrace();
        }
        return msgId;
    }

    /**
     * can't merge with tryToExecuteApiMethod(BotApiMethod method) because of type exception
     */
    private void tryToExecuteApiMethod(SendPhoto method) {
        try {
            execute(method);
        } catch (TelegramApiException e)    // never catched this, no idea about of conditions of this exception
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return AppUtil.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
