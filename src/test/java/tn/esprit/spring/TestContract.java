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
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.services.IContractService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
@TestMethodOrder(value = OrderAnnotation.class)
@DisplayName("Contract Service Test")
public class TestContract {
    private static final Logger log = LogManager.getLogger(TestContract.class);
    private static Contrat contrat;
    @Autowired
    private IContractService iContractService;

    @BeforeAll
    public static void init() {
        contrat = new Contrat();
    }

    @Test
    @DisplayName("Ajout d'un contrat 1")
    @Order(1)
    public void testAjoutContrat1() {
        Employe  employe = new Employe("samar","neji","email@gmail","1234",true, Role.INGENIEUR);
        contrat.setTypeContrat("CDI");
        contrat.setEmploye(employe);
        contrat.setDateDebut(new Date(21/ 4 /2022));
        contrat.setSalaire(850);
        int reference = iContractService.addOrUpdateContrat(contrat).getReference();
        contrat.setReference(reference);
        log.info("Test add");
        assertTrue(iContractService.getAllContrats().stream().anyMatch(e -> e.getReference() == contrat.getReference()));

    }
    @Test
    @DisplayName("Ajout d'un contrat 2")
    @Order(2)
    public void testAjoutContrat2() {
        Employe  employe = new Employe("samar","neji","email@gmail","1234",true, Role.INGENIEUR);
        contrat.setTypeContrat("CDI");
        contrat.setEmploye(employe);
        contrat.setDateDebut(new Date(21/ 4 /2022));
        contrat.setSalaire(850);
        int reference = iContractService.addOrUpdateContrat(contrat).getReference();
        contrat.setReference(reference);
        log.info("Test add");
        assertTrue(iContractService.getAllContrats().stream().anyMatch(e -> e.getReference() == contrat.getReference()));
    }



    @Test
    @DisplayName("Selection des contrats")
    @Order(3)
    public void getAllContract() {
        log.info("Get all contracts");
        assertTrue(iContractService.getAllContrats().size() > 0);
    }

    @Test
    @DisplayName("Modifier un contrat ")
    @Order(4)
    public void UpdateContract() {
        log.info("Update contract");
        contrat.setTypeContrat("CDD");
        iContractService.addOrUpdateContrat(contrat);
        log.info("Update contract type");
        assertEquals("CDD", iContractService.getTypeContratById(contrat.getReference()));
    }


    @Test
    @DisplayName("Supprimer un contrat ")
    @Order(5)
    public void RemoveContract() {
        log.info("Remove Contract");
        iContractService.deleteContratById(contrat.getReference());
        assertFalse(iContractService.getAllContrats().contains(contrat));
    }
}
