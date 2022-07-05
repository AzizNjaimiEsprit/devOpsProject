package tn.esprit.spring.services;

import java.util.Date;
import java.util.List;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;


public interface ITimesheetService {
	
	Mission addMission(Mission mission);
	void assignMissionToDepartment(int missionId, int depId);
	void addTimeSheet(int missionId, int employeId, Date dateDebut, Date dateFin);
	List<Timesheet> findAllTimeSheetsByMission(int missionId, Date from, Date to);
	List<Timesheet> findAllTimeSheetsByEmployee(int missionId, Date from, Date to);

}
