package analog.ninja.sensorview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;


class RotationFieldEventListener implements SensorEventListener {
    TextView outputX;
    TextView outputY;
    TextView outputZ;
    TextView outputXMax;
    TextView outputYMax;
    TextView outputZMax;

    public RotationFieldEventListener(TextView x, TextView y, TextView z, TextView xMax, TextView yMax, TextView zMax) {

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

        float currentXMax =  Float.parseFloat(outputXMax.getText().toString());
        float currentYMax =  Float.parseFloat(outputYMax.getText().toString());
        float currentZMax =  Float.parseFloat(outputZMax.getText().toString());

        if(Math.abs(se.values[0])> Math.abs(currentXMax) )
            outputXMax.setText(String.format("%.2f", Math.abs(se.values[0])));

        if(Math.abs(se.values[1])> Math.abs(currentYMax) )
            outputYMax.setText(String.format("%.2f", Math.abs(se.values[1])));

        if(Math.abs(se.values[2])> Math.abs(currentZMax) )
            outputZMax.setText(String.format("%.2f", Math.abs(se.values[2])));

        if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            outputX.setText(String.format("%.2f", se.values[0]));
            outputY.setText(String.format("%.2f", se.values[1]));
            outputZ.setText(String.format("%.2f", se.values[2]));

        }
    }
}