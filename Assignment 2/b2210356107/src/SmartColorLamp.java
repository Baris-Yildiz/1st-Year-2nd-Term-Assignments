/**
 * <p>
 *     SmartColorLamp is a type of SmartLamp. Alongside the attributes and behaviors of
 *     SmartLamp, it also has a color value and color behaviors.
 *     A SmartColorLamp is a special kind of SmartLamp in that it has the "color mode"
 *     as an alternative to kelvin mode.
 *     A SmartColorLamp can be configured to use kelvin or color mode to determine its color.
 * </p>
 * @author Barış Yıldız
 * @version 1.0
 * */
public class SmartColorLamp extends SmartLamp {

    private String color;

    /**
     * The SmartColorLamp constructor sets the name, status (optionally), color (optionally) and brightness (optionally)
     * attributes of the smart lamp.
     * The color can be given in kelvins (integer) or as a string that represents a hexadecimal number.
     * @param args string array in the form and order { name, status(optional), color(optional), brightness(optional) }".
     * @throws DeviceException if the length of args is 3. (other size values are checked in the outer try block)
     * @throws NumberFormatException if the format of any numeric argument is invalid.
     */
    public SmartColorLamp(String[] args) throws DeviceException, NumberFormatException {
        super(args);

        if (args.length == 4) {
            if (args[2].startsWith("0x")) {
                setColor(args[2]);
            } else {
                setKelvin(Integer.parseInt(args[2]));
            }
            setBrightness(Integer.parseInt(args[3]));
        }
    }

    /**
     * Sets the color of the smart colored lamp.
     * The color to be set must be a string that represents a hexadecimal number
     * in the range [0x000000 - 0xFFFFFF].
     * @param color color to be set.
     * @throws DeviceException if <b>color</b> is out of bounds.
     * @throws NumberFormatException if <b>color</b> is in invalid format.
     */
    public void setColor(String color) throws DeviceException, NumberFormatException {
        int decimalColorValue = Integer.parseInt(color.substring(2),16);

        if (decimalColorValue < 0 || decimalColorValue > 16777215) {
            throw new DeviceException("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
        }

        this.color = color;
    }

    /**
     * @return information about the smart colored lamp.
     */
    @Override
    public String toString() {
        String switchTime = getSwitchTime() == null ? "null" : Time.getTimeAsString(getSwitchTime());
        String colorOrKelvin = color == null ? getKelvin() + "K" : color;

        return "Smart Color Lamp " + getName() + " is " + getStatus()
                + " and its color value is " + colorOrKelvin + " with "
                + getBrightness() + "% brightness, and its time to switch its status is "
                + switchTime + ".\n";

    }

}
