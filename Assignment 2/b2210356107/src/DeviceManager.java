import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * <p>
 *     DeviceManager class is the class that stores all devices that are present in the system,
 *     contains methods that add/remove devices, fetch and sort devices in the system and alter device attributes.
 * </p>
 * @author Barış Yıldız
 * @version 1.0
 * */
public abstract class DeviceManager {

    private static final ArrayList<SmartDevice> smartDeviceList = new ArrayList<>();

    public static SmartDevice currentSmartDevice;

    /**
     * Returns whether a device is present in the system. Searches the device by name.
     * Used before setting the name of a device.
     * @param name name of the device.
     * @return true if the device exists, false otherwise.
     */
    public static boolean deviceNameExists(String name) {
        for (SmartDevice smartDevice : DeviceManager.smartDeviceList) {
            if (smartDevice.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a device to the system.
     * @param smartDevice device to be added
     */
    public static void addDevice(SmartDevice smartDevice) {
        smartDeviceList.add(smartDevice);
    }

    /**
     * Tries to fetch a device from the list of devices present.
     * @param name name of the device trying to be fetched.
     * @return the device if it exists.
     * @throws DeviceException if there is no such device named <b>name</b>
     */
    public static SmartDevice fetchDeviceFromList(String name) throws DeviceException {
        for (SmartDevice smartDevice : smartDeviceList) {
            if (smartDevice.getName().equals(name)) {
                return smartDevice;
            }
        }
        throw new DeviceException("ERROR: There is not such a device!\n");
    }

    /**
     * Removes a device from the system.
     * @param name name of the device to be removed.
     * @throws DeviceException if there is no such device named <b>name</b>
     */
    public static void removeDevice(String name) throws DeviceException {
        currentSmartDevice = fetchDeviceFromList(name);
        currentSmartDevice.setStatus("Off");
        smartDeviceList.remove(currentSmartDevice);
    }

    /**
     * Sets the switchTime of a device.
     * @param args is a string array in the form { name, switchTime }
     * @throws DeviceException if there is no device named <b>name</b> (args[0])
     * @throws TimeFormatException if <b>switchTime</b> (args[1]) is in an invalid format.
     */
    public static void setSwitchTime(String[] args) throws DeviceException, TimeFormatException {
        currentSmartDevice = fetchDeviceFromList(args[0]);
        currentSmartDevice.setSwitchTime(args[1]);
        sortBySwitchTime();
        checkWhetherDevicesNeedSwitching();
    }

    /**
     * Sets the status of a device.
     * @param args is a string array in the form { name, status }
     * @throws DeviceException if there is no such device or if given status is
     * invalid.
     */
    public static void switchDevice(String[] args) throws DeviceException {
        currentSmartDevice = fetchDeviceFromList(args[0]);
        currentSmartDevice.setStatus(args[1]);
    }

    /**
     * Changes the name of a device.
     * @param args is a string array in the form { oldName, newName }
     * @throws DeviceException if there is no such device or there is already a device
     * with the name <b>newName</b> (args[1])
     */
    public static void changeName(String[] args) throws DeviceException {
        String oldName = args[0];
        String newName = args[1];

        if (oldName.equals(newName)) {
            throw new DeviceException("ERROR: Both of the names are the same, nothing changed!\n");
        }

        currentSmartDevice = fetchDeviceFromList(oldName);
        currentSmartDevice.setName(newName);
    }

    /**
     * Plugs in an item to a smart plug.
     * @param args is a string array in the form { name(of the smart plug), ampere }
     * @throws DeviceException if there is no such device, or if given ampere value
     * is not correct, or if the smart device of name <b>name</b> is not a smart plug.
     * @throws NumberFormatException if given ampere is not a number.
     */
    public static void plugIn(String[] args) throws DeviceException, NumberFormatException {
        currentSmartDevice = fetchDeviceFromList(args[0]);

        try {
            ((SmartPlug) currentSmartDevice).plugIn(Double.parseDouble(args[1]));
        } catch (ClassCastException e){
            throw new DeviceException("ERROR: This device is not a smart plug!\n");
        }
    }

    /**
     * Plugs out an item from a smart plug.
     * @param name the name of the smart plug.
     * @throws DeviceException if there is no device named <b>name</b> or
     * if the smart device of name <b>name</b> is not a smart plug.
     */
    public static void plugOut(String name) throws DeviceException {
        currentSmartDevice = fetchDeviceFromList(name);

        try {
            ((SmartPlug) currentSmartDevice).plugOut();
        } catch (ClassCastException e) {
            throw new DeviceException("ERROR: This device is not a smart plug!\n");
        }
    }

    /**
     * Sets the kelvin value of a smart lamp.
     * @param args is a string array in the form { name of the lamp, kelvin }
     * @throws DeviceException if there is no device or if the device is not a
     * smart lamp.
     * @throws NumberFormatException if given kelvin value is not an integer.
     */
    public static void setKelvin(String[] args) throws DeviceException {
        currentSmartDevice = fetchDeviceFromList(args[0]);

        try {
            ((SmartLamp) currentSmartDevice).setKelvin(Integer.parseInt(args[1]));
        } catch (ClassCastException e) {
            throw new DeviceException("ERROR: This device is not a smart lamp!\n");
        }
    }

    /**
     * Sets the brightness of a smart lamp.
     * @param args is a string array in the form { name of the lamp, brightness }
     * @throws DeviceException if there is no device or if the device is not a
     * smart lamp.
     * @throws NumberFormatException if given brightness is not an integer.
     */
    public static void setBrightness(String[] args) throws DeviceException, NumberFormatException {
        currentSmartDevice = fetchDeviceFromList(args[0]);

        try {
            ((SmartLamp) currentSmartDevice).setBrightness(Integer.parseInt(args[1]));
        } catch (ClassCastException e) {
            throw new DeviceException("ERROR: This device is not a smart lamp!\n");
        }
    }
    /**
     * Sets the color value of a smart lamp. The given color value must be in hexadecimal form.
     * @param args is a string array in the form { name of the lamp, color }
     * @throws DeviceException if there is no device or if the device is not a
     * smart color lamp.
     */
    public static void setColorCode(String[] args) throws DeviceException {
        currentSmartDevice = fetchDeviceFromList(args[0]);

        try {
            ((SmartColorLamp) currentSmartDevice).setColor(args[1]);
        } catch (ClassCastException e) {
            throw new DeviceException("ERROR: This device is not a smart color lamp!\n");
        }
    }

    /**
     * Sets the color mode and brightness of a smart lamp. The given color value must be in kelvin form.
     * @param args is a string array in the form { name of the lamp, color, brightness }
     * @throws DeviceException if there is no device, if the device is not a
     * smart color lamp.
     * @throws NumberFormatException if given color or brightness is not an integer.
     */
    public static void setWhite(String[] args) throws DeviceException {
        try {
            new SmartLamp(new String[]{"test", "Off", args[1], args[2]});

            currentSmartDevice = fetchDeviceFromList(args[0]);
            ((SmartLamp) currentSmartDevice).setKelvin(Integer.parseInt(args[1]));
            ((SmartLamp) currentSmartDevice).setBrightness(Integer.parseInt(args[2]));
        } catch (ClassCastException e) {
            throw new DeviceException("ERROR: This device is not a smart lamp!\n");
        }
    }

    /**
     * Sets the color mode and brightness of a smart lamp. The given color value must be in hexadecimal form.
     * @param args is a string array in the form { name of the lamp, color, brightness }
     * @throws DeviceException if there is no device or if the device is not a
     * smart color lamp.
     * @throws NumberFormatException if given brightness is not an integer.
     */
    public static void setColor(String[] args) throws DeviceException {
        try {
            new SmartColorLamp(new String[]{"test", "Off", args[1], args[2]});

            currentSmartDevice = fetchDeviceFromList(args[0]);
            ((SmartColorLamp) currentSmartDevice).setColor(args[1]);
            ((SmartColorLamp) currentSmartDevice).setBrightness(Integer.parseInt(args[2]));
        } catch (ClassCastException e) {
            throw new DeviceException("ERROR: This device is not a smart color lamp!\n");
        }
    }

    /**
     * Sorts the device list based on devices' switch time.
     */
    private static void sortBySwitchTime() {

        boolean swapped = true;
        SmartDevice temp;

        // bubble sort algorithm.
        while (swapped) {
            swapped = false;
            for (int i = 0; i < smartDeviceList.size()-1; i++) {

                SmartDevice smartDevice1 = smartDeviceList.get(i);
                SmartDevice smartDevice2 = smartDeviceList.get(i+1);

                LocalDateTime time1 = smartDevice1.getSwitchTime() == null ? LocalDateTime.MAX : smartDevice1.getSwitchTime();
                LocalDateTime time2 = smartDevice2.getSwitchTime() == null ? LocalDateTime.MAX : smartDevice2.getSwitchTime();

                if (time1.isAfter(time2)) {
                    temp = smartDeviceList.get(i+1);
                    smartDeviceList.set(i+1, smartDevice1);
                    smartDeviceList.set(i, temp);
                    swapped = true;
                }
            }
        }
    }

    /**
     * Sets the current time of the system to the nearest switch time out of all assigned switch times.
     * @throws DeviceException if no switch time has been assigned.
     */
    public static void nop() throws DeviceException {
        if (smartDeviceList.size() == 0 || smartDeviceList.get(0).getSwitchTime() == null) {
            throw new DeviceException("ERROR: There is nothing to switch!\n");
        }

        String nearestSwitchTime = Time.getTimeAsString(smartDeviceList.get(0).getSwitchTime());
        Time.setCurrentTime(nearestSwitchTime);
        checkWhetherDevicesNeedSwitching();
    }

    /**
     * Compares the current time and the switch time of devices, switches the status of
     * any device whose switch time is behind the current time. Sets the switch times of those
     * devices to null afterward.
     */
    public static void checkWhetherDevicesNeedSwitching() {
        for (int i = 0; i < smartDeviceList.size(); i++) {
            LocalDateTime switchTime = smartDeviceList.get(i).getSwitchTime();

            if (switchTime == null) {
                break;
            }

            if (switchTime.isEqual(Time.getCurrentTime()) || switchTime.isBefore(Time.getCurrentTime())) {
                for (int j = i; j < smartDeviceList.size(); j++) {
                    LocalDateTime otherSwitchTime = smartDeviceList.get(j).getSwitchTime();
                    if(otherSwitchTime == null)
                        break;
                    if(otherSwitchTime.isEqual(switchTime)){
                        SmartDevice smartDevice = smartDeviceList.get(j);
                        smartDevice.setSwitchTime(null);
                        smartDevice.switchStatus();
                    }
                }
                i=-1;
            }
            sortBySwitchTime();
        }
    }

    /**
     * @return the zReport of the system, which includes the information about the current time and all devices present.
     */
    public static String zReport() {
        StringBuilder report = new StringBuilder();
        report.append("Time is:\t").append(Time.getTimeAsString(Time.getCurrentTime())).append("\n");
        for (SmartDevice smartDevice : smartDeviceList) {
            report.append(smartDevice);
        }
        return report.toString();
    }

}