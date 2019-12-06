package springApplication.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springApplication.db.model.CharactersStatsEntity;
import springApplication.db.repository.CharacterStatsRepository;

import java.util.Optional;

@Service
public class CharacterStatsService {

    @Autowired
    CharacterStatsRepository characterStatsRepository;

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

}
