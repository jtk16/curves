public class Dense {

    double[] input;
    double[] output;

    double[][] weights;

    double[] bias;



    public Dense(int inputSize, int outputSize) {
        input = new double[inputSize];
        output = new double[outputSize];

        weights = new double[inputSize][outputSize];

        bias = new double[outputSize];

        populateRandom(bias);
        for (int i = 0; i < inputSize; i++) {
            populateRandom(weights[i]);
        }
    }

    public double[] feedForward(double[] in) {
        for (int i = 0; i < in.length; i++) {
            input[i] = in[i];
        }

        for (int j = 0; j < output.length; j++) {
            output[j] = bias[j];
            for (int i = 0; i < input.length; i++) {
                output[j] += input[i] * weights[i][j];
            }
        }
        return output;
    }

    public void populateRandom(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 0.5 * Math.random();
        }
    }

    public 
}
