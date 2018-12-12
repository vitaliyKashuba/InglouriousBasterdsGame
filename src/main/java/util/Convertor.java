package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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

    public static List<Teammate> convertTeammatesForWebApi(Map<String, String> teammates)
    {
        List<Teammate> tm = new ArrayList<>();
        for(String name : teammates.keySet())
        {
            tm.add(new Teammate(name, teammates.get(name)));
        }
        return tm;
    }

    public static String convertTeammatesForTelegram(Map<String, String> teammates)
    {
        StringBuilder sb = new StringBuilder();
        for(String name : teammates.keySet())
        {
            sb.append(name).append(" - ").append(teammates.get(name)).append("\n");
        }
        return sb.toString();
    }

    public static String convertLocationsForTelegram(Collection<String> locations)
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
}
