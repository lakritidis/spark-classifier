package PaperClassifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Feature implements Serializable {
	private static final long serialVersionUID = 4777626052436156868L;

	private int freq;
	private double weight;
	private List<RdvEntry> rdv;

	Feature() {
		freq = 0;
		weight = 0.0;
		rdv = new ArrayList<RdvEntry>();
	}

	public void insert_label(double l) {
		freq++;
		int list_size = rdv.size();

		for (int i = 0; i < list_size; i++) {
			RdvEntry e = rdv.get(i); 
			if (e.getlabel() == l) {
				e.setfreq(e.getfreq() + 1);
				return;
			}
		}

		rdv.add(new RdvEntry(l, 1));
	}

	public void display_rdv() {
		int list_size = rdv.size();

		for (int i = 0; i < list_size; i++) {
			RdvEntry e = rdv.get(i);
			System.out.println("\t" + e.getlabel() + " - " + e.getfreq());
		}
	}

	// getters
	public int getfreq() { return freq; }
	public double getweight() { return weight; }
	public List<RdvEntry> getrdv() { return rdv; }

	// setters
	public void setfreq(int v) { freq = v; }
	public void setweight(double v) {
		if (v > 0) {
			weight = v;
		} else {
			weight = -v;
			System.out.println(weight);
		}
	}
/*
	public void setweight(double v) {
		if (v == 0.5) {
			weight = 0.7;
		} else if (v == 0.3 ){ weight = 0.2; } else { weight = 0.1; }
	}
*/
}
