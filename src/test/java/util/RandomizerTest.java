package util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;
import springApplication.game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class RandomizerTest {

    @Test
    public void getRandomElement()
    {
        int playersCount = 5;
        int testsCount = 10000;

        List<String> players = new ArrayList<>();
        for (int i = 0; i < playersCount; i++)
        {
            players.add("Player " + i);
        }

        Multiset<String> multiset = HashMultiset.create();
        for (int i=0; i<testsCount; i++)
        {
            multiset.add(Randomizer.getRandomElement(players));
        }

        multiset.entrySet().forEach(entry -> {
            System.out.println(entry);
//            System.out.println(Math.round(Double.valueOf(testsCount)/entry.getCount()));
            if(Math.round(Double.valueOf(testsCount)/entry.getCount()) != playersCount) { fail(); }
        });
        System.out.println("Seems like random works correctly");
    }
}