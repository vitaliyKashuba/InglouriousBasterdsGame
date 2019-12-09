package springapp.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springapp.db.model.CharactersStatsEntity;
import springapp.db.repository.CharacterStatsRepository;

import java.util.List;

@Service
public class CharacterStatsService {
    private static final int STATS_LIMIT = 10;

    @Autowired
    private CharacterStatsRepository characterStatsRepository;

    public void addOrUpdateCharacterStat(String character) {
        characterStatsRepository.findByCharacter(character).ifPresentOrElse(characterStat -> {
            characterStat.incrementCount();
            characterStatsRepository.save(characterStat);
        }, () -> {
            CharactersStatsEntity characterStat = new CharactersStatsEntity();
            characterStat.setCharacter(character);
            characterStatsRepository.save(characterStat);
        });
    }

    public List<CharactersStatsEntity> getAllStats() {
        return characterStatsRepository.findAll();
    }

    public String getStatsInTgReadebleFormat() {
        return "CHARACTER STATS:\n" + characterStatsRepository.findAll()
                .stream()
                .sorted((char1, char2) -> Integer.compare(char2.getCount(), char1.getCount()))
                .limit(STATS_LIMIT)
                .map(character -> character.getCharacter() + " - " + character.getCount())
                .reduce((capacitor, character) -> capacitor += ",\n" + character)
                .orElse("no stats");
    }

}
