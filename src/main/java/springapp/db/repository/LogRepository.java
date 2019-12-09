package springapp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springapp.db.model.LogEntity;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findAll();

    @Query("select l from LogEntity l order by l.dateTime desc ")
    List<LogEntity> findAllInReverseOrder();

}
