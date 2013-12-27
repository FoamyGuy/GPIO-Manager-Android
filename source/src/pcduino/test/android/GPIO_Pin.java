package pcduino.test.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.util.Log;

/**
 * A class to access and manage the GPIO pins of a pcDuino
 *
 * @author Mohammad Adib
 * 
 * Originally found here: https://github.com/MohammadAdib/GPIO-Manager
 * License not listed.
 * 
 * Modified: Tim Cocks 12/26/2013 
 * 
 * Changed try/catch to support java 1.6 for Android. 
 * Added flush() call to the write method
 */
public class GPIO_Pin {

    /**
     * Initialize variables
     */
    private String modeURI = "/sys/devices/virtual/misc/gpio/mode/";
    private String statusURI = "/sys/devices/virtual/misc/gpio/pin/";
    private int pin = 0;
    public static final String HIGH = "1", LOW = "0", INPUT = "0", OUTPUT = "1", INPUT_PU = "8";

    public static final String TAG = "pcDuino GPIO";
    /**
     * Constructor
     */
    public GPIO_Pin(int pin) {
        // Finalize file paths
        modeURI += "gpio" + pin;
        statusURI += "gpio" + pin;
        this.pin = pin;
    }

    /**
     * @return the specified pin number
     */
    public int getPin() {
        return pin;
    }

    /**
     * Overrides the default directory for pins and their mode/state (sys/devices/virtual/misc/gpio/)
     *
     * @param uri
     */
    public void overrideURI(String uri) {
        modeURI = uri + "mode/gpio" + pin;
        statusURI = uri + "pin/gpio" + pin;
    }

    /**
     * Sets the mode of the pin to a user-defined String
     */
    public void setMode(String mode) {
        writeToFile(getModeURI(), mode);
    }

    /**
     * Sets the state of the pin to a user-defined String
     */
    public void set(String state) {
        writeToFile(getStatusURI(), state);
    }

    /**
     * Sets the state of the pin to HIGH
     */
    public void setHIGH() {
        writeToFile(getStatusURI(), HIGH);
    }

    /**
     * Sets the state of the pin to LOW
     */
    public void setLOW() {
        writeToFile(getStatusURI(), LOW);
    }

    /**
     * Sets the mode of the pin to INPUT
     */
    public void setModeINPUT() {
        writeToFile(getModeURI(), INPUT);
    }

    /**
     * Sets the mode of the pin to OUTPUT
     */
    public void setModeOUTPUT() {
        writeToFile(getModeURI(), OUTPUT);
    }

    /**
     * Sets the mode of the pin to INPUT PULL UP
     */
    public void setModeINPUT_PU() {
        writeToFile(getModeURI(), INPUT_PU);
    }

    /**
     * @return the path to the mode file of the pin
     */
    public String getModeURI() {
        return modeURI;
    }

    /**
     * @return the path to the state file of the pin
     */
    public String getStatusURI() {
        return statusURI;
    }

    /**
     * @return the current mode of the pin
     */
    public String getPinMode() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getModeURI()));
            String data = reader.readLine();
            reader.close();
            return data;
        } catch (IOException e) {
        }
        return "";
    }

    /**
     * @return the current status of the pin
     */
    public String getPinStatus() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getStatusURI()));
            String data = reader.readLine();
            reader.close();
            return data;
        } catch (IOException e) {
        }
        return "";
    }

    /**
     * Writes the data to the specified GPIO pin file
     *
     * @param URI that path of the mode/state file of the pin
     * @param data the data to write to the file
     */
    private void writeToFile(String URI, String data) {
        try {
            // delete the file
            File file = new File(URI);
            file.delete();
            // recreate it
            File newFile = new File(URI);
            newFile.createNewFile();
            // write the specified data to it
            
        	FileWriter writer = new FileWriter(URI);
            writer.write(data);
            // had to flush to ensure file gets written
            writer.flush();
            // close the file to apply the change
            writer.close();
            Log.d(TAG, "Wrote " + "\"" + data + "\" to " + URI);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
