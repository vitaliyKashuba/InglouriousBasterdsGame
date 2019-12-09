package util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AppUtilTest {

    @Test
    public void readFromResourcesTest() {
        try {
            AppUtil.readFromResources("spyfall.json");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void environmentVariablesTest() {
        try {
            AppUtil.getEnvironmentVariable("GOOGLE_API_KEY");
            AppUtil.getEnvironmentVariable("CSE_ID");
            AppUtil.getEnvironmentVariable("TOKEN");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fail();
        }
    }
}