package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class AppUtil
{
    private final static Map<String, String> env;

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

    @NotNull
    @Contract("_ -> new")
    public static File loadFileFromResources(String path) throws FileNotFoundException
    {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(path);
        if(url == null)
        {
            throw new FileNotFoundException("file not found");
        }
        return new File(url.getFile());
    }

    /**for debug*/
    public static void printClasspath()
    {
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
            System.out.println(url.getFile());
        }
    }

    public static ResponseEntity responce200OK()
    {
        return new ResponseEntity(HttpStatus.OK);
    }

}
