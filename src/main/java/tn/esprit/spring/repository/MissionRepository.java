package tn.esprit.spring.repository;

import org.springframework.data.repository.CrudRepository;

import tn.esprit.spring.entities.Mission;

import java.util.List;


public interface MissionRepository extends CrudRepository<Mission, Integer> {
    List<Mission> findMissionsByDepartement_Id (int depId);
}
