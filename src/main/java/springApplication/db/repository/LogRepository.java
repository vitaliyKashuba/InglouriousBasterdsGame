package springApplication.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springApplication.db.model.LogEntity;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findAll();

//    List<LogEntity> findAllOrderByDateTimeDesc();

    @Query("select l from LogEntity l order by l.dateTime desc ")
    List<LogEntity> findAllInReverseOrder();

}
