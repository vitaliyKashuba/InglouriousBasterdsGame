import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class IngloriousBastardBot extends TelegramLongPollingBot
{
    private IBGameMaster gameMaster = IBGameMaster.getInstance();

    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String receivedMessage = update.getMessage().getText();
            String responceString = "";

            int senderId = update.getMessage().getFrom().getId();

            if (receivedMessage.startsWith("/"))
            {
                switch(receivedMessage)
                {
                    case "/start":
                        gameMaster.addPlayer(new IBPlayer(senderId));
                        responceString = "welcome!";
                        break;
                    case "/init":
                        int room = gameMaster.initGame(senderId);
                        responceString = String.valueOf(room);
                        break;
                    case "/join":
                        responceString = "join";
                        break;
                    case "/help":
                        responceString = "help";
                        break;
                    default:
                        responceString = "unrecognized command";
                        break;

                }

                SendMessage message = new SendMessage()
                        .setChatId(update.getMessage().getChatId())
                        .setText(responceString);
                try
                {
                    execute(message);
                } catch (TelegramApiException e)
                {
                    e.printStackTrace();
                }
            } else
            {
                //TODO parse message
            }

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

//    private String HandleCommand(String str)
//    {
//        String responceString;
//        switch(str)
//        {
//            case "/start":
//                gameMaster.addPlayer(new IBPlayer());
//                responceString = "welcome!";
//                break;
//            case "/help":
//                responceString = "help";
//                break;
//            case "/init":
//                int room = gameMaster.newRoom();
//                responceString = String.valueOf(room);
//                break;
//            default:
//                responceString = "unrecognized command";
//                break;
//
//        }
//
//        return responceString;
//    }

//    private String HandleMessage(String str)
//    {
//
//    }
}
