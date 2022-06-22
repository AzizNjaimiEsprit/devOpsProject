package tn.esprit.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.services.IDepartmentService;
import tn.esprit.spring.services.IEntrepriseService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)

@DisplayName("Test Department class")
public class TestDepartement {

    private static final Logger log = LogManager.getLogger(TestDepartement.class);

    private static Departement departement;
    private static Entreprise entreprise;

    @Autowired
    private IDepartmentService IDepartmentService;

    @Autowired
    private IEntrepriseService entrepriseService;

    @BeforeAll
    public static void init() {
        departement = new Departement();
        entreprise = new Entreprise();
    }

    @Test
    @DisplayName("Test Ajouter Enterprise")
    @Order(1)
    public void test1_AddEnterprise() {
        log.info("test add enterprise");
        entreprise.setName("Vermeg");
        entreprise.setDepartements(new ArrayList<>());
        entreprise.setRaisonSocial("LAC 1");
        int id = entrepriseService.ajouterEntreprise(entreprise);
        entreprise.setId(id);
        assertTrue(id != 0 );
    }

    @Test
    @DisplayName("Test Ajouter Departement")
    @Order(2)
    public void test2_testAddDepartment() {
        log.info("test add department");
        departement.setName("IT");
        departement.setEntreprise(entreprise);
        departement.setEmployes(new ArrayList<>());
        departement.setMissions(new ArrayList<>());
        departement = this.IDepartmentService.addOrUpdateDepartement(departement);
        assertTrue(this.IDepartmentService.getAllDepartements().contains(departement));
    }

    @Test
    @DisplayName("test Get Department By Id")
    @Order(3)
    public void test3_getDepartment() {
        log.info("test get department");
        Departement res = IDepartmentService.getDepartement(departement.getId());
        assertTrue(res.equals(departement));
    }

    @Test
    @DisplayName("Test Update Department")
    @Order(4)
    public void test4_updateDepartment() {
        log.info("Test update department");
        departement.setName("DEV");
        departement = this.IDepartmentService.addOrUpdateDepartement(departement);
        assertEquals("DEV", IDepartmentService.getDepartementName(departement.getId()));
    }

    @Test
    @DisplayName("test Delete Department")
    @Order(5)
    public void test5_testRemove() {
        log.info("test remove department");
        this.IDepartmentService.deletedepartementById(departement.getId());
        assertFalse(this.IDepartmentService.getAllDepartements().contains(departement));
    }
}
