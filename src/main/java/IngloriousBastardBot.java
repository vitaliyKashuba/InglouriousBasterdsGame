import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class IngloriousBastardBot extends TelegramLongPollingBot
{
    private IBGameMaster gameMaster;

    public IngloriousBastardBot()
    {
        gameMaster = IBGameMaster.getInstance();
    }

    //TODO check nulls, user data. refactor
    //TODO split to handleCommands() amd handleMessages()
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String receivedMessage = update.getMessage().getText();
            String responceString = "";

            int senderId = update.getMessage().getFrom().getId();

            if (receivedMessage.startsWith("/"))
            {
                switch(receivedMessage) //TODO add /stats command
                {
                    case "/start":
                        gameMaster.addPlayer(new IBPlayer(senderId));
                        responceString = "welcome!";
                        break;
                    case "/init":
                        int room = gameMaster.initGame(senderId);
                        gameMaster.changeStatus(senderId, IBPlayer.Status.JOINED);
                        responceString = "Room " + String.valueOf(room) + " created!\nEnter character";
                        break;
                    case "/join":
                        gameMaster.addPlayerIfNull(senderId);
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
                                responceString = p.getCharacter();
                                sendMsg(p.getId(), responceString);
                            }

                            return;
                        }
                        else {
                            responceString = "only room creators allows run this command";
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

    public String getBotUsername()
    {
        return "IngloriousBasterdsBot";
    }

    public String getBotToken()
    {
        return "581126969:AAEGSMNLhPt_qx-1hCTMHiyqJwjDHUax04g";
    }

}
