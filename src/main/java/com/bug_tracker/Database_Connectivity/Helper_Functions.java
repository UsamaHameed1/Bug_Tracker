package com.bug_tracker.Database_Connectivity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Helper_Functions {

    //-------------- This Function Will Convert the Java Date To Java Local Date ---------------------------------------
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
