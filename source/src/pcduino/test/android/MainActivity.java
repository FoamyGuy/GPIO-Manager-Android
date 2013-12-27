package pcduino.test.android;

import com.example.pcduino.pins.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {
    /**
     * Initialize variables
     */
	private Spinner pinSpn;
	private Spinner modeSpn;
	private Spinner stateSpn;
	private Button applyBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pinSpn = (Spinner)findViewById(R.id.pinSpn);
		modeSpn = (Spinner)findViewById(R.id.modeSpn);
		stateSpn = (Spinner)findViewById(R.id.stateSpn);
		applyBtn = (Button)findViewById(R.id.applyBtn);    
		
		// When mode is not output disable the state spinner
		modeSpn.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int pos, long id) {
				if(pos != 0){
					stateSpn.setEnabled(false);
				}else{
					stateSpn.setEnabled(true);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		// Apply click 
		applyBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// create reference for the chosen pin
				GPIO_Pin pin = new GPIO_Pin(pinSpn.getSelectedItemPosition());
				
				// get mode selection
				int mode = modeSpn.getSelectedItemPosition();
				
				// set the mode on the pin
				switch (mode){
				case 0:
					pin.setModeOUTPUT();
					break;
				case 1:
					pin.setModeINPUT();
					break;
				case 2:
					pin.setModeINPUT_PU();
					break;
				}
				
				// if mode is output set the desired state
				if(mode == 0){ 
					int state = stateSpn.getSelectedItemPosition();
					switch(state){
					case 0:
						pin.setHIGH();
						break;
					case 1:
						pin.setLOW();
						break;
					}
				// if mode is input display the state in the spinner
				}else{ 
					int state = Integer.valueOf(pin.getPinStatus());
					switch(state){
					case 0:
						stateSpn.setSelection(1);
						break;
					case 1:
						stateSpn.setSelection(0);
						break;
					}
				}
			}
		});

	}

}
