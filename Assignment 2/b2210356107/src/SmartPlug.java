import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <p>
 *     SmartPlug is a type of SmartDevice. Alongside the attributes and behaviors of
 *     SmartDevice, it also has its own unique attributes and behaviors.
 *     A SmartPlug can measure its power consumption. Power consumption measurement only happens when
 *     the device is switched off or when an item is plugged out from it.
 * </p>
 * @author Barış Yıldız
 * @version 1.0
 * */
public class SmartPlug extends SmartDevice {

    private double ampere;

    private boolean isBusy = false;

    private double consumption;

    private LocalDateTime latestBusyTime;

    /**
     * The SmartPlug constructor sets the name, status (optionally) and plugged in ampere (optionally) attributes of the smart plug.
     * @param args string array in the form and order { name, status(optional), ampere(optional) }".
     * @throws DeviceException if the length of args is 4. (other size values are checked in the outer try block)
     * @throws NumberFormatException if the format of any numeric argument is invalid.
     */
    public SmartPlug(String[] args) throws DeviceException, NumberFormatException {
        int inputAmount = args.length;

        if (inputAmount == 4) {
            throw new DeviceException("ERROR: Erroneous command");
        }

        setName(args[0]);

        if (inputAmount >= 2) {
            setStatus(args[1]);
        }

        if (inputAmount == 3) {
            setAmpere(Double.parseDouble(args[2]));
        }
    }

    /**
     * Sets the status of the smart plug. Additionally, measures the energy consumption so far.
     * If the plug is switched on, and it's already busy, stores the current time into "latestBusyTime" .
     * If the plug is switched off, measures consumption by calculating the hours between "latestBusyTime" and the current time.
     * @param status the status to be set.
     */
    @Override
    public void setStatus(String status) {
        super.setStatus(status);

        if (isBusy) {
            if (status.equals("On")) {
                latestBusyTime = Time.getCurrentTime();
            } else if (latestBusyTime != null) {
                Duration duration = Duration.between(latestBusyTime, Time.getCurrentTime());
                double hours = ((double)duration.toMillis()/3600000) + ((double) duration.toDays()*24);
                consumption += 220 * ampere * hours;
                latestBusyTime = null;
            }
        }

    }

    /**
     * Sets the ampere of the plugged in item.
     * The new ampere value has to be a positive number.
     * @param ampere the ampere value to be set
     * @throws DeviceException if <b>ampere</b> is not a positive number.
     */
    public void setAmpere(double ampere) throws DeviceException {
        if (ampere <= 0) {
            throw new DeviceException("ERROR: Ampere value must be a positive number!\n");
        }

        if (getStatus().equals("on")) {
            latestBusyTime = Time.getCurrentTime();
        }

        isBusy = true;
        this.ampere = ampere;
    }

    /**
     * Plugs in an item to the smart plug, i.e. sets the ampere of the smart plug.
     * Ampere must be a positive number.
     * The smart plug shouldn't be busy, i.e. there currently shouldn't be any item plugged in.
     * @param ampere the ampere value to be set
     * @throws DeviceException if the <b>ampere</b> isn't a positive number, or if there is already an item plugged in.
     */
    public void plugIn(double ampere) throws DeviceException {
        if (isBusy) {
            throw new DeviceException("ERROR: There is already an item plugged in to that plug!\n");
        }
        setAmpere(ampere);
    }

    /**
     * Plugs out an item from the smart plug.
     */
    public void plugOut() {
        if(!isBusy){
            throw new DeviceException("ERROR: This plug has no item to plug out from that plug!\n");
        }

        // switched status 2 times in order to both update the consumption and not to change the initial status.
        switchStatus();
        switchStatus();
        isBusy = false;
    }

    /**
     *
     * @return the information about the smart plug.
     */
    @Override
    public String toString() {
        String switchTime = getSwitchTime() == null ? "null" : Time.getTimeAsString(getSwitchTime());
        return "Smart Plug " + getName() + " is " + getStatus()
                + " and consumed " + String.format("%.2f", consumption) + "W so far (excluding current device),"
                + " and its time to switch its status is " + switchTime + ".\n";
    }
}