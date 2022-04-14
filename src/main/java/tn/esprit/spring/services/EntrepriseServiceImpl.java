package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Slf4j
@Service
public class EntrepriseServiceImpl implements IEntrepriseService {

	@Autowired
    EntrepriseRepository entrepriseRepoistory;
	@Autowired
	DepartementRepository deptRepoistory;
	
	
	@Override
	public List<Entreprise> getAllEntreprises() {
		return (List<Entreprise>) entrepriseRepoistory.findAll();
	}
	
	public int ajouterEntreprise(Entreprise entreprise) {
		entrepriseRepoistory.save(entreprise);
		return entreprise.getId();
	}

	public int ajouterDepartement(Departement dep) {
		deptRepoistory.save(dep);
		return dep.getId();
	}
	
	public void affecterDepartementAEntreprise(int depId, int entrepriseId) {
		// Le bout Master de cette relation N:1 est departement
		// donc il faut rajouter l'entreprise a departement
		// ==> c'est l'objet departement(le master) qui va mettre a jour l'association
		// Rappel : la classe qui contient mappedBy represente le bout Slave
		// Rappel : Dans une relation oneToMany le mappedBy doit etre du cote one.

		deptRepoistory.findById(depId).ifPresent(dep -> {
			if (entrepriseRepoistory.findById(entrepriseId).isPresent()) {
				dep.setEntreprise(entrepriseRepoistory.findById(entrepriseId).get());
				deptRepoistory.save(dep);
			}
		});

	}
	
	public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) {
		List<String> depNames = new ArrayList<>();

		entrepriseRepoistory.findById(entrepriseId).ifPresent(entrep -> {
			for (Departement dep : entrep.getDepartements()) {
				depNames.add(dep.getName());
			}

		});

		return depNames;
	}

	@Transactional
	public void deleteEntrepriseById(int entrepriseId) {
		entrepriseRepoistory.findById(entrepriseId).ifPresent(entrep -> entrepriseRepoistory.delete(entrep));

	}

	@Transactional
	public void deleteDepartementById(int depId) {
		deptRepoistory.findById(depId).ifPresent(dep -> deptRepoistory.delete(dep));

	}


	public Entreprise getEntrepriseById(int entrepriseId) {
		Optional<Entreprise> optionalEntreprise = entrepriseRepoistory.findById(entrepriseId);
		return optionalEntreprise.isPresent() ? optionalEntreprise.get()
				: optionalEntreprise.orElseThrow(() -> new NoSuchElementException(
						"There's no entrepsise with the id %s in the database" + entrepriseId));
	}

}
