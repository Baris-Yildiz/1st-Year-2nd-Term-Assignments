import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <p>
 *     SmartCamera is a type of SmartDevice. Alongside the attributes and behaviors of
 *     SmartDevice, it also has its own unique attributes and behaviors.
 *     A SmartCamera can measure how much storage it uses from the time it was switched on.
 * </p>
 * @author Barış Yıldız
 * @version 1.0
 * */
public class SmartCamera extends SmartDevice {

    private double MBPerMinute;

    private double usedStorage;

    private LocalDateTime latestStartTime;

    /**
     * The SmartCamera constructor sets the name, mb/minute and status (optionally) attributes of the smart camera.
     * @param args string array in the form and order { name, mb/minute, status(optional) }".
     * @throws DeviceException if the length of args is not 2 or 3.
     * @throws NumberFormatException if the format of any numeric argument is invalid.
     */
    public SmartCamera(String[] args) throws DeviceException{
        int inputAmount = args.length;

        if (inputAmount < 2 || inputAmount > 3) {
            throw new DeviceException("ERROR: Erroneous command!\n");
        }

        setName(args[0]);
        setMBPerMinute(Double.parseDouble(args[1]));

        if (inputAmount == 3) {
            setStatus(args[2]);
        }
    }

    /**
     * Sets the megabytes per minute (MB/minute) attribute of the smart camera.
     * The new MB/minute value has to be a positive value.
     * @param MBPerMinute the MB/minute to be set.
     * @throws DeviceException if <b>MBPerMinute</b> is not a positive number.
     */
    public void setMBPerMinute(double MBPerMinute) throws DeviceException {
        if (MBPerMinute <= 0) {
            throw new DeviceException("ERROR: Megabyte value must be a positive number!\n");
        }
        this.MBPerMinute = MBPerMinute;
    }

    /**
     * Sets the status of the smart camera and measures consumption (used storage).
     * If the smart camera was switched on, stores the current time in the attribute "latestStartTime".
     * If the camera was switched off, measures used storage by calculating the minutes between "latestStartTime" and the current time.
     * @param status the status to be set.
     */
    @Override
    public void setStatus(String status) {
        super.setStatus(status);

        if (status.equals("On")) {
            latestStartTime = Time.getCurrentTime();
        } else if (latestStartTime != null) {
            Duration duration = Duration.between(latestStartTime, Time.getCurrentTime());
            double minutes = ((double)duration.toMillis()/60000) + ((double) duration.toDays()*24*60);
            usedStorage += MBPerMinute * minutes;
            latestStartTime = null;
        }
    }

    /**
     * @return the information about the smart camera.
     */
    @Override
    public String toString() {
        String switchTime = getSwitchTime() == null ? "null" : Time.getTimeAsString(getSwitchTime());
        return "Smart Camera " + getName() + " is " + getStatus()
                + " and used " + String.format("%.2f", usedStorage) + " MB of storage so far (excluding current status),"
                + " and its time to switch its status is " + switchTime + ".\n";
    }

}
