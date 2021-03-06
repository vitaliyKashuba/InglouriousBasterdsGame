package util;

import com.google.common.io.Resources;
import net.glxn.qrgen.javase.QRCode;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Map;

public class AppUtil {
    public static final String BOT_NAME = "IngloriousBasterdsBot";
    private static final Map<String, String> env;

    static {
        env = System.getenv();
    }

    public static String getEnvironmentVariable(String var) {
        String variable = env.get(var);
        if (variable == null) {
            throw new IllegalArgumentException("no such variable");
        }
        return variable;
    }

    /**
     * deprecated as not working on heroku, because can't read from jar.
     * can be useful only when debug smth requires file
     */
    @Deprecated
    public static File loadFileFromResources(String path) throws FileNotFoundException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource(path);
        if (url == null) {
            throw new FileNotFoundException("file not found");
        }
        return new File(url.getFile());
    }

    /**
     * read text file from resources and return content as string
     */
    public static String readFromResources(@NotNull String resourceName) throws IOException {
        String fileContent;
        fileContent = Resources.toString(Resources.getResource(resourceName), Charset.defaultCharset());
        return fileContent;
    }

    /**
     * for debug
     */
    public static void printClasspath() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader) cl).getURLs();

        for (URL url : urls) {
            System.out.println(url.getFile());
        }
    }

    public static File getBotInviteQR() {
        return QRCode.from("tg://resolve?domain=" + BOT_NAME).file();
    }

    public static File getWebUrlQR() {
        return QRCode.from("https://inglorious-basterds-bot.herokuapp.com/").file();
    }

    public static ResponseEntity responce200OK() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
