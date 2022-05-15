package tn.esprit.spring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.services.ITimesheetService;
import tn.esprit.spring.util.TimeSheetUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/timesheets")
public class TimeSheetController {

    @Autowired
    private ITimesheetService timeSheetManager;

    @PostMapping("/addMission")
    private void addMission(HttpServletRequest httpRequest,HttpServletResponse response, @RequestBody Mission mission) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return;
        }

        if (!currentUser.getRole().equals(Role.CHEF_DEPARTEMENT)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have priviliges");
            return;
        }
        // check department
        timeSheetManager.addMission(mission);
    }

    @PostMapping("{missionId}/assignMission")
    private void assignMissionToEmployees (HttpServletRequest httpRequest, HttpServletResponse response, @RequestBody List<Employe> employes, @PathVariable int missionId, @RequestParam Date from, @RequestParam Date to) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (currentUser == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentification failed");
            return;
        }

        if (!currentUser.getRole().equals(Role.CHEF_DEPARTEMENT)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have priviliges");
            return;
        }


    }

    @PostMapping("/startMission")
    private void launchMission (HttpServletRequest request, @PathVariable Date from, @PathVariable Date to) {


    }

    @GetMapping("/findByMission")
    private List<Timesheet> findByMission (HttpServletRequest request, @PathVariable int missionId) {
        return Collections.emptyList();
    }

    @GetMapping("/findByEmployee")
    private List<Timesheet> findByEmployee (HttpServletRequest request, @PathVariable int employeeId) {
        return Collections.emptyList();
    }

    @GetMapping("/findBetween")
    private List<Timesheet> findByMission (HttpServletRequest request, @PathVariable Date from, @PathVariable Date to) {
        return Collections.emptyList();
    }
}
