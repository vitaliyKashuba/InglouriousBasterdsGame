package util;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randomizer
{
    private static Random random;
    private static Faker faker;

    static {
        random = new Random();
        faker = new Faker();
    }

    public static int getRandomRoomNumber()
    {
        return random.nextInt(100);
    }

    private static int getRandomIndex(int upperBound)
    {
        return random.nextInt(upperBound);
    }

    public static String getRandomElement(List<String> list)
    {
        return list.get(getRandomIndex(list.size()));
    }

    public static String getRandomCharacter()
    {
        ArrayList<String> ch = new ArrayList<>();
        ch.add(faker.lordOfTheRings().character() + " (LOTR)");
        ch.add(faker.hobbit().character() + " (Hobbit)");
        ch.add(faker.lebowski().character() + " (Big Lebowski)");

        return getRandomElement(ch);

    }
}
