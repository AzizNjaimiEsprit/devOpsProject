package tn.esprit.spring;


import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Vacation;
import tn.esprit.spring.entities.VacationRequest;
import tn.esprit.spring.repository.VacationRepository;
import tn.esprit.spring.repository.VacationRequestRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IVacationService;
import tn.esprit.spring.services.VacationService;
import tn.esprit.spring.util.TestUtility;
import tn.esprit.spring.util.TimeSheetUtility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class VacationTest {
    @Mock
    private static Employe employe;

    @Mock
    private static Employe supervisor;

    @InjectMocks
    private static IVacationService vacationServiceMock;

    private static VacationService vacationService;

    @InjectMocks
    private static IEmployeService employeService;

    @InjectMocks
    private static VacationRequestRepository vacationRequestRepository;

    private static List<VacationRequest> vacationsRequests;
    private static VacationRequest request;
    @BeforeAll
    public static void setUpMocks () {
        employe = Mockito.mock(Employe.class);
        supervisor = Mockito.mock(Employe.class);
        request = new VacationRequest(employe, supervisor, TestUtility.dateGenerator(10, 10, 2022), TestUtility.dateGenerator(15, 10, 2022));
        request.setId(1);
        supervisor.setId(2);
        vacationsRequests = Arrays.asList(new VacationRequest(employe, supervisor, TestUtility.dateGenerator(15,10,2022), TestUtility.dateGenerator(20,10,2022)),
                new VacationRequest(employe, supervisor, TestUtility.dateGenerator(1,9,2022), TestUtility.dateGenerator(7,9,2022)));
        vacationServiceMock = Mockito.mock(IVacationService.class);
        employeService = Mockito.mock(IEmployeService.class);
        //vacationService = new VacationService();
        vacationRequestRepository = Mockito.mock(VacationRequestRepository.class);
        TimeSheetUtility.injectVacationService(vacationServiceMock);
        TimeSheetUtility.injectEmployeeService(employeService);
    }

    @Test
    void holidayBalanceTest () {
        Mockito.when(employe.getId()).thenReturn(1);
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(TestUtility.generateVacation(employe, TestUtility.dateGenerator(1,1,2022), TestUtility.dateGenerator(5,1,2022)));
        vacations.add(TestUtility.generateVacation(employe, TestUtility.dateGenerator(4,6,2022), TestUtility.dateGenerator(8,6,2022)));
        Mockito.when(employe.getJoinDate()).thenReturn(TimeSheetUtility.asDate(LocalDateTime.now().minusYears(2)));
        Mockito.when(vacationServiceMock.getEmployeeVacationList(employe.getId())).thenReturn(vacations);

        Assertions.assertEquals(Math.ceil(TimeSheetUtility.getHolidayBalance(employe)), 22);
    }

    @Test
    void findRequestTest () {
        vacationService = new VacationService();
        vacationService.vacationRequestManager = vacationRequestRepository;
        Mockito.when(vacationRequestRepository.findById(request.getId())).thenReturn(java.util.Optional.ofNullable(request));
        Assertions.assertNotNull(vacationService);
        Assertions.assertNotNull(request);
        Assertions.assertEquals(request, vacationService.getRequestById(request.getId()));
    }

    @Test
    void getRequestsByEmployeeTest () {
        vacationService = new VacationService();
        vacationService.vacationRequestManager = vacationRequestRepository;
        Mockito.when(vacationRequestRepository.findVacationRequestsByEmploye_Id(employe.getId())).thenReturn(vacationsRequests);
        Assertions.assertEquals(vacationsRequests, vacationService.getEmployeeRequests(employe.getId()));
    }
}
