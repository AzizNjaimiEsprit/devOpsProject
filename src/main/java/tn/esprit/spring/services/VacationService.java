package tn.esprit.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Vacation;
import tn.esprit.spring.entities.VacationRequest;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.repository.VacationRepository;
import tn.esprit.spring.repository.VacationRequestRepository;
import tn.esprit.spring.util.TimeSheetUtility;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacationService implements IVacationService{

    @Autowired
    private VacationRequestRepository vacationRequestManager;

    @Autowired
    private VacationRepository vacationManager;

    @Autowired
    private TimesheetRepository timesheetManager;

    @Override
    public void submitRequest(VacationRequest request) {
        Optional.ofNullable(request).ifPresent(req -> {
            if (TimeSheetUtility.getHolidayBalance(req.getEmploye()) < TimeSheetUtility.diffBetweenDates(req.getFrom(), req.getTo()))
                req.setApproved(Boolean.FALSE);
            vacationRequestManager.save(req);
        });
    }

    @Override
    public void acceptRequest(long requestReference, int userId) {
        VacationRequest request = vacationRequestManager.getOne(requestReference);
        Optional.ofNullable(request).ifPresent(req -> {
            if (userId != req.getSupervisor().getId())
                throw new RuntimeException("Unauthorized action");
            req.setApproved(Boolean.TRUE);
            saveVacation(req);
            vacationRequestManager.save(req);
        });
    }

    private void saveVacation (VacationRequest request) {
        Vacation vacation = new Vacation();
        vacation.setEmployeeId(request.getEmploye());
        vacation.setFrom(request.getFrom());
        vacation.setTo(request.getTo());
        vacationManager.save(vacation);
    }

    @Override
    public void declineRequest(long requestReference, int userId) {
        VacationRequest request = vacationRequestManager.getOne(requestReference);
        Optional.ofNullable(request).ifPresent(req -> {
            if (userId != req.getSupervisor().getId())
                throw new RuntimeException("Unauthorized action");
            req.setApproved(Boolean.FALSE);
            vacationRequestManager.save(req);
        });
    }

    private VacationRequest getFullRequest (VacationRequest request) {
        if (request == null)
            return null;
        request.setOverlappingTimeSheets(timesheetManager.getOverlappingTimeSheets(request.getEmploye().getId(), request.getFrom(), request.getTo()));
        request.setOverlapTimeSheet(request.getOverlappingTimeSheets().isEmpty() ? Boolean.FALSE : Boolean.TRUE);
        return request;
    }
    @Override
    public VacationRequest getRequestById(long requestReference) {
        return getFullRequest(vacationRequestManager.findById(requestReference).get());
    }

    @Override
    public List<VacationRequest> getSupervisorRequests(int supervisorId) {
        return vacationRequestManager.findVacationRequestsBySupervisor_Id(supervisorId).stream().map(req -> getFullRequest(req)).collect(Collectors.toList());
    }

    @Override
    public List<VacationRequest> getEmployeeRequests(int empId) {
        return vacationRequestManager.findVacationRequestsByEmploye_Id(empId).stream().map(req -> getFullRequest(req)).collect(Collectors.toList());
    }

    @Override
    public List<Vacation> getEmployeeVacationList(long employeeId) {
        return (List<Vacation>) vacationManager.findAll();
    }
}
