package util;


import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public class TgUtil
{
    /**
     * markup builder
     * @param buttonsRows list - rows of keyboard markup
     *                    map - button text: callback
     * @return markup
     */
    private static InlineKeyboardMarkup buildKeyboardMarkup(@NotNull List<Map<String,String>> buttonsRows)
    {
        InlineKeyboardMarkup inlineKeyboardMarkup =new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList= new ArrayList<>();

        for (Map<String, String> buttonRow : buttonsRows)
        {
            List<InlineKeyboardButton> row = new ArrayList<>();

            for (String buttonText: buttonRow.keySet())
            {
                String buttonCallback = buttonRow.get(buttonText);
                row.add(new InlineKeyboardButton().setText(buttonText).setCallbackData(buttonCallback));
            }

            rowList.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private static InlineKeyboardMarkup buildKeyboardMarkup(Map<String, String> buttons)
    {
        List<Map<String,String>> lines = new ArrayList<>();
        lines.add(buttons);
        return buildKeyboardMarkup(lines);
    }

    /**
     * @return markup for select mode of IB game
     */
    public static InlineKeyboardMarkup getStartIBKeyboardMarkup()
    {
        return buildKeyboardMarkup(new TreeMap<String, String>()
        {{
            put("CLASSIC MODE", "start1");
            put("LIST MODE", "start2");
        }});
    }

    /**
     * @return markup to start spyfall game
     */
    public static InlineKeyboardMarkup getStartSpyfallKeyboardMarkup()
    {
        return buildKeyboardMarkup(new TreeMap<String, String>()
        {{
            put("START", "start_spyfall");
        }});
    }

    /**
     * @return markup for game selecting
     */
    public static InlineKeyboardMarkup getSelectGameKeyboardMarkup()
    {
        List<Map<String,String>> lines = new ArrayList<>();
        lines.add(new TreeMap<String, String>()
        {{
            put("Inglorious basterds", "init_ib");
        }});
        lines.add(new TreeMap<String, String>()
        {{
            put("Spyfall", "init_spyfall");
            put("Mafia", "init_mafia");
        }});

        return buildKeyboardMarkup(lines);
    }

    public static InlineKeyboardMarkup getRandomCharacterButtonKeyboardMarkup()
    {
        return buildKeyboardMarkup(new TreeMap<String, String>()
        {{
            put("Set random character\"", "random");
        }});
    }
}
