import java.time.LocalDateTime;
/**
 * <p>
 *     SmartDevice is the direct superclass and representative of all "smart" devices.
 *     Smart device types are : SmartLamp, SmartColorLamp, SmartCamera and SmartPlug.
 *     Smart devices can be added to the system and their attributes can be modified.
 *     Behaviors in smart devices provide ways of modifying attributes.
 * </p>
 * @author Barış Yıldız
 * @version 1.0
 * */
public abstract class SmartDevice {

    private String name;

    private String status;

    private LocalDateTime switchTime;

    /**
     *  @return the name of the smart device
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the smart device.
     * The new name has to be a non-null name which is not taken by any other device present in the system.
     * @param name the name to be set.
     * @throws DeviceException if the <b>name</b> is null or is already taken.
     */
    public void setName(String name) throws DeviceException {
        if (name == null) {
            throw new DeviceException("ERROR: Erroneous command!\n");
        }
        if (DeviceManager.deviceNameExists(name)) {
            throw new DeviceException("ERROR: There is already a smart device with same name!\n");
        }
        this.name = name;
    }
    /**
     * Returns the status ("on"/"off") of the smart device. Additionally, returns "off" if the status is null.
     * @return the string representation of the status.
     */
    public String getStatus() {
        return (status == null) ? "off" : status;
    }

    /**
     * Sets the status of the smart device. The new status must be given as "On" or "Off" (it is case-sensitive).
     * Note that the status of the smart device is stored as "on" or "off" (in lowercase) in order to make
     * the printing device information process more efficient.
     * @param status the status to be set.
     * @throws DeviceException if <b>status</b> isn't "On"/"Off" or if the device is already in that state.
     */
    public void setStatus(String status) throws DeviceException {
        if (status.equals("On") || status.equals("Off")) {
            status = status.toLowerCase();
        } else {
            throw new DeviceException("ERROR: Erroneous command!\n");
        }

        if (status.equals(this.status)) {
            throw new DeviceException("ERROR: This device is already switched " + this.status + "!\n");
        }

        this.status = status;
    }

    /**
     * @return the switch time of the smart device.
     */
    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    /**
     * Sets the switch time of the smart device.
     * The new switch time must be in the format yyyy-MM-dd_HH-mm-ss. It can also be null.
     * @param switchTime the switch time to be set.
     * @throws TimeException if <b>switchTime</b> is not in the correct format or if <b>switchTime</b> is already
     * the switch time of the smart device.
     */
    public void setSwitchTime(String switchTime) throws TimeException {

        try {
            if (switchTime != null) {
                LocalDateTime formattedTime = Time.returnFormattedTime(switchTime);
                if (this.switchTime != null && formattedTime.isEqual(this.switchTime)) {
                    throw new TimeException("ERROR: There is nothing to change!\n");
                }
                this.switchTime = formattedTime;
            }
            else {
                this.switchTime = null;
            }

        } catch (TimeException exception) {
            throw new TimeException("ERROR: Switch time cannot be in the past!\n");
        }
    }

    /**
     * Switches the status of the smart device which means it sets the device "On" if it was "Off" and
     * "Off" if it was "On" previously.
     */
    public void switchStatus() {
        if (getStatus().equals("on")) {
            setStatus("Off");
        } else if (getStatus().equals("off")) {
            setStatus("On");
        }
    }
}
