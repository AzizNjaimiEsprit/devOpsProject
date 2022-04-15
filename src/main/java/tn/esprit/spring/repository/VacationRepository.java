package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.Vacation;
import tn.esprit.spring.entities.VacationId;

import java.util.Date;
import java.util.List;

@Repository
public interface VacationRepository extends CrudRepository<Vacation, VacationId> {

    @Query("select v from Vacation v where v.from >= :start and v.to <= :end")
    List<Vacation> findAllBetween (@Param("start") Date from,@Param("end") Date to);
}
