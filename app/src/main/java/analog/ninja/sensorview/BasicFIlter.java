package analog.ninja.sensorview;


public class BasicFilter {
    float alpha;
    float[] input = new float[3];
    float[] output = new float[3];

    public BasicFilter(float α) {
        alpha = α;
    }

    public float[] lowPassFilter(float[] in, float[] out) {
        //Initialize output
        if (output == null)
            return input;

        for (int i = 0; i < in.length; i++)
            out[i] = out[i] + alpha * (in[i] - out[i]);

        return output;
    }

    public void tune(float a) {
        alpha = a;
    }
}


