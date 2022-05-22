package tn.esprit.spring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.ITimesheetService;
import tn.esprit.spring.util.TimeSheetUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/timesheets")
public class TimeSheetController {

    @Autowired
    private ITimesheetService timeSheetManager;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @PostMapping("/addMission")
    private void addMission(HttpServletRequest httpRequest,HttpServletResponse response, @RequestBody Mission mission) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (Objects.isNull(currentUser) || !currentUser.getRole().equals(Role.CHEF_DEPARTEMENT) || !TimeSheetUtility.isMissionAccessible(currentUser, mission.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have priviliges");
            return;
        }

        // check department
        timeSheetManager.addMission(mission);
    }

    @PostMapping("{missionId}/assignMission")
    private void assignMissionToEmployees (HttpServletRequest httpRequest, HttpServletResponse response, @RequestBody List<Employe> employees, @PathVariable int missionId, @RequestParam Date from, @RequestParam Date to) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(httpRequest);
        if (Objects.isNull(currentUser) || !currentUser.getRole().equals(Role.CHEF_DEPARTEMENT) || !TimeSheetUtility.isMissionAccessible(currentUser, missionId)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have priviliges");
            return;
        }

        if (employees == null || employees.isEmpty())
            return;

        employees.stream().forEach(employe -> timeSheetManager.addTimeSheet(missionId, employe.getId(), from, to));
    }

    @GetMapping("/findByMission")
    private List<Timesheet> findByMission (HttpServletRequest request, HttpServletResponse response, @PathVariable int missionId) throws IOException {
        Employe currentUser = TimeSheetUtility.basicAuth(request);
        if (currentUser == null || !TimeSheetUtility.isMissionAccessible(currentUser, missionId))
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have priviliges");

        return timesheetRepository.findTimesheetsByMission_Id(missionId);
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
