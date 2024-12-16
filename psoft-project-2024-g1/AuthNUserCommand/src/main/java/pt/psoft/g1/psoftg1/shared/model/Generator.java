package pt.psoft.g1.psoftg1.shared.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Generator {
    private static int counter = 0;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");

    public static long generateVersion() {
        // Get the current timestamp and format it as a long
        long timestamp = Long.parseLong(dateFormat.format(new Date()));

        // Increment the counter if it has not reached the maximum value
        if (counter < 99999) {
            counter++;
        }

        // Combine the timestamp and counter to create the version
        long version = timestamp * 100000L + counter;

        return version;
    }


    public static long generateLongID() {
        long timestamp = System.currentTimeMillis();
        long random = (long) (Math.random() * Long.MAX_VALUE);
        return timestamp + random;
    }
}