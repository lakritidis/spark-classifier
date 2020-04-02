package PaperClassifier;

import java.io.Serializable;

public class RdvEntry implements Serializable {
	private static final long serialVersionUID = 4467930258720866310L;
	private double label;
	private int freq;

	RdvEntry() { }
	RdvEntry (double v1, int v2) {
		label = v1;
		freq = v2;
	}

	public double getlabel() { return label; }
	public int getfreq() { return freq; }

	public void setlabel(double v) { label = v; }
	public void setfreq(int v) { freq = v; }
}
