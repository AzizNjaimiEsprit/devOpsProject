package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.entities.VacationRequest;

import java.util.List;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    @Query("select v from VacationRequest v where v.supervisor = :supervisor_id and v.approved is null")
    List<VacationRequest> findAllBySuperVisor (@Param("supervisor_id") int id);

    @Query("select v from VacationRequest v where v.employe = :employee_id and v.approved is null")
    List<VacationRequest> findAllByEmployee (@Param("employee_id") int id);

}
