package tn.esprit.spring.services;

import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Vacation;
import tn.esprit.spring.entities.VacationId;
import tn.esprit.spring.entities.VacationRequest;

import java.util.List;

public interface IVacationService {
    void submitRequest(VacationRequest request);
    void acceptRequest(long requestReference, int userId);
    void declineRequest(long requestReference, int userId);
    VacationRequest getRequestById(long requestReference);
    List<VacationRequest> getSupervisorRequests (int supervisorId);
    List<VacationRequest> getEmployeeRequests (int empId);
    List<Vacation> getEmployeeVacationList (long employeeId);
}
