package PaperClassifier;

import java.io.Serializable;

public class TrainingMapOutput implements Serializable {
	static final long serialVersionUID = 1L;

	private Integer feature;
	private Double label;
	private Double weight;

	/// Getters
	public Integer getfeature() { return feature; }
	public Double getlabel() { return label; }
	public Double getweight() { return weight; }

	/// Setters
	public void setfeature(Integer v) { feature = v; }
	public void setlabel(Double v) { label = v; }
	public void setweight(Double v) { weight = v; }
}
