package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import springApplication.ibGame.Teammate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Convertor
{
    @SneakyThrows   // probably impossible to catch JsonProcessingException while converting to json
    public static String toJson(Object o)
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

    public static <E extends Enum<E>> List<String> stringifyEnumList(List<E> enums)
    {
        List<String> strings = new ArrayList<>();
        for (E e : enums)
        {
            strings.add(e.name());
        }
        return strings;
    }

    public static List<Teammate> convertTeammatesForWebApi(@NotNull Map<String, String> teammates)
    {
        List<Teammate> tm = new ArrayList<>();
        for(String name : teammates.keySet())
        {
            tm.add(new Teammate(name, teammates.get(name)));
        }
        return tm;
    }

    @NotNull
    public static String convertTeammatesForTelegram(@NotNull Map<String, String> teammates)
    {
        StringBuilder sb = new StringBuilder();
        for(String name : teammates.keySet())
        {
            sb.append(name).append(" - ").append(teammates.get(name)).append("\n");
        }
        return sb.toString();
    }

    @NotNull
    public static String convertLocationsForTelegram(@NotNull List<String> locations)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Locations:\n");
        for (String l: locations)
        {
            sb.append(l);
            sb.append("\n");
        }

        return sb.toString();
    }

    @NotNull
    public static String convertListForTelegram(@NotNull List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        for (String s: list)
        {
            sb.append(s);
            sb.append("\n");
        }

        return sb.toString();
    }
}
