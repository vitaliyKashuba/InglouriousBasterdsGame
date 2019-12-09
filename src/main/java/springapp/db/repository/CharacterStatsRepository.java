package springapp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springapp.db.model.CharactersStatsEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterStatsRepository extends JpaRepository<CharactersStatsEntity, Long> {
    List<CharactersStatsEntity> findAll();

    Optional<CharactersStatsEntity> findByCharacter(String character);
}
