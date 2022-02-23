package helper;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class dateTime {


    /**getTimeStamp.
     * This will get the current Timestamp in UTC. */
    public static Timestamp getTimeStamp() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        Timestamp timeStamp = Timestamp.valueOf(localDateTime);
        return timeStamp;
    }

    /**getDate.
     * This will get the current date. */
    public static java.sql.Date getDate() {
        java.sql.Date date = java.sql.Date.valueOf(LocalDate.now());
        return date;
    }

}
