package tn.esprit.spring.services;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class TimesheetServiceImpl implements ITimesheetService {

	@Autowired
	private TimesheetRepository timesheetManager;

	@Autowired
	private EmployeRepository employeeRepository;

	@Autowired
	private MissionRepository missionManager;

	@Autowired
	private DepartementRepository departementManager;

	@Override
	public Mission addMission(Mission mission) {
		return Optional.ofNullable(mission).map(m -> missionManager.save(m)).orElse(null);
	}

	@Override
	public void assignMissionToDepartment(int missionId, int depId) {
		Departement requestedDepartment = departementManager.findById(depId).get();
		if (Objects.isNull(requestedDepartment))
			throw new RuntimeException("Department doesn't exist");
		Optional.ofNullable(missionManager.findById(missionId).get()).ifPresent(m -> {
			m.setDepartement(requestedDepartment);
			missionManager.save(m);
		});
	}

	@Override
	public void addTimeSheet(int missionId, int employeId, Date dateDebut, Date dateFin) {
		Mission mission = missionManager.findById(missionId).get();
		if (Objects.isNull(mission))
			throw new RuntimeException("Mission doesn't exist");

		Employe employe = employeeRepository.findById(missionId).get();
		if (Objects.isNull(employe))
			throw new RuntimeException("Employee doesn't exist");

		if (Objects.isNull(dateDebut) || Objects.isNull(dateFin) || dateDebut.after(dateFin))
			throw new RuntimeException("Timesheet input is wrong");

		timesheetManager.save(generateTimeSheet(mission, employe, dateDebut, dateFin));
	}

	private Timesheet generateTimeSheet (Mission mission , Employe employe, Date start, Date end) {
		TimesheetPK pk = new TimesheetPK();
		pk.setDateDebut(start);
		pk.setDateFin(end);
		pk.setIdEmploye(employe.getId());
		Timesheet timesheet = new Timesheet();
		timesheet.setTimesheetPK(pk);
		timesheet.setMission(mission);
		timesheet.setEmploye(employe);
		return timesheet;
	}

	@Override
	public List<Timesheet> findAllTimeSheetsByMission(int missionId, Date from, Date to) {
		return Objects.isNull(from) && Objects.isNull(to) ? timesheetManager.findTimesheetsByMission_Id(missionId) : timesheetManager.getOverlappingTimeSheetsByMission_id(missionId, from, to);
	}

	@Override
	public List<Timesheet> findAllTimeSheetsByEmployee(int employeeId, Date from, Date to) {
		return Objects.isNull(from) && Objects.isNull(to) ? timesheetManager.findTimesheetsByEmploye_Id(employeeId) : timesheetManager.getOverlappingTimeSheets(employeeId, from, to);
	}

}
