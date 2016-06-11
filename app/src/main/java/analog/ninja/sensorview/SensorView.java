package analog.ninja.sensorview;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.lang.Math;
import java.util.Arrays;


public class SensorView extends AppCompatActivity {
    public LineGraphView graph;
    public LineGraphView raw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_view);
        LinearLayout l =  (LinearLayout) findViewById(R.id.graphLayout);

        //Filtered Graph Data
        graph = new LineGraphView(this, 100, Arrays.asList("x", "y", "z"));
        l.addView(graph);
        graph.setVisibility(View.VISIBLE);

        //Raw Sensor Graph Data
        raw = new LineGraphView(this, 100, Arrays.asList("x", "y", "z"));
        l.addView(raw);
        raw.setVisibility(View.VISIBLE);

        TextView tvLight = (TextView) findViewById(R.id.tvlight);

        TextView tvMagneticX = (TextView) findViewById(R.id.tvMagneticX);
        TextView tvMagneticY = (TextView) findViewById(R.id.tvMagneticY);
        TextView tvMagneticZ = (TextView) findViewById(R.id.tvMagneticZ);
        TextView tvMagneticXMax = (TextView) findViewById(R.id.tvMagneticXMAX);
        TextView tvMagneticYMax = (TextView) findViewById(R.id.tvMagneticYMAX);
        TextView tvMagneticZMax = (TextView) findViewById(R.id.tvMagneticZMAX);

        tvMagneticXMax.setText("0");
        tvMagneticYMax.setText("0");
        tvMagneticZMax.setText("0");


        TextView tvAccelX = (TextView) findViewById(R.id.accelX);
        TextView tvAccelY = (TextView) findViewById(R.id.accelY);
        TextView tvAccelZ = (TextView) findViewById(R.id.accelZ);
        TextView tvAccelXMax = (TextView) findViewById(R.id.tvAccelXMax);
        TextView tvAccelYMax = (TextView) findViewById(R.id.tvAccelYMax);
        TextView tvAccelZMax = (TextView) findViewById(R.id.tvAccelZMax);
        TextView tvAccelUnits  = (TextView) findViewById(R.id.accelUnits);

        tvAccelXMax.setText("0");
        tvAccelYMax.setText("0");
        tvAccelZMax.setText("0");
        tvAccelUnits.setText(Html.fromHtml("( m/s<sup><small>2</small></sup> )"));


        TextView tvRotationX = (TextView) findViewById(R.id.tvRotationX);
        TextView tvRotationY = (TextView) findViewById(R.id.tvRotationY);
        TextView tvRotationZ = (TextView) findViewById(R.id.tvRotationZ);
        TextView tvRotationXMax = (TextView) findViewById(R.id.tvRotationXMax);
        TextView tvRotationYMax = (TextView) findViewById(R.id.tvRotationYMax);
        TextView tvRotationZMax = (TextView) findViewById(R.id.tvRotationZMax);

        tvRotationXMax.setText("0");
        tvRotationYMax.setText("0");
        tvRotationZMax.setText("0");


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        // Sensor delay changed to SENSOR_DELAY_FASTEST
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        SensorEventListener selAccel = new AccelFieldSensorEventListener(tvAccelX, tvAccelY, tvAccelZ, tvAccelXMax, tvAccelYMax, tvAccelZMax, graph, raw);
        sensorManager.registerListener(selAccel, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);

        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorEventListener selMagnetic = new MagneticFieldSensorEventListener(tvMagneticX, tvMagneticY, tvMagneticZ, tvMagneticXMax, tvMagneticYMax, tvMagneticZMax);
        sensorManager.registerListener(selMagnetic, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener selLight = new LightSensorEventListener(tvLight);
        sensorManager.registerListener(selLight, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SensorEventListener selRotation = new RotationFieldSensorEventListener(tvRotationX, tvRotationY, tvRotationZ, tvRotationXMax, tvRotationYMax, tvRotationZMax);
        sensorManager.registerListener(selRotation, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
}

class AccelFieldSensorEventListener implements SensorEventListener {
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
    static final float alpha = 0.25f;

    public AccelFieldSensorEventListener(TextView x, TextView y, TextView z, TextView xMax, TextView yMax, TextView zMax, LineGraphView g, LineGraphView r) {
        outputX = x;
        outputY = y;
        outputZ = z;
        outputXMax = xMax;
        outputYMax = yMax;
        outputZMax = zMax;
        graph = g;  //Filtered Graph Data
        raw = r;    //Raw Sensor Graph Data
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

        // Modify constant alpha in constructor for filter sensitivity
        output = lowPassFilter(se.values, output, alpha );

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

    protected float[] lowPassFilter(float[] in, float[] out, float α){
        //Initialize output
        if (output == null)
            return input;

        for(int i=0; i<in.length; i++)
            out[i] = out[i] + α * (in[i]-out[i]);

        return output;
    }
}

class RotationFieldSensorEventListener implements SensorEventListener {
    TextView outputX;
    TextView outputY;
    TextView outputZ;
    TextView outputXMax;
    TextView outputYMax;
    TextView outputZMax;

    public RotationFieldSensorEventListener(TextView x, TextView y, TextView z, TextView xMax, TextView yMax, TextView zMax) {

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

class MagneticFieldSensorEventListener implements SensorEventListener {
    TextView outputX;
    TextView outputY;
    TextView outputZ;
    TextView outputXMax;
    TextView outputYMax;
    TextView outputZMax;

    public MagneticFieldSensorEventListener(TextView x, TextView y, TextView z, TextView xMax, TextView yMax, TextView zMax) {
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

class LightSensorEventListener implements SensorEventListener {
    TextView output;

    public LightSensorEventListener(TextView outputView) {
        output = outputView;
    }

    public void onAccuracyChanged(Sensor s, int i) {
    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LIGHT) {
            output.setText(String.format("%.2f", se.values[0]));
        }
    }
}





