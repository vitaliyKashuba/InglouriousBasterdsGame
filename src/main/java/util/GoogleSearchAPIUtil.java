package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class GoogleSearchAPIUtil
{
    private static final String GOOGLE_API_KEY;
    private static final String CSE_ID; // custom search engine identifier
    private static final String API_URL;

    static
    {
        GOOGLE_API_KEY = AppUtil.getEnvironmentVariable("GOOGLE_API_KEY");
        CSE_ID = AppUtil.getEnvironmentVariable("CSE_ID");
        API_URL = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_API_KEY + "&cx=" + CSE_ID + "&searchType=image";
//        API_URL = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_API_KEY + "&cx=" + CSE_ID + "&searchType=image&q=";
    }

    public static String getApiUrl()
    {
        return API_URL;
    }

    static String makeRequest(String query)
    {
        return HttpRequest.get(API_URL, true, 'q', query).body();
    }

    @SneakyThrows   //TODO handle exception ?
    static List<String> getImagesFromResponse(String json)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> images = new ArrayList<>();

        JsonNode rootNode = objectMapper.readTree(json);
        List<JsonNode> imageNodes = rootNode.path("items").findValues("link");

        for (JsonNode n : imageNodes)
        {
            images.add(n.asText());
        }

        return images;
    }

    public static List<String> searchForImages(String request)
    {
        return getImagesFromResponse(makeRequest(request));
    }
}
