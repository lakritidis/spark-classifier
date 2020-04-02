package PaperClassifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.util.LongAccumulator;

import scala.Tuple3;
import scala.runtime.AbstractFunction2;
import scala.runtime.BoxedUnit;


public class Model implements Serializable {
	private static final long serialVersionUID = 1L;

	HashMap<Integer, Feature> features;

	Model() {
		features = new HashMap<Integer, Feature>(1048576);
	}
/*
	public void train(JavaRDD<LabeledPoint> T) {
		List<TrainingMapOutput> flist = T.flatMap(new FlatMapFunction<LabeledPoint, TrainingMapOutput>() {
			private static final long serialVersionUID = 2864525283354902332L;

			public Iterator<TrainingMapOutput> call(LabeledPoint x) {
				List<TrainingMapOutput> md_list = new ArrayList<>();

				Double label = x.label();
				Vector features = x.features();

//				System.out.println("Label " + label + " - Features:" + features);
				features.foreachActive(
						new AbstractFunction2<Object, Object, BoxedUnit>() {
					        public BoxedUnit apply(Object t1, Object t2) {
					        	TrainingMapOutput map_record = new TrainingMapOutput();
					        	map_record.setlabel(label);
					        	map_record.setfeature((Integer)t1);
					        	map_record.setweight((Double)t2);
					        	md_list.add(map_record);
//					            System.out.println("Index:" + t1 + "      Value:" + t2);
					            return BoxedUnit.UNIT;
					        }
					    }
				);

				Iterator<TrainingMapOutput> it = md_list.iterator();
				return it;
			}
		}).collect();

		System.out.println(" ========= Data Collected, building the model.... ====");
		flist.forEach(row -> {
			double label = row.getlabel();
			int feature_id = row.getfeature();
			double weight = row.getweight();

//			System.out.println(feature_id);
			if (features.containsKey(feature_id)) {
				Feature f = features.get(feature_id);

				f.insert_label(label);
				features.put(feature_id, f);
			} else {
				Feature f = new Feature();

				f.setweight(weight);
				f.insert_label(label);
				features.put(feature_id, f);
			}
		});

		System.out.println("flist size: " + flist.size() + " - features size: " + features.size());
		flist = null;
	}
*/

	public void train(JavaRDD<LabeledPoint> T) {
		List<Tuple3<Integer, Double, Double>> flist = T.flatMap(new FlatMapFunction<LabeledPoint, Tuple3<Integer, Double, Double>>() {
			private static final long serialVersionUID = 2864525283354902332L;

			public Iterator<Tuple3<Integer, Double, Double>> call(LabeledPoint x) {
				List<Tuple3<Integer, Double, Double>> md_list = new ArrayList<>();

				Double label = x.label();
				Vector features = x.features();

//				System.out.println("Label " + label + " - Features:" + features);
				features.foreachActive(
						new AbstractFunction2<Object, Object, BoxedUnit>() {
					        public BoxedUnit apply(Object t1, Object t2) {
//					        	TrainingMapOutput map_record = new TrainingMapOutput();
//					        	map_record.setlabel(label);
//					        	map_record.setfeature((Integer)t1);
//					        	map_record.setweight((Double)t2);
//					        	md_list.add(new Tuple2<Tuple2<Integer, Double>, Double>(new Tuple2<Integer, Double>((Integer)t1,(Double)t2), label));
					        	md_list.add(new Tuple3<Integer, Double, Double>((Integer)t1,(Double)t2, label));

//					            System.out.println("Index:" + t1 + "      Value:" + t2);
					            return BoxedUnit.UNIT;
					        }
					    }
				);

				Iterator<Tuple3<Integer, Double, Double>> it = md_list.iterator();
				return it;
			}
		}).collect();

		System.out.println(" ========= Data Collected, building the model.... ====");
		flist.forEach(row -> {
			double label = row._3();
			int feature_id = row._1();
			double weight = row._2();

//			System.out.println(feature_id + ":" + weight + ":" + label);
			if (features.containsKey(feature_id)) {
				Feature f = features.get(feature_id);

				f.insert_label(label);
				features.put(feature_id, f);
			} else {
				Feature f = new Feature();

				f.setweight(weight);
				f.insert_label(label);
				features.put(feature_id, f);
			}
		});

		System.out.println("flist size: " + flist.size() + " - features size: " + features.size());
		flist = null;
	}

	/// Classification Function
	public long classify(JavaRDD<LabeledPoint> R, LongAccumulator cor, LongAccumulator tot) {
//		System.out.println("===================================== features size: " + features.size());

		Map<Double, Double> candidates = new TreeMap<Double, Double>();

		long papers = R.map(x -> {

			Double label = x.label();
			Double predicted_label = -1.0;
			Vector features_vector = x.features();

//			System.out.println("=== New testing record with label " + label + " & features vector " + features_vector.toString());

			features_vector.foreachActive( new AbstractFunction2<Object, Object, BoxedUnit>() {
				public BoxedUnit apply(Object t1, Object t2) {
					if (features.containsKey(t1)) {
						Feature f = features.get(t1);

						List<RdvEntry> rdv = features.get(t1).getrdv();
						int list_size = rdv.size();

//						System.out.println("====== Checking feature " + t1 + " with " + list_size + " candidate labels");

						for (int i = 0; i < list_size; i++) {

							RdvEntry e = rdv.get(i);
							double score = (double) f.getweight() * e.getfreq() / f.getfreq();

//							System.out.println("========= Checking candidate label " + e.getlabel() + ": |X_f|=" + f.getfreq() + ", |X_{f,c}| = " + e.getfreq() + ", Weight = " + f.getweight() + " Computed Score: " + score);
							if (candidates.containsKey(e.getlabel())) {
//								System.out.println("========= Candidate label " + e.getlabel() + " exists into candidate list: Previous Score: " + candidates.get(e.getlabel()));
								candidates.put(e.getlabel(), candidates.get(e.getlabel()) + score);
							} else {
//								System.out.println("========= Candidate label " + e.getlabel() + " does not exist in the candidate list");
								candidates.put(rdv.get(i).getlabel(), score);
							}
						}
					}
					// System.out.println("Index:" + t1 + "      Value:" + t2);
					return BoxedUnit.UNIT;
				}
			});

    		Set<Double> keys = candidates.keySet();
    		double max_score = 0.0;
            for(Double key : keys) {
                if ( candidates.get(key) > max_score ) {
                	max_score = candidates.get(key);
                	predicted_label = key;
                }
            }

            candidates.clear();

            //System.out.println("Label:" + label + " Predicted label:" + predicted_label);
            tot.add(1L);
			if (predicted_label.doubleValue() == label.doubleValue()) {
				cor.add(1L);
				return 1;
			}
			return 0;
		}).count();

		return papers;
	}

	public void display_features() {
		Iterator<Map.Entry<Integer, Feature>> it = features.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Feature> pair = it.next();
			System.out.println("Feature: " + pair.getKey() +
					" - Frequency: " + pair.getValue().getfreq() +
					" - Weight: " + pair.getValue().getweight());
			pair.getValue().display_rdv();
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public void acc(LongAccumulator a) {
		a.add(6L);
	}
}
