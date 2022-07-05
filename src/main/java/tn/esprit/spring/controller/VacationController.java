package tn.esprit.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.VacationRequest;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IVacationService;
import tn.esprit.spring.util.TimeSheetUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/vacations")
public class VacationController {

    private Logger logger = LoggerFactory.getLogger(VacationController.class);

    @Autowired
    private IVacationService vacationService;

    @Autowired
    private DepartementRepository departementRepository;

    @PostMapping("/submitRequest")
    public void submitVacationRequest (@RequestBody VacationRequest request, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return;
        }

        if (request.getFrom() == null ||request.getTo() == null || request.getFrom().after(request.getTo()))
            throw new RuntimeException("Wrong vacation request input");

        request.setEmploye(currentUser);
        request.setSupervisor(departementRepository.getSupervisor(currentUser.getDepartements().iterator().next().getId()));
        vacationService.submitRequest(request);
    }

    @PostMapping("/{requestReference}/acceptRequest")
    public void acceptRequest (@PathVariable long requestReference, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return;
        }
        vacationService.acceptRequest(requestReference, currentUser.getId());
    }

    @PostMapping("/{requestReference}/declineRequest")
    public void declineRequest (@PathVariable long requestReference, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return;
        }
        vacationService.declineRequest(requestReference, currentUser.getId());
    }

    @GetMapping("/{requestReference}")
    public VacationRequest getRequest (@PathVariable long requestReference, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return null;
        }
        VacationRequest vacationRequest = vacationService.getRequestById(requestReference);
        if (vacationRequest.getEmploye().getId() != currentUser.getId() && vacationRequest.getSupervisor().getId() != currentUser.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can't access this request");
            return null;
        }

        return vacationRequest;
    }

    @GetMapping("/employeesRequests")
    public List<VacationRequest> getEmployeeRequests (HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return null;
        }
        return vacationService.getEmployeeRequests(currentUser.getId());
    }

    @GetMapping("/supervisorRequests")
    public List<VacationRequest> getSupervisorRequests (HttpServletRequest httpRequest, HttpServletResponse response) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return null;
        }
        return vacationService.getSupervisorRequests(currentUser.getId());
    }
}
