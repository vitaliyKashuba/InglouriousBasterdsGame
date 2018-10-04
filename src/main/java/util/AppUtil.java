package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

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

}
