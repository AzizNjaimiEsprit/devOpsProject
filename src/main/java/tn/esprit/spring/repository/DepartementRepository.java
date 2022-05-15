package tn.esprit.spring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.repository.query.Param;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;

public interface DepartementRepository extends CrudRepository<Departement, Integer>{
    @Query("SELECT e from Employe e INNER JOIN e.departements d WHERE d.id = :dep_id AND e.role = tn.esprit.spring.entities.Role.CHEF_DEPARTEMENT")
    Employe getSupervisor (@Param("dep_id") int departmentId);
}
