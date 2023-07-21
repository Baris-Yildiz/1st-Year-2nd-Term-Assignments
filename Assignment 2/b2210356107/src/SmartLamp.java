/**
 * <p>
 *     SmartLamp is a type of SmartDevice. Alongside the attributes and behaviors of
 *     SmartDevice, it also has its own unique attributes and behaviors.
 *     A SmartLamp has kelvin (color, indirectly) and brightness values which can be adjusted to the user's liking.
 * </p>
 * @author Barış Yıldız
 * @version 1.0
 * */
public class SmartLamp extends SmartDevice {

    private int kelvin = 4000;

    private int brightness = 100;

    /**
     * The SmartLamp constructor sets the name, status (optionally), kelvin (optionally) and brightness (optionally)
     * attributes of the smart lamp.
     * @param args string array in the form and order { name, status(optional), kelvin(optional), brightness(optional) }".
     * @throws DeviceException if the length of args is 3. (other size values are checked in the outer try block)
     * @throws NumberFormatException if the format of any numeric argument is invalid.
     */
    public SmartLamp(String[] args) throws DeviceException, NumberFormatException {
        int inputAmount = args.length;

        if (inputAmount == 3) {
            throw new DeviceException("ERROR: Erroneous command!\n");
        }

        setName(args[0]);

        if (inputAmount >= 2) {
            setStatus(args[1]);
        }

        if (inputAmount == 4 && !(this instanceof SmartColorLamp)) {
            setKelvin(Integer.parseInt(args[2]));
            setBrightness(Integer.parseInt(args[3]));
        }
    }

    /**
     * @return the kelvin value of the smart lamp.
     */
    public int getKelvin() {
        return kelvin;
    }

    /**
     * Sets the kelvin value of the smart lamp.
     * The kelvin value must be in the inclusive range [2000K - 6500K].
     * @param kelvin the kelvin value to be set.
     * @throws DeviceException if <b>kelvin</b> is out of bounds.
     */
    public void setKelvin(int kelvin) throws DeviceException {
        if (kelvin < 2000 || kelvin > 6500) {
            throw new DeviceException("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
        }
        this.kelvin = kelvin;
    }

    /**
     * @return the brightness of the smart lamp.
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Sets the brightness of the smart lamp.
     * The brightness to be set must be in the inclusive range [0 - 100].
     * @param brightness brightness value to be set.
     * @throws DeviceException if <b>brightness</b> is out of bounds.
     */
    public void setBrightness(int brightness) throws DeviceException {
        if (brightness < 0 || brightness > 100) {
            throw new DeviceException("ERROR: Brightness must be in range of 0%-100%!\n");
        }
        this.brightness = brightness;
    }

    /**
     * @return the information about the smart lamp.
     */
    @Override
    public String toString() {
        String switchTime = getSwitchTime() == null ? "null" : Time.getTimeAsString(getSwitchTime());
        return "Smart Lamp " + getName() +
                " is " + getStatus() +
                " and its kelvin value is " + getKelvin() + "K with "
                + getBrightness() + "% brightness, and its time to switch its status is "
                + switchTime + ".\n";
    }

}
