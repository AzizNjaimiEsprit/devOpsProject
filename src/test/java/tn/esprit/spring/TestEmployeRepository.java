package tn.esprit.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IEntrepriseService;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
@TestMethodOrder(value = OrderAnnotation.class)
@DisplayName("Test Employee repository class")
public class TestEmployeRepository {
    private static final Logger log = LogManager.getLogger(TestEmployeRepository.class);

    private static Employe employe;

    private static Contrat contrat;

    @Autowired
    private IEmployeService employeService;
    private IEntrepriseService entrepriseService;


    @BeforeAll
    public static void init() {
        employe = new Employe();
        contrat = new Contrat();
    }

    @Test
    @DisplayName("Test insert methode")
    @Order(1)
    void testInsert() {
        employe = employeService.addOrUpdateEmploye(employe);

        log.info("Test add");
        assertTrue(employeService.getAllEmployes().stream().filter(e -> e.getId() == employe.getId()).findFirst()
                .isPresent());
    }

    @Test
    @DisplayName("test select")
    @Order(2)
    void testSelect() {
        log.info("test select method");
        assertTrue(employeService.getAllEmployes().size() > 0);
    }

    @Test
    @DisplayName("Test update methode")
    @Order(3)
    void testUpdate() {
        log.info("Test update");
        employe.setPrenom("yassine");
        employeService.addOrUpdateEmploye(employe);
        log.info("update employee prenom");
        assertEquals("yassine", employeService.getEmployePrenomById(employe.getId()));
    }

    @Test
    @DisplayName("test affecterContratAEmploye")
    @Order(4)
    void testAffecterContratAEmploye() {


        log.info("Start Method affecterContratAEmploye");


        employe = employeService.addOrUpdateEmploye(employe);
        contrat = employeService.addContrat(contrat);
        log.info(contrat);
        log.info(employe);
        employeService.affecterContratAEmploye(contrat.getReference(), employe.getId());
        employe = employeService.getAllEmployes().stream().filter(x -> x.getId() == employe.getId()).findFirst().get();
        assertEquals(employe.getContrat().getReference(), contrat.getReference());

        log.info("End Method affecterContratAEmploye");
    }

    @Test
    @DisplayName("test remove")
    @Order(5)
    void testRemove() {
        log.info("test remove");
        employeService.deleteContratById(employe.getContrat().getReference());
        employeService.deleteEmployeById(employe.getId());
        assertFalse(employeService.getAllEmployes().contains(employe));

    }


}