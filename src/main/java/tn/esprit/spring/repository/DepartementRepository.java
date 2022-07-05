package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;

import java.util.List;

public interface DepartementRepository extends CrudRepository<Departement, Integer>{
    @Query("SELECT e from Employe e INNER JOIN e.departements d WHERE d.id = :dep_id AND e.role = tn.esprit.spring.entities.Role.CHEF_DEPARTEMENT")
    Employe getSupervisor (@Param("dep_id") int departmentId);

    @Query(nativeQuery = true ,value = "select * from departement where id in (select departements_id from departement_employes de INNER JOIN employe e on de.employes_id = e.id where e.id = :supervisor_id AND e.role = 'CHEF_DEPARTEMENT')")
    List<Departement> findDepartementsBySupervisor (@Param("supervisor_id") int supervisorId);
}
