package util;

import java.util.Random;

public class Randomizer
{
    static Random random;

    static {
        random = new Random();
    }

    public static int getRandomRoomNumber()
    {
        return random.nextInt(100);
    }

    public static int getRandomIndex(int upperBound)
    {
        return random.nextInt(upperBound);
    }
}
