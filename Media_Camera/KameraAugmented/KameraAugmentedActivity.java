import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class KameraAugmentedActivity extends Activity implements SensorEventListener{
    /** Called when the activity is first created. */
	
	RelativeLayout rl;
	SensorManager sm;
	MojeView myCameraOverlay;
	Preview myCameraView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        rl = (RelativeLayout)findViewById(R.id.relativeLayout1);
        
        myCameraView = new Preview(this);
        rl.addView(myCameraView);
        
        myCameraOverlay = new MojeView(this);
        rl.addView(myCameraOverlay);
        
        
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        
        
    }

    
    @Override
	protected void onPause() {
		super.onPause();
		sm.unregisterListener(this);
		//Toast.makeText(this, "KameraAugmentedActivity.onPause()\nunregisterListener", Toast.LENGTH_LONG).show();
	}


	@Override
	protected void onResume() {
		super.onResume();
		Sensor def = sm.getDefaultSensor(Sensor.TYPE_ALL);
		sm.registerListener(this, def, SensorManager.SENSOR_DELAY_NORMAL);
		//Toast.makeText(this, "KameraAugmentedActivity.onResume()\nregisterListener", Toast.LENGTH_LONG).show();
	}


	//Nadpisane z SensorEventListener
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		myCameraOverlay.setDane(event.values);
		myCameraView.invalidate();
		
	}
}