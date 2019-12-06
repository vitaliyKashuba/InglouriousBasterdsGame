package springApplication.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springApplication.db.model.CharactersStatsEntity;
import springApplication.db.model.LogEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterStatsRepository extends JpaRepository<CharactersStatsEntity, Long> {
    List<CharactersStatsEntity> findAll();

    Optional<CharactersStatsEntity> findByCharacter(String character);
}
