package util;

import com.github.javafaker.Faker;
import org.jetbrains.annotations.NotNull;
import springapp.ibGame.Teammate;

import java.util.*;

public class Randomizer {
    private static final Random random;
    private static final Faker faker;

    static {
        random = new Random();
        faker = new Faker();
    }

    public static int getRandomRoomNumber() {
        return random.nextInt(100);
    }

    private static int getRandomIndex(int upperBound) {
        return random.nextInt(upperBound);
    }

    @NotNull
    public static <T> T getRandomElement(@NotNull Collection<T> list) {
        return list.stream().skip(getRandomIndex(list.size())).findFirst().get();
    }

    /**
     * @deprecated use Collections.shuffle instead. code keept to remind about reinventing existing utils
     */
    @Deprecated
    public static <T> void shuffle(@NotNull Collection<T> list) {
        for (int i = 0; i < list.size() * 2; i++) {
            T el = list.stream().skip(getRandomIndex(list.size())).findFirst().get();
            list.remove(el);
            list.add(el);
        }
    }

    @NotNull
    public static String getRandomIBCharacter() {
        ArrayList<String> ch = new ArrayList<>();
        ch.add(faker.lordOfTheRings().character() + " (LOTR)");
        ch.add(faker.hobbit().character() + " (Hobbit)");
        ch.add(faker.lebowski().character() + " (Big Lebowski)");

        return getRandomElement(ch);
    }

    @NotNull
    public static Teammate getRandomIbTreammate() {
        return new Teammate(faker.name().fullName(), getRandomIBCharacter());
    }

    /**
     * @return random id for non-tg players
     * id's like 10000xxxx
     */
    public static int getRandomPlayerId() {
        return 100000000 + random.nextInt(9999);
    }
}
