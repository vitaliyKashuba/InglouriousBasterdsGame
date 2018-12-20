package util;


import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import springApplication.mafiaGame.MafiaGameMaster;

import javax.management.relation.Role;
import java.util.*;

public class TgUtil
{
    public static final String ADD_MAFIA_ROLE_CALLBACK_PREFIX = "mafia_add";

    /** not enum because of don't want to use Enum.getValue() methods, just keep constants*/
    public class Callbacks
    {
        public static final String START_MAFIA = "mafia_start";
    }

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

    public static InlineKeyboardMarkup getSetRolesForMafiaKeyboardMarkup()
    {
        return buildKeyboardMarkup(new TreeMap<String, String>()
        {{
            put("SET ROLES", "set_mafia_roles");
            put("AUTO", "autoset_mafia_roles");
        }});
    }

    public static InlineKeyboardMarkup getAllRolesButtonsForMafiaKeyboardMarkup()
    {
        List<MafiaGameMaster.Roles> roles = MafiaGameMaster.getAllRoles();

        Map<String, String> buttons = new TreeMap<>();

        for (MafiaGameMaster.Roles r : roles)
        {
            buttons.put(r.name(), ADD_MAFIA_ROLE_CALLBACK_PREFIX + r.name());
        }

        List<Map<String,String>> buttonsGrid = convertToMultiline(buttons, 3);
        buttonsGrid.add(new TreeMap<String, String>()
        {{
            put("START", Callbacks.START_MAFIA);
        }});

        return buildKeyboardMarkup(buttonsGrid);
    }

    private static List<Map<String,String>> convertToMultiline(Map<String, String> buttons, int buttonsInLine)
    {
        List<Map<String,String>> lines = new ArrayList<>();

        Map<String, String> buttonsRow = new TreeMap<>();
        for(String key: buttons.keySet())
        {
            if (buttonsRow.size() < buttonsInLine)
            {
                buttonsRow.put(key, buttons.get(key));
            }
            else
            {
                lines.add(buttonsRow);
                buttonsRow = new TreeMap<>();
                buttonsRow.put(key, buttons.get(key));
            }
        }
        lines.add(buttonsRow);
        return lines;
    }

    public static InlineKeyboardMarkup getRandomCharacterButtonKeyboardMarkup()
    {
        return buildKeyboardMarkup(new TreeMap<String, String>()
        {{
            put("Set random character\"", "random");
        }});
    }
}
