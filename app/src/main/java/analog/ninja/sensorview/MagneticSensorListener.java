package analog.ninja.sensorview;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

class MagneticSensorEventListener implements SensorEventListener {
    TextView outputX;
    TextView outputY;
    TextView outputZ;
    TextView outputXMax;
    TextView outputYMax;
    TextView outputZMax;

    public MagneticSensorEventListener(TextView x, TextView y, TextView z, TextView xMax, TextView yMax, TextView zMax) {
        outputX = x;
        outputY = y;
        outputZ = z;
        outputXMax = xMax;
        outputYMax = yMax;
        outputZMax = zMax;
    }

    public void onAccuracyChanged(Sensor s, int i) {
    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            float currentXMax =  Float.parseFloat(outputXMax.getText().toString());
            float currentYMax =  Float.parseFloat(outputXMax.getText().toString());
            float currentZMax =  Float.parseFloat(outputXMax.getText().toString());

            if(Math.abs(se.values[0])> Math.abs(currentXMax) )
                outputXMax.setText(String.format("%.2f", Math.abs(se.values[0])));

            if(Math.abs(se.values[1])> Math.abs(currentYMax) )
                outputYMax.setText(String.format("%.2f", Math.abs(se.values[1])));

            if(Math.abs(se.values[2])> Math.abs(currentZMax) )
                outputZMax.setText(String.format("%.2f", Math.abs(se.values[2])));

            outputX.setText(String.format("%.2f", se.values[0]));
            outputY.setText(String.format("%.2f", se.values[1]));
            outputZ.setText(String.format("%.2f", se.values[2]));

        }
    }
}