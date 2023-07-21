import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 *     Time class is the class where the time of the system is controlled. It contains the
 *     static attribute currentTime which represents the current time and static behaviors
 *     to format time, set time and skip time.
 * </p>
 * @author Barış Yıldız
 * @version 1.0
 * */
public class Time {
    private static LocalDateTime currentTime;

    /**
     *
     * @return the current time
     */
    public static LocalDateTime getCurrentTime() {
        return currentTime;
    }

    /**
     * Sets the currentTime static attribute. Also checks if any switch time is passed.
     * @param currentTime the time to be set.
     * @throws TimeException if currentTime comes before or is equal to the current time.
     * @throws TimeFormatException if the format of <b>currentTime</b> is invalid.
     */
    public static void setCurrentTime(String currentTime) throws TimeException, TimeFormatException {

        LocalDateTime time = returnFormattedTime(currentTime);

        if (Time.currentTime != null) {
            if (time.isBefore(Time.currentTime)) {
                throw new TimeException("ERROR: Time cannot be reversed!\n");
            } else if (time.isEqual(Time.currentTime)) {
                throw new TimeException("ERROR: There is nothing to change!\n");
            }
        }

        Time.currentTime = time;

        DeviceManager.checkWhetherDevicesNeedSwitching();
    }
    /**
     * Sets the initial time of the system. (inside currentTime)
     * Has to be the first command in the program. If it isn't the first command, or
     * If it is, but it is faulty, the program terminates.
     * @param currentTime time to be set as initial time.
     * @throws InitialTimeException if <b>currentTime</b> has invalid format.
     */
    public static void setInitialTime(String currentTime) throws InitialTimeException {
        if (Time.currentTime != null) {
            throw new InitialTimeException("ERROR: Erroneous command!\n");
        }

        try {
            setCurrentTime(currentTime);
        } catch (TimeFormatException exception) {
            throw new InitialTimeException("ERROR: Format of the initial date is wrong!");
        }
    }

    /**
     * Converts a LocalDateTime object to a string. Mostly used for printing a certain time.
     * @param time time to be converted into a string.
     * @return the string form of <b>time</b>.
     */
    public static String getTimeAsString(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'_'HH:mm:ss"));
    }

    /**
     * Returns the LocalDateTime form of a "time" string. e.g. "2023-12-03_13:40:00" to its LocalDateTime form.
     * @param time a string in the form yyyy-MM-dd_HH:mm:ss
     * @return LocalDateTime form of <b>time</b>.
     * @throws TimeFormatException if <b>time</b> is in invalid format.
     * @throws TimeException if <b>time</b> is before the current time.
     */
    public static LocalDateTime returnFormattedTime(String time) throws TimeFormatException {
        try {
            String[] yearMonth = time.split("-");
            String[] dayHour = yearMonth[2].split("_");
            String[] HourMinuteSeconds = dayHour[1].split(":");

            if (yearMonth.length != 3 || dayHour.length != 2 || HourMinuteSeconds.length != 3) {
                throw new TimeFormatException("ERROR: Time format is not correct!\n");
            }

            LocalDateTime formattedTime = LocalDateTime.of(Integer.parseInt(yearMonth[0]), Integer.parseInt(yearMonth[1]), Integer.parseInt(dayHour[0]),
                    Integer.parseInt(HourMinuteSeconds[0]), Integer.parseInt(HourMinuteSeconds[1]), Integer.parseInt(HourMinuteSeconds[2]));

            if (Time.currentTime != null && formattedTime.isBefore(Time.currentTime)) {
                throw new TimeException("ERROR: Time cannot be reversed!\n");
            }

            return formattedTime;
        } catch (DateTimeException| NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException exception) {
            throw new TimeFormatException("ERROR: Time format is not correct!\n");
        }
    }

    /**
     * Adds minutes to currentTime.
     * Added minutes must be positive.
     * @param minutes minutes to be added.
     * @throws TimeException if <b>minutes</b> is not positive.
     */
    public static void skipMinutes(String minutes) {
        int minutesAsInteger = Integer.parseInt(minutes);

        if (minutesAsInteger < 0) {
            throw new TimeException("ERROR: Time cannot be reversed!\n");
        } else if (minutesAsInteger == 0) {
            throw new TimeException("ERROR: There is nothing to skip!\n");
        }

        currentTime = currentTime.plusMinutes(Integer.parseInt(minutes));
        DeviceManager.checkWhetherDevicesNeedSwitching();
    }

}
