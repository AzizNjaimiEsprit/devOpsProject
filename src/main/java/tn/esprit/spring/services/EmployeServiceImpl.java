package tn.esprit.spring.services;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class EmployeServiceImpl implements IEmployeService {
	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	@Override
	public Employe authenticate(String login, String password) {
		return employeRepository.getEmployeByEmailAndPassword(login, password);
	}

	@Override
	public Employe addOrUpdateEmploye(Employe employe) {
		return employeRepository.save(employe);

	}


	public void mettreAjourEmailByEmployeId(String email, int employeId) {

		employeRepository.findById(employeId).ifPresent(e -> {
			e.setEmail(email);
			employeRepository.save(e);
		});
	}

	@Transactional
	public void affecterEmployeADepartement(int employeId, int depId) {

		deptRepoistory.findById(depId).ifPresent(d -> {
			if (employeRepository.findById(employeId).isPresent()) {
				if (d.getEmployes() == null) {
					List<Employe> employes = new ArrayList<>();
					employes.add(employeRepository.findById(employeId).get());
					d.setEmployes(employes);
				} else {

					d.getEmployes().add(employeRepository.findById(employeId).get());
				}
				deptRepoistory.save(d);
			}
		});

	}

	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId) {
		deptRepoistory.findById(depId).ifPresent(dep -> {
			int employeNb = dep.getEmployes().size();
			for (int index = 0; index < employeNb; index++) {
				if (dep.getEmployes().get(index).getId() == employeId) {
					dep.getEmployes().remove(index);
					break;// a revoir
				}
			}
		});
	}

	// Tablesapce (espace disque)

	public int ajouterContrat(Contrat contrat) {
		contratRepoistory.save(contrat);
		return contrat.getReference();
	}

	public Contrat addContrat(Contrat contrat) {
		return contratRepoistory.save(contrat);

	}


	public void affecterContratAEmploye(int contratId, int employeId) {
		Optional<Contrat> optionalContract = contratRepoistory.findById(contratId);
		Optional<Employe> optionalEmployee = employeRepository.findById(employeId);
		optionalContract.ifPresent(o -> {
			if (optionalEmployee.isPresent()) {
				o.setEmploye(optionalEmployee.get());
				contratRepoistory.save(o);
			}
		});
	}




	public String getEmployePrenomById(int employeId) {
		Optional<Employe> optionalEmployee = employeRepository.findById(employeId);
		return optionalEmployee.isPresent() ? optionalEmployee.get().getPrenom()
				: new NoSuchElementException("Employe with the id " + employeId + " doesent exist in the db")
				.getMessage();
	}

	public void deleteEmployeById(int employeId)
	{
		Optional<Employe> optionalEmployee = employeRepository.findById(employeId);
		optionalEmployee.ifPresent(o -> {
			for(Departement dep : o.getDepartements()){
				dep.getEmployes().remove(o);
			}
			employeRepository.delete(o);
		});


	}

	public void deleteContratById(int contratId) {

		contratRepoistory.findById(contratId).ifPresent(c -> contratRepoistory.delete(c));

	}

	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}

	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}

	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
		employeRepository.deleteAllContratJPQL();
	}

	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}

	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
														 Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
		return (List<Employe>) employeRepository.findAll();
	}




}