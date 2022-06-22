package tn.esprit.spring.util;

import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Vacation;
import tn.esprit.spring.entities.VacationId;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TestUtility {
    private static Random random = new Random();
    public static Date dateGenerator (int day, int month, int year) {
        return TimeSheetUtility.asDate(LocalDateTime.now().withYear(year).withMonth(month).withDayOfMonth(day).with(LocalTime.MIN));
    }

    public static Vacation generateVacation (Employe employe, Date from, Date to) {
        return new Vacation(employe, from, to);
    }
}
