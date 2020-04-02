package PaperClassifier;

import java.io.Serializable;

public class ClassifyMapOutput implements Serializable {
	static final long serialVersionUID = 1L;

	double y;
	double predicted_y;
	int result;

	public double gety() { return y; }
	public double getpredicted_y() { return predicted_y; }
	public int getresult() { return result; }

	public void sety(double v) { y = v; }
	public void setpredicted_y(double v) { predicted_y = v; }
	public void setresult(int v) { result = v; }
}
