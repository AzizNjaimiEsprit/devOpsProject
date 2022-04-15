package tn.esprit.spring.util;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.services.IVacationService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeSheetUtility {

    private static final LocalDateTime defaultStartDate = LocalDateTime.now().minusYears(2).with(LocalTime.MIN);

    @Autowired
    private static IVacationService vacationManager;

    public static double getHolidayBalance(Employe employe) {
        return (ChronoUnit.MONTHS.between(LocalDateTime.now(), employe.getJoinDate() == null ? defaultStartDate : asLocalDate(employe.getJoinDate())) * 1.23)
                - (vacationManager.getEmployeeVacationList(employe.getId())
                    .stream()
                    .mapToLong(vacation -> diffBetweenDates(vacation.getFrom(), vacation.getTo()))
                    .sum());
    }

    public static long diffBetweenDates(Date from, Date to) {
        return ChronoUnit.DAYS.between(asLocalDate(to), asLocalDate(from));
    }

    public static LocalDateTime asLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
