package PaperClassifier;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;

public class Driver {
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		// Configure spark
		SparkConf sparkConf = new SparkConf().setAppName("PaperClassifier").setMaster("local[4]").
        		set("spark.driver.memory", "24g").
        		set("spark.driver.maxResultSize", "20g").
        		set("spark.executor.memory", "2g");

		// Start a spark context
		JavaSparkContext sc = new JavaSparkContext(sparkConf);

		SparkSession spark = SparkSession.builder().appName("PaperClassifier").getOrCreate();

		/// Read all dataset input files into a dataset object
		String datapath = "/path/to/dataset/part-*";
		JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc.sc(), datapath, (int)Math.pow(2, 12)).toJavaRDD();

		// Split the dataset, 70% for training, 30% for testing
	    JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3}, 11L);
	    JavaRDD<LabeledPoint> trainingData = splits[0];
	    JavaRDD<LabeledPoint> testingData = splits[1];

		// Training phase
	    System.out.println(" =========== Training Model....");
	    Model M = new Model();
	    M.train(trainingData);
//	    M.display_features();

		// Test phase
	    System.out.println(" =========== Classification started");

        LongAccumulator correct = sc.sc().longAccumulator();
        LongAccumulator total = sc.sc().longAccumulator();
	    M.classify(testingData, correct, total);
        double accuracy = ((double)correct.value() / total.value());
        System.out.println("Correct: " + correct.value() + " of " + total.value() + " - Accuracy:" + accuracy);

/*
		Model M = new Model();
        LongAccumulator correct = sc.sc().longAccumulator();
        M.acc(correct);
        System.out.println("Correct: " + correct.value());
*/
		spark.stop();
		sc.close();
		sc.stop();

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(" =========== Elapsed Time: " + elapsedTime);
	}
}
