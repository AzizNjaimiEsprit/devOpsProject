package tn.esprit.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IVacationService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class TimeSheetUtility {

    private static final LocalDateTime defaultStartDate = LocalDateTime.now().minusYears(2).with(LocalTime.MIN);
    private static IEmployeService employeeManager;
    private static IVacationService vacationManager;
    private static MissionRepository missionManager;
    private static DepartementRepository departementManager;

    private static final Logger logger = LoggerFactory.getLogger(TimeSheetUtility.class);


    @Autowired
    private IVacationService vacationBean;
    @Autowired
    private IEmployeService employeeBean;

    public static Employe basicAuth(HttpServletRequest request) {
        String auth = request.getHeaders("Authorization").nextElement();
        String user = new String(Base64.getDecoder().decode(auth.split(" ", 2)[1]));
        List<String> credentials = Arrays.asList(user.split(":"));
        if (credentials.size() < 2) {
            logger.error("Authentication : missing credentials");
            return null;
        }

        Employe authenticatedEmp = employeeManager.authenticate(credentials.get(0), credentials.get(1));
        if (authenticatedEmp == null)
            logger.error(String.format("Authentication failed for user %s", credentials.get(0)));

        return authenticatedEmp;
    }

    public static double getHolidayBalance(Employe employe) {
        return (ChronoUnit.MONTHS.between(employe.getJoinDate() == null ? defaultStartDate : asLocalDate(employe.getJoinDate()), LocalDateTime.now()) * 1.23)
                - (vacationManager.getEmployeeVacationList(employe.getId())
                .stream()
                .mapToLong(vacation -> diffBetweenDates(vacation.getFrom(), vacation.getTo()))
                .sum());
    }

    public static boolean isMissionAccessible(Employe employe, int missionId) {
        Mission mission = missionManager.findById(missionId).get();

        if (Objects.isNull(mission) || Objects.isNull(employe))
            return Boolean.FALSE;

        if (departementManager.findDepartementsBySupervisor(employe.getId()).stream().filter(dep -> mission.getDepartement().getId() == dep.getId()).findFirst().orElse(null) == null && employe.getDepartements().stream().filter(dep -> dep.getId() == mission.getDepartement().getId()).findFirst().orElse(null) == null)
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    public static long diffBetweenDates(Date from, Date to) {
        return ChronoUnit.DAYS.between(asLocalDate(from), asLocalDate(to));
    }

    public static LocalDateTime asLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date asDate (LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    @PostConstruct
    private void init() {
        employeeManager = this.employeeBean;
        vacationManager = this.vacationBean;
    }

    public static void injectVacationService (IVacationService vacationService) {
        vacationManager = vacationService;
    }

    public static void injectEmployeeService (IEmployeService employeService) {
        employeeManager = employeService;
    }

}
