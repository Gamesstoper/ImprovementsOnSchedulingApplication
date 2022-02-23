package test;

import model.functions;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.Assert.*;

public class functionsTest {
    private LocalDateTime localTime = LocalDateTime.now(ZoneId.systemDefault());
    private static final ZoneId utcZoneID = ZoneId.of("UTC");
    private static final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime utc = LocalDateTime.now(utcZoneID);


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void utcToLocalTest() {
        try {
            String time = functions.utcToLocal(utc);
            LocalDateTime localTime = LocalDateTime.now(ZoneId.systemDefault());
            String localTimeString = localTime.format(datetimeDTF);
            System.out.println(time);
            System.out.println(localTimeString);
            for (int i = 0; i < time.length(); i++) {
                System.out.println(time.charAt(i));
                if (time.charAt(i) == localTimeString.charAt(i) ) {

                } else {
                    fail("Was not able to convert to Local Time");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void localToUTCTest() {
        try {
            String time = functions.localToUTC(localTime);
            LocalDateTime utc = LocalDateTime.now(utcZoneID);
            String utcTimeString = utc.format(datetimeDTF);
            System.out.println(time);
            System.out.println(utcTimeString);
            for (int i = 0; i < time.length(); i++) {
                if (time.charAt(i) == utcTimeString.charAt(i)) {

                } else {
                    fail("Was not able to convert to UTC Time");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}