package utility;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {
   // private DateTimeFormatter dateFormatter;

  //  public DateValidator ( DateTimeFormatter dateFormatter) {
        //this.dateFormatter = dateFormatter;
   // }

    public static boolean isValid(String dateStr, DateTimeFormatter dateFormatter) {
        try {
            dateFormatter.parse(dateStr);
        } catch ( DateTimeParseException e) {
            return false;
        }
        return true;
    }
}
