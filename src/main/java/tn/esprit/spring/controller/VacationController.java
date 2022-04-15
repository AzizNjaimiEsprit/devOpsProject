package tn.esprit.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.VacationRequest;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IVacationService;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/vacations")
public class VacationController {

    private Logger logger = LoggerFactory.getLogger(VacationController.class);

    @Autowired
    private IEmployeService employeService;

    @Autowired
    private IVacationService vacationService;

    @PostMapping("/submitRequest")
    public void submitVacationRequest (@RequestBody VacationRequest request) {

        //vacationManager.submitRequest(request);
    }

    @PostMapping("/{requestReference}/acceptRequest")
    public void acceptRequest (@PathVariable long requestReference, @RequestParam int userId) {
        //vacationManager.acceptRequest(requestReference, userId);
    }

    @PostMapping("/{requestReference}/declineRequest")
    public void declineRequest (@PathVariable long requestReference, @RequestParam int userId) {

      //  vacationManager.declineRequest(requestReference, userId);
    }

    @GetMapping("/{requestReference}")
    public VacationRequest getRequest (HttpServletRequest httpRequest, @PathVariable long requestReference) {
      //  return vacationManager.getRequestById(requestReference);
        String auth = httpRequest.getHeaders("Authorization").nextElement();
        logger.info(new String(Base64.getDecoder().decode(auth.split(" ", 2)[1])));
        return null;
    }

    @GetMapping("/employeesRequests")
    public List<VacationRequest> getEmployeeRequests (@RequestParam int userId) {
      //  return vacationManager.getEmployeeRequests(userId);
        return null;
    }

    @GetMapping("/supervisorRequests")
    public List<VacationRequest> getSupervisorRequests (@RequestParam int userId) {
        //return vacationManager.getSupervisorRequests(userId);
        return null;
    }
}
