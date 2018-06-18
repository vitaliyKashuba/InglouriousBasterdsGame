import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class IngloriousBastardBot extends TelegramLongPollingBot
{
    IBGameMaster gameMaster = IBGameMaster.getInstance();

    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String receivedMessage = update.getMessage().getText();
            String responceString = "";

            if (receivedMessage.startsWith("/"))
            {
                responceString = HandleCommand(receivedMessage);

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

    private String HandleCommand(String str)
    {
        String responceString;
        switch(str)
        {
            case "/start":
                responceString = "start";
                break;
            case "/help":
                responceString = "help";
                break;
            case "/init":
                int room = gameMaster.newRoom();
                responceString = String.valueOf(room);
                break;
            default:
                responceString = "unrecognized command";
                break;

        }

        return responceString;
    }
}
