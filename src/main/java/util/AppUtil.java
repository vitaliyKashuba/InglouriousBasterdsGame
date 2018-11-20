package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import springApplication.game.Teammate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppUtil
{
    private static Map<String, String> env;

    static
    {
        env = System.getenv();
    }

    public static String getEnvironmentVariable(String var)
    {
        String variable = env.get(var);
        if (variable == null)
        {
            throw new IllegalArgumentException("no such variable");
        }
        return variable;
    }

    @SneakyThrows   // probably impossible to catch JsonProcessingException while converting to json
    public static String toJson(Object o)
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

    public static ResponseEntity responce200OK()
    {
        return new ResponseEntity(HttpStatus.OK);
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

}
