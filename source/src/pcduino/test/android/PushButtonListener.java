package pcduino.test.android;

import android.os.Handler;

/**
 * A class to provide an Android like interface to get button
 * or momentary switch click events.
 *
 * @author Tim Cocks
 */
public class PushButtonListener {
    /**
     * Initialize variables
     */
	private GPIO_Pin mPin;
	private Thread listeningThread;
	public static final String PRESSED = "0", NOT_PRESSED = "1";
	private String curStatus = NOT_PRESSED;
	private static Handler h = new Handler();
	private boolean listening = false;
	private Runnable buttonPressedRun;
	
	
    /**
     * Constructor
     */
	public PushButtonListener(GPIO_Pin pin){
		// Set the pin
		this.mPin = pin;
		//Set up our pressed runnable.
		buttonPressedRun = new Runnable(){
			public void run(){
				onButtonPressed();
			}
		};
		
		// Listener thread that will monitor the pin status
		listeningThread = new Thread(){
			public void run(){
				// get the new status
				String newStatus = mPin.getPinStatus();
				
				// If it new is pressed, and old is not pressed
				if(curStatus.equals(NOT_PRESSED) && newStatus.equals(PRESSED)){
					// Make the callback
					h.post(buttonPressedRun);
				}
				// set old status for next time
				curStatus = newStatus;
				
				// post the next iteration
				if(listening){
					h.postDelayed(this, 100);
				}
				
			}
		};
	}
	
    /**
     * Start listening on the given pin.
     * Be sure to call stopListening() when
     * you are done!
     * 
     */
	public void listen(){
		listening = true;
		h.post(listeningThread);
	}
	
	/**
     * Stop listening on the given pin
     */
	public void stopListening(){
		listening = false;
		h.removeCallbacks(listeningThread);
	}
	
	/**
     * Callback method. Does nothing by default.
     * Override this to take whatever action you wish.
     */
	public void onButtonPressed(){
		
	}
	

}
