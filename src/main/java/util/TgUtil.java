package util;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TgUtil
{
    public static InlineKeyboardMarkup getStartGameKeyboardMarkup()
    {
        InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Mode 1").setCallbackData("start1"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Mode 2").setCallbackData("start2"));

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
