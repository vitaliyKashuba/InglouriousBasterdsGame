package util;


import jdk.nashorn.internal.codegen.CompilerConstants;
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
    public static class Callbacks
    {
        public static final String START_MAFIA = "mafia_start";
        public static final String START_IB_CLASSIC = "start_ib_classic";
        public static final String START_IB_LIST = "start_ib_list";
        public static final String START_SPYFALL = "start_spyfall";
        public static final String INIT_IB = "init_ib";
        public static final String INIT_SPYFALL = "init_spyfall";
        public static final String INIT_MAFIA = "init_mafia";
        public static final String MAFIA_SET_ROLES = "mafia_set";
        public static final String MAFIA_AUTOSET_ROLES = "mafia_autoset";
        public static final String QR_BOT = "qr_bot";
        public static final String QR_WEB = "qr_web";
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
            put("CLASSIC MODE", Callbacks.START_IB_CLASSIC);
            put("LIST MODE", Callbacks.START_IB_LIST);
        }});
    }

    /**
     * @return markup to start spyfall game
     */
    public static InlineKeyboardMarkup getStartSpyfallKeyboardMarkup()
    {
        return buildKeyboardMarkup(new TreeMap<String, String>()
        {{
            put("START", Callbacks.START_SPYFALL);
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
            put("Inglorious basterds", Callbacks.INIT_IB);
        }});
        lines.add(new TreeMap<String, String>()
        {{
            put("Spyfall", Callbacks.INIT_SPYFALL);
            put("Mafia", Callbacks.INIT_MAFIA);
        }});

        return buildKeyboardMarkup(lines);
    }

    public static InlineKeyboardMarkup getSetRolesForMafiaKeyboardMarkup()
    {
        return buildKeyboardMarkup(new TreeMap<String, String>()
        {{
            put("SET ROLES", Callbacks.MAFIA_SET_ROLES);
            put("AUTO", Callbacks.MAFIA_AUTOSET_ROLES);
        }});
    }

    public static InlineKeyboardMarkup getAllRolesButtonsForMafiaKeyboardMarkup()       // TODO make static keyboard to avoid re-initing at every call ?
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

    public static InlineKeyboardMarkup getMainMenuKeyboardMarkup()
    {
        List<Map<String,String>> lines = new ArrayList<>();
        lines.add(new TreeMap<String, String>()
        {{
            put("Bot QR", Callbacks.QR_BOT);
            put("Web QR", Callbacks.QR_WEB);
        }});

        return buildKeyboardMarkup(lines);
    }
}
