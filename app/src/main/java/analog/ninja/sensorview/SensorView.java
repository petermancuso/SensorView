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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;
import java.util.Arrays;


public class SensorView extends AppCompatActivity {
    private LineGraphView graph;
    private LineGraphView raw;
    private SeekBar seekBar;
    private TextView textView;
    private TextView alphaView;
    private BasicFilter lowPass = new BasicFilter(1f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_view);
        LinearLayout l =  (LinearLayout) findViewById(R.id.graphLayout);
        textView = (TextView) findViewById(R.id.textView1);
        alphaView = (TextView) findViewById(R.id.alpha);

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

        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        SensorEventListener selAccel = new AccelerometerSensorListener(tvAccelX, tvAccelY, tvAccelZ, tvAccelXMax, tvAccelYMax, tvAccelZMax, graph, raw, lowPass);
        sensorManager.registerListener(selAccel, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);

        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorEventListener selMagnetic = new MagneticSensorEventListener(tvMagneticX, tvMagneticY, tvMagneticZ, tvMagneticXMax, tvMagneticYMax, tvMagneticZMax);
        sensorManager.registerListener(selMagnetic, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        SensorEventListener selLight = new LightSensorListener(tvLight);
        sensorManager.registerListener(selLight, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        SensorEventListener selRotation = new RotationFieldEventListener(tvRotationX, tvRotationY, tvRotationZ, tvRotationXMax, tvRotationYMax, tvRotationZMax);
        sensorManager.registerListener(selRotation, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar));

        textView.setText("Scale Factor: " + seekBar.getProgress() + "/" + seekBar.getMax());

        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView.setText("Applied: " + progress + "/" + seekBar.getMax());
                lowPass.tune(0.01f - progress / 100000f);

                alphaView.setText("Low Pass: "+ (String.format("%.5f",0.01f-progress/100000f)));

                if (progress == 0){
                    lowPass.tune(1f);
                    alphaView.setText("Low Pass: 0.00000");
            }
                if(progress==1000) {
                    lowPass.tune(0.00001f);
                    alphaView.setText("Low Pass: 0.00001");

                }//Toast.makeText(getApplicationContext(), "Changing lowpass filter Attenuation", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                //Toast.makeText(getApplicationContext(), "Started Lowpass Filter Attenuation", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                textView.setText("Scale: " + progress + "/" + seekBar.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}














