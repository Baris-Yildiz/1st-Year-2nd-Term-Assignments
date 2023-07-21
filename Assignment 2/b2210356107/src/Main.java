import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        String inputFile = "testInput";//args[0];
        String outputFile = "out.txt";//args[1];

        StringBuilder stringBuilder = new StringBuilder();

        // mapping commands to unary methods (methods that take only one argument)
        HashMap<String, Consumer<String>> unaryMethodMap = new HashMap<String, Consumer<String>>(){{
            put("SetInitialTime", Time::setInitialTime);
            put("SetTime", Time::setCurrentTime);
            put("SkipMinutes", Time::skipMinutes);
            put("PlugOut", DeviceManager::plugOut);
            put("Remove", DeviceManager::removeDevice);
        }};

        // mapping commands to binary methods (methods that take two arguments)
        HashMap<String, Consumer<String[]>> binaryMethodMap = new HashMap<String, Consumer<String[]>>(){{
            put("SetSwitchTime", DeviceManager::setSwitchTime);
            put("Switch", DeviceManager::switchDevice);
            put("ChangeName", DeviceManager::changeName);
            put("PlugIn", DeviceManager::plugIn);
            put("SetKelvin", DeviceManager::setKelvin);
            put("SetBrightness", DeviceManager::setBrightness);
            put("SetColorCode", DeviceManager::setColorCode);
        }};

        // mapping commands to ternary methods (methods that take three arguments)
        HashMap<String, Consumer<String[]>> ternaryMethodMap = new HashMap<String, Consumer<String[]>>(){{
            put("SetWhite", DeviceManager::setWhite);
            put("SetColor", DeviceManager::setColor);
        }};

        try {
            String[] inputLines = FileIO.readFile(inputFile);

            String[] lineContents;
            String command;
            String[] otherArguments;
            boolean programStarted = false;
            boolean printedZReport = false;
            boolean initialTimeSet = false;


            for (String inputLine : inputLines) {
                printedZReport = false;
                lineContents = inputLine.trim().split("\t");
                command = lineContents[0];
                otherArguments = Arrays.copyOfRange(lineContents, 1, lineContents.length);

                try {
                    if (!command.equals("")) {
                        stringBuilder.append("COMMAND: ").append(inputLine).append("\n");

                        if (unaryMethodMap.containsKey(command)) {

                            if (otherArguments.length != 1) {
                                if (!initialTimeSet && command.equals("SetInitialTime")) {
                                    throw new InitialTimeException("ERROR: First command must be set initial time!");
                                }
                                throw new DeviceException("ERROR: Erroneous command!\n");
                            }

                            unaryMethodMap.get(command).accept(lineContents[1]);

                            if (command.equals("SetInitialTime")) {
                                stringBuilder.append("SUCCESS: Time has been set to ")
                                        .append(Time.getTimeAsString(Time.getCurrentTime())).append("!\n");
                                initialTimeSet = true;
                            } else if (command.equals("Remove")) {
                                stringBuilder.append("SUCCESS: Information about removed smart device is as follows:\n")
                                        .append(DeviceManager.currentSmartDevice);
                            }

                        } else if (binaryMethodMap.containsKey(command)) {

                            if (otherArguments.length != 2) {
                                throw new DeviceException("ERROR: Erroneous command!\n");
                            }

                            binaryMethodMap.get(command).accept(otherArguments);

                        } else if (ternaryMethodMap.containsKey(command)) {

                            if (otherArguments.length != 3) {
                                throw new DeviceException("ERROR: Erroneous command!\n");
                            }

                            ternaryMethodMap.get(command).accept(otherArguments);

                        } else if (command.equals("Add")) {
                            String deviceType = lineContents[1];
                            String[] deviceProperties = Arrays.copyOfRange(otherArguments, 1, lineContents.length-1);

                            if (deviceProperties.length == 0 || deviceProperties.length > 4) {
                                throw new DeviceException("ERROR: Erroneous command!\n");
                            }

                            switch (deviceType) {
                                case "SmartPlug":
                                    DeviceManager.addDevice(new SmartPlug(deviceProperties));
                                    break;
                                case "SmartCamera":
                                    DeviceManager.addDevice(new SmartCamera(deviceProperties));
                                    break;
                                case "SmartLamp":
                                    DeviceManager.addDevice(new SmartLamp(deviceProperties));
                                    break;
                                case "SmartColorLamp":
                                    DeviceManager.addDevice(new SmartColorLamp(deviceProperties));
                                    break;
                                default:
                                    throw new DeviceException("ERROR: Erroneous command!\n");
                            }

                        } else if (command.equals("ZReport")) {

                            if (otherArguments.length != 0) {
                                throw new DeviceException("ERROR: Erroneous command!\n");
                            }

                            stringBuilder.append(DeviceManager.zReport());
                            printedZReport = true;

                        } else if (command.equals("Nop")) {

                            if (otherArguments.length != 0) {
                                throw new DeviceException("ERROR: Erroneous command!\n");
                            }

                            DeviceManager.nop();

                        } else {
                            stringBuilder.append("ERROR: Erroneous command!\n");
                        }

                        if (!programStarted) {
                            if (!command.equals("SetInitialTime")) {
                                throw new InitialTimeException("ERROR: First command must be set initial time!");
                            }
                            programStarted = true;
                        }
                    }
                } catch (DeviceException | TimeException | TimeFormatException exception) {
                    stringBuilder.append(exception.getMessage());
                } catch (NumberFormatException exception) {
                    stringBuilder.append("ERROR: Erroneous command!\n");
                } catch (InitialTimeException exception) {
                    stringBuilder.append(exception.getMessage());
                    if (!initialTimeSet) {
                        stringBuilder.append(" Program is going to terminate!\n");
                        break;
                    }
                }

            }

            if (!printedZReport && initialTimeSet) {
                stringBuilder.append("ZReport:\n").append(DeviceManager.zReport());
            }


        } catch (NullPointerException | IOException exception) {
            stringBuilder.append("cannot find the specified input file.");
        }

        FileIO.writeFile(outputFile,stringBuilder.toString());
    }
}
