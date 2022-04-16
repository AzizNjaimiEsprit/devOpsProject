package tn.esprit.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;
import tn.esprit.spring.services.IEntrepriseService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(value = OrderAnnotation.class)
@DisplayName("Test Entreprise repository class")
public class TestEntrepriseController {
    private static final Logger log = LogManager.getLogger(TestEntrepriseController.class);

    @Autowired
	IEntrepriseService es;
	@Autowired
	EntrepriseRepository er;
	@Autowired
	DepartementRepository dr;
	
	String etsName = "vermeg";
	Entreprise ent = new Entreprise(etsName, etsName);
	Departement dep = new Departement(etsName);
	
	
	@Test
	@DisplayName("Test insert entreprise methode")
    @Order(1)
	public void ajouterEntrepriseTest() {
		
		int a = es.ajouterEntreprise(ent);
		assertTrue(a > 0);
		log.info("Test add entreprise");
		// clear db
		er.deleteById(a);
	}

	@Test
	@DisplayName("Test insert departement methode")
    @Order(2)
	public void ajouterDepartementTest() {
		
		int a = es.ajouterDepartement(dep);
		assertTrue(a > 0);
		log.info("Test add department");
		// clear db
		dr.deleteById(a);
	}

	@Test
	@DisplayName("Test affect departement to entreprise")
    @Order(3)
	public void affecterDepartementAEntrepriseTest() {
		int addedEntrepriseId = es.ajouterEntreprise(ent);
		int addedDepId = es.ajouterDepartement(dep);
		es.affecterDepartementAEntreprise(addedDepId, addedEntrepriseId);
		Optional<Departement> depOpt = dr.findById(addedDepId);
		Departement departementEntity = null;
		if (depOpt.isPresent()) {
			departementEntity = depOpt.get();
		}
		if(departementEntity !=null){
		 assertEquals(departementEntity.getEntreprise().getId(), addedEntrepriseId);
		}
		log.info("Test affect departement to entreprise");
		// clear db
		dr.deleteById(addedDepId);
		er.deleteById(addedEntrepriseId);
	}

	@Test
	@DisplayName("Test get department by entreprise")
    @Order(4)
	public void getAllDepartementsNamesByEntrepriseTest() {
		int addedEntrepriseId = es.ajouterEntreprise(ent);
		int addedDepId = es.ajouterDepartement(dep);
		List<String> names = es.getAllDepartementsNamesByEntreprise(addedEntrepriseId);
		assertNotNull(names);
		log.info("Test get departement by entreprise");
		// clear db
		er.deleteById(addedEntrepriseId);
		dr.deleteById(addedDepId);
	}

	@Test
	@DisplayName("Test get entreprise by id")
    @Order(5)
	public void getEntrepriseByIdTest() {
		int a = es.ajouterEntreprise(ent);
		Optional<Entreprise> entOpt = er.findById(a);
		Entreprise entr = null;
		if (entOpt.isPresent()) {
			entr = entOpt.get();
		}
		if(entr != null){
		 assertEquals(entr.getName(), ent.getName());
		 assertEquals(entr.getRaisonSocial(), ent.getRaisonSocial());
		}
		log.info("Test get entreprise by id");
		// clear db
		er.deleteById(a);
	}

	@Test
	@DisplayName("Test remove entreprise")
    @Order(6)
	public void deleteEntrepriseByIdTest() {
		int a = es.ajouterEntreprise(ent);
		log.info("test remove entreprise");
		es.deleteEntrepriseById(a);
		Optional<Entreprise> mustBeNull = er.findById(a);
		assertFalse(mustBeNull.isPresent());
	}

	@Test
	@DisplayName("Test remove departement")
    @Order(7)
	public void deleteDepartementByIdTest() {
		int a = es.ajouterDepartement(dep);
		log.info("test remove departement");
		es.deleteDepartementById(a);
		Optional<Departement> mustBeNull = dr.findById(a);
		assertFalse(mustBeNull.isPresent());
	}

}