package analog.ninja.sensorview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;


public class AccelerometerSensorListener implements SensorEventListener {
    TextView outputX;
    TextView outputY;
    TextView outputZ;
    TextView outputXMax;
    TextView outputYMax;
    TextView outputZMax;
    LineGraphView graph;
    LineGraphView raw;
    float[] input = new float[3];
    float[] output = new float[3];
    BasicFilter filter;

    public AccelerometerSensorListener(TextView x, TextView y, TextView z, TextView xMax, TextView yMax, TextView zMax, LineGraphView g, LineGraphView r, BasicFilter f) {
        outputX = x;
        outputY = y;
        outputZ = z;
        outputXMax = xMax;
        outputYMax = yMax;
        outputZMax = zMax;
        graph = g;  //Filtered Graph Data
        raw = r;    //Raw Sensor Graph Data
        filter = f;
    }

    public void onAccuracyChanged(Sensor s, int i) {
    }

    public void onSensorChanged(SensorEvent se) {

        float currentXMax = Float.parseFloat(outputXMax.getText().toString());
        float currentYMax = Float.parseFloat(outputYMax.getText().toString());
        float currentZMax = Float.parseFloat(outputZMax.getText().toString());

        if (Math.abs(se.values[0]) > Math.abs(currentXMax))
            outputXMax.setText(String.format("%.2f", Math.abs(se.values[0])));

        if (Math.abs(se.values[1]) > Math.abs(currentYMax))
            outputYMax.setText(String.format("%.2f", Math.abs(se.values[1])));

        if (Math.abs(se.values[2]) > Math.abs(currentZMax))
            outputZMax.setText(String.format("%.2f", Math.abs(se.values[2])));

        // Modify constant alpha in constructor for filter sensitivity


        //output = lowPassFilter(se.values, output, filter );
        output = filter.lowPassFilter(se.values, output);
        //Sensor data processing complete
        //Insert measurement updates and method calls
        //Raw data available via se.values[], filtered data via output[]
        if (se.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            outputX.setText(String.format("%.2f", output[0]));
            outputY.setText(String.format("%.2f", output[1]));
            outputZ.setText(String.format("%.2f", output[2]));
            graph.addPoint(output);
            raw.addPoint(se.values);
        }
    }
}

