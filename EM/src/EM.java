import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;


public class EM {
	static Vector<Vector <Integer>> D = new Vector< Vector <Integer>>();
	static Vector<Vector <Integer>> T = new Vector< Vector <Integer>>();
	
	// P_absence P_mild P_severe
	static Vector <Double> Psyndrome = new Vector<Double>();
	static double TRIMONO;
	
	//All the conditional probabilities
	static Vector<Vector <Double>> PSloepnea = new Vector< Vector <Double>>();
	static Vector<Vector <Double>> PForiennditis = new Vector< Vector <Double>>();
	static Vector<Vector <Double>> PDegar = new Vector< Vector <Double>>();
	
	// probabilities
	static Vector<Vector <Double>> PA = new Vector< Vector <Double>>();

	public static void newP(){
		double sumWeight = 0;
		double numeratorA = 0;
		double numeratorB = 0;
		double numeratorC = 0;
		double numeratorD = 0;
		for (int i = 0; i < PA.size(); i++) {
			if (PA.get(i).get(4) == 0) {
				sumWeight = sumWeight + PA.get(i).get(7)*PA.get(i).get(5);
				numeratorA = numeratorA + PA.get(i).get(7)*PA.get(i).get(5);
			}
			else if (PA.get(i).get(4) == 1) {
				sumWeight = sumWeight + PA.get(i).get(7)*PA.get(i).get(5);
				numeratorB = numeratorB + PA.get(i).get(7)*PA.get(i).get(5);
			}
			else if (PA.get(i).get(4) == 2) {
				sumWeight = sumWeight + PA.get(i).get(7)*PA.get(i).get(5);
				numeratorC = numeratorC + PA.get(i).get(7)*PA.get(i).get(5);
			}
			
			if (PA.get(i).get(3) == 1) {
				numeratorD = numeratorD + PA.get(i).get(7)*PA.get(i).get(5);
			}
		}
		
		Psyndrome.set(0, numeratorA/sumWeight);
		Psyndrome.set(1, numeratorB/sumWeight);
		Psyndrome.set(2, numeratorC/sumWeight);
		
		TRIMONO = numeratorD/sumWeight;
		
		//update PSloepnea
		for (int i = 0; i < PSloepnea.size(); i++) {
			sumWeight = 0;
			numeratorA = 0;
			for (int j = 0; j < PA.size(); j++) {
				if (PA.get(j).get(4) == 1 || PA.get(j).get(4) == 2 || PA.get(j).get(4) == 0) {
					Vector<Double>entry = PA.get(j);
					double t = entry.get(3);
					double ds = entry.get(4);
					double s = entry.get(0);
					double t1 = PSloepnea.get(i).get(0);
					double ds1 = PSloepnea.get(i).get(1);
					if (t==t1 && ds==ds1) {
						sumWeight = sumWeight + PA.get(j).get(7)*PA.get(j).get(5);
						if (s == (double) 1) {
							numeratorA = numeratorA + PA.get(j).get(7)*PA.get(j).get(5);
						}
					}
				}
			}
			PSloepnea.get(i).set(2, numeratorA/sumWeight);
		}
		for (int i = 0; i < PSloepnea.size(); i++) {
			Vector<Double>entry = PSloepnea.get(i);
			if (entry.get(2) == 0) {
				PSloepnea.get(i).set(2, 0.001);
			}
		}
		
		//update PForiennditis
		for (int i = 0; i < PForiennditis.size(); i++) {
			sumWeight = 0;
			numeratorA = 0;
			for (int j = 0; j < PA.size(); j++) {
				if (PA.get(j).get(4) == 1 || PA.get(j).get(4) == 2 || PA.get(j).get(4) == 0) {
					Vector<Double>entry = PA.get(j);
					double ds = entry.get(4);
					double f = entry.get(1);
					double ds1 = PForiennditis.get(i).get(0);
					if (ds==ds1) {
						sumWeight = sumWeight + PA.get(j).get(7)*PA.get(j).get(5);
						if (f == (double) 1) {
							numeratorA = numeratorA + PA.get(j).get(7)*PA.get(j).get(5);
						}
					}
				}
			}
			PForiennditis.get(i).set(1, numeratorA/sumWeight);
		}
		for (int i = 0; i < PForiennditis.size(); i++) {
			Vector<Double>entry = PForiennditis.get(i);
			if (entry.get(1) == 0) {
				PForiennditis.get(i).set(1, 0.001);
			}
		}
		
		//update PForiennditis
		for (int i = 0; i < PDegar.size(); i++) {
			sumWeight = 0;
			numeratorA = 0;
			for (int j = 0; j < PA.size(); j++) {
				if (PA.get(j).get(4) == 1 || PA.get(j).get(4) == 2 || PA.get(j).get(4) == 0) {
					Vector<Double>entry = PA.get(j);
					double ds = entry.get(4);
					double f = entry.get(2);
					double ds1 = PDegar.get(i).get(0);
					if (ds==ds1) {
						sumWeight = sumWeight + PA.get(j).get(7)*PA.get(j).get(5);
						if (f == (double) 1) {
							numeratorA = numeratorA + PA.get(j).get(7)*PA.get(j).get(5);
						}
					}
				}
			}
			PDegar.get(i).set(1, numeratorA/sumWeight);
		}
		for (int i = 0; i < PDegar.size(); i++) {
			Vector<Double>entry = PDegar.get(i);
			if (entry.get(1) == 0) {
				PDegar.get(i).set(1, 0.001);
			}
		}
	}
	
	public static double getRandom(){
		Random r = new Random();
		double randomValue = 0 + (4 - 0) * r.nextDouble();
		
		return randomValue;
	}
	
	public static void whiteNoise(){
		
		double total = 0;
		double randomNum;
		for(int i = 0; i < Psyndrome.size(); i++){
			Psyndrome.set(i, Psyndrome.get(i) + getRandom());
			total = total + Psyndrome.get(i);
		}
		for(int i = 0; i < Psyndrome.size(); i++){
			Psyndrome.set(i, Psyndrome.get(i)/total);
		}
		
		randomNum = getRandom();
		TRIMONO = (TRIMONO+randomNum)/ (randomNum + getRandom() + 1);
		
		total = 0;
		for (int i = 0; i < 6; i++) {
			PSloepnea.get(i).set(2, getRandom()+ PSloepnea.get(i).get(2));
			total = total + PSloepnea.get(i).get(2);
		}
		for (int i = 0; i < 6; i++) {
			PSloepnea.get(i).set(2, (PSloepnea.get(i).get(2))/total);
		}
		
		total = 0;
		for (int i = 0; i < 3; i++) {
			PForiennditis.get(i).set(1, PForiennditis.get(i).get(1)+getRandom());
			total = total + PForiennditis.get(i).get(1);
		}
		for (int i = 0; i < 3; i++) {
			PForiennditis.get(i).set(1, PForiennditis.get(i).get(1)/total);
		}
		
		total = 0;
		for (int i = 0; i < 3; i++) {
			PDegar.get(i).set(1, getRandom()+PDegar.get(i).get(1));
			total = total + PDegar.get(i).get(1);
		}
		for (int i = 0; i < 3; i++) {
			PDegar.get(i).set(1, (PDegar.get(i).get(1))/total);
		}
		
	}
	
	public static void countSample(){
		for (int i = 0; i < D.size(); i++) {
			Vector<Integer>example = D.get(i);
			boolean found = false;
			for(int j = 0; j < PA.size();j++){
				double s = PA.get(j).get(0);
				double f = PA.get(j).get(1);
				double d = PA.get(j).get(2);
				double t = PA.get(j).get(3);
				double DS = PA.get(j).get(4); 
				double s1 = example.get(0);
				double f1 = example.get(1);
				double d1 = example.get(2);
				double t1 = example.get(3);
				double DS1 = example.get(4);
				if(s == s1 && f == f1 && d == d1 && t == t1 && DS == DS1){
					found = true;
					if (PA.get(j).size() == 5) {
						PA.get(j).add(new Double(1.0));
						PA.get(j).add(new Double(0.0));
						PA.get(j).add(new Double(0.0));
					}
					else{
						PA.get(j).set(5, PA.get(j).get(5)+1);
					}
					break;
				}
			}
			if (!found) {
				System.out.println("Holy!");
			}
		}
		
		// add zero if some entry has no corresponding entry
		for (int i = 0; i < PA.size(); i++) {
			if (PA.get(i).size() == 5) {
				PA.get(i).add(new Double(0));
				PA.get(i).add(new Double(0));
				PA.get(i).add(new Double(0));
			}
		}
		
		//split the entries with missing data to other value
//		for (int i = 0; i < PA.size(); ) {
//			Vector<Double>entry = PA.get(i+3);
//			double count = entry.get(5);
//			for (double j = 0; j < count; j++) {
//				PA.get(i).set(5, PA.get(i).get(5)+Psyndrome.get(0));
//				PA.get(i+1).set(5, PA.get(i+1).get(5)+Psyndrome.get(1));
//				PA.get(i+2).set(5, PA.get(i+2).get(5)+Psyndrome.get(2));
//			}
//			
//			i = i+4;
//			
//		}
		
		//remove the entry with missing data
//		for (int i = 0; i < PA.size(); i++) {
//			if (PA.get(i).get(4) == -1) {
//				PA.remove(i);
//				i = 0;
//			}
//		}
	}
	
	public static void calculateWeight(){
		
		for (int i = 0; i < PA.size(); i++) {
//			double s = PA.get(i).get(0);
//			double f = PA.get(i).get(1);
//			double d = PA.get(i).get(2);
			double t = PA.get(i).get(3);
			double DS = PA.get(i).get(4); 
			double P = 1;
			
			if (t == 1) {
				P = P * TRIMONO;
			}
			else {
				P = P * (1-TRIMONO);
			}
			
			//get the probability for Syndrome
			if (DS == -1) { // ignore DS = -1 for now, because we just calculating the probability now
				continue;
			}
			else if( DS == 0){
				P = P * Psyndrome.get(0);
			}
			else if( DS == 1){
				P = P * Psyndrome.get(1);
			}
			else { // DS == 2
				P = P * Psyndrome.get(2);
			}
			
			//multiply the probability for Sloepnea
			for (int j = 0; j < PSloepnea.size(); j++) {
				Vector<Double>entry = PSloepnea.get(j);
				if (entry.get(0) == t && entry.get(1) == DS) {
					double p = entry.get(2);
					if (PA.get(i).get(0) == 0) {
						p = 1-p;
					}
					P = P * p;
					break;
				}
			}
			
			//multiply the probability for Foriennditis
			for (int j = 0; j < PForiennditis.size(); j++) {
				Vector<Double>entry = PForiennditis.get(j);
				if (entry.get(0) == DS) {
					double p = entry.get(1);
					if (PA.get(i).get(1) == 0) {
						p = 1-p;
					}
					P = P * p;
					break;
				}
			}
			
			//multiply the probability for Degar Spot
			for (int j = 0; j < PDegar.size(); j++) {
				Vector<Double>entry = PDegar.get(j);
				if (entry.get(0) == DS) {
					double p = entry.get(1);
					if (PA.get(i).get(2) == 0) {
						p = 1-p;
					}
					P = P * p;
					break;
				}
			}
			PA.get(i).set(6, P);
		}
		
		// get the normalized result
		for (int j = 0; j < PA.size();) {
			Vector<Double>entry1 = PA.get(j);
			Vector<Double>entry2 = PA.get(j+1);
			Vector<Double>entry3 = PA.get(j+2);
			
			Double total  = entry1.get(6) + entry2.get(6) + entry3.get(6);
			Double normal1 = entry1.get(6)/total;
			Double normal2 = entry2.get(6)/total;
			Double normal3 = entry3.get(6)/total;
			PA.get(j).set(7, normal1);
			PA.get(j+1).set(7, normal2);
			PA.get(j+2).set(7, normal3);
			
			j = j+4;
		}
		
	}
	
	public static void initAugment(){
		PA = new Vector< Vector <Double>>();
		for(int i = 0; i<D.size(); i++){
			Vector<Integer>example = D.get(i);
			boolean found = false;
			for(int j = 0; j < PA.size();j++){
				double s = PA.get(j).get(0);
				double f = PA.get(j).get(1);
				double d = PA.get(j).get(2);
				double t = PA.get(j).get(3);
				double s1 = example.get(0);
				double f1 = example.get(1);
				double d1 = example.get(2);
				double t1 = example.get(3);
				if(s == s1 && f == f1 && d == d1 && t == t1){
					found = true;
					break;
				}
			}
			
			if(!found){
				Vector<Double>holder = new Vector<Double>();
				holder.add(new Double(example.get(0)));
				holder.add(new Double(example.get(1)));
				holder.add(new Double(example.get(2)));
				holder.add(new Double(example.get(3)));
				holder.add(new Double(0));
				PA.add(holder);
				holder = new Vector<Double>();
				holder.add(new Double(example.get(0)));
				holder.add(new Double(example.get(1)));
				holder.add(new Double(example.get(2)));
				holder.add(new Double(example.get(3)));
				holder.add(new Double(1));
				PA.add(holder);
				holder = new Vector<Double>();
				holder.add(new Double(example.get(0)));
				holder.add(new Double(example.get(1)));
				holder.add(new Double(example.get(2)));
				holder.add(new Double(example.get(3)));
				holder.add(new Double(2));
				PA.add(holder);
				holder = new Vector<Double>();
				holder.add(new Double(example.get(0)));
				holder.add(new Double(example.get(1)));
				holder.add(new Double(example.get(2)));
				holder.add(new Double(example.get(3)));
				holder.add(new Double(-1));
				PA.add(holder);
			}
		}
	}
	
	public static void initSloepnea(){
		Vector<Double> tmp = new Vector<Double>();
		tmp.add(new Double(1.0));	//T=t
		tmp.add(new Double(0.0));	//DS=a
		tmp.add(new Double(0.0));	//P(S|T, DS)
		PSloepnea.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(1.0));	//T=t
		tmp.add(new Double(1.0));	//DS=a
		tmp.add(new Double(0.25));	//P(S|T, DS)
		PSloepnea.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(1.0));	//T=t
		tmp.add(new Double(2.0));	//DS=a
		tmp.add(new Double(0.25));	//P(S|T, DS)
		PSloepnea.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(0.0));	//T=t
		tmp.add(new Double(0.0));	//DS=a
		tmp.add(new Double(0.0));	//P(S|T, DS)
		PSloepnea.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(0.0));	//T=t
		tmp.add(new Double(1.0));	//DS=a
		tmp.add(new Double(0.25));	//P(S|T, DS)
		PSloepnea.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(0.0));	//T=t
		tmp.add(new Double(2.0));	//DS=a
		tmp.add(new Double(0.25));	//P(S|T, DS)
		PSloepnea.add(tmp);
	}
	
	public static void initForiennditis(){
		Vector<Double> tmp = new Vector<Double>();
		tmp.add(new Double(0.0));	//DS=a
		tmp.add(new Double(0.0));	//P(F|DS)
		PForiennditis.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(1.0));	//DS=a
		tmp.add(new Double(0.80));	//P(F|DS)
		PForiennditis.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(2.0));	//DS=a
		tmp.add(new Double(0.20));	//P(F|DS)
		PForiennditis.add(tmp);
	}
	
	public static void initPDegar(){
		Vector<Double> tmp = new Vector<Double>();
		tmp.add(new Double(0.0));	//DS=a
		tmp.add(new Double(0.0));	//P(F|DS)
		PDegar.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(1.0));	//DS=a
		tmp.add(new Double(0.20));	//P(F|DS)
		PDegar.add(tmp);
		tmp = new Vector<Double>();
		tmp.add(new Double(2.0));	//DS=a
		tmp.add(new Double(0.80));	//P(F|DS)
		PDegar.add(tmp);
	}

	public static void split(){
		for (int i = 0; i < PA.size(); ) {
			Vector<Double>entry = PA.get(i+3);
			double count = entry.get(5);
			for (double j = 0; j < count; j++) {
				PA.get(i).set(5, PA.get(i).get(5)+PA.get(i).get(7));
				PA.get(i+1).set(5, PA.get(i+1).get(5)+PA.get(i+1).get(7));
				PA.get(i+2).set(5, PA.get(i+2).get(5)+PA.get(i+2).get(7));
			}

			i = i+4;

		}
	}

	public static void main(String[] args) {
		BufferedReader br = null;

		//load data and initialize data vector
		try {
			String sCurrentLine;
			String []tokens;
			String deli = "[ ]+";
			br = new BufferedReader(new FileReader("src/trainData.txt"));
			for(int i=0; (sCurrentLine = br.readLine()) != null; i++){
				Vector <Integer>example = new Vector<Integer>();
				tokens = sCurrentLine.split(deli);
				example.add(Integer.parseInt(tokens[1]));
				example.add(Integer.parseInt(tokens[2]));
				example.add(Integer.parseInt(tokens[3]));
				example.add(Integer.parseInt(tokens[4]));
				example.add(Integer.parseInt(tokens[5]));
				D.add(example);
			}
			
			//Initial guess
			Psyndrome = new Vector<Double>();
			Psyndrome.add(new Double(0.5));
			Psyndrome.add(new Double(0.25));
			Psyndrome.add(new Double(0.25));
			
			TRIMONO = 0.10;
			
			initSloepnea();
			initForiennditis();
			initPDegar();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		whiteNoise();
		
		for (int i = 0; i < 15; i++) {
			initAugment();
			countSample();
			calculateWeight();
			split();
			newP();
			
//			for (int j = 0; j < Psyndrome.size(); j++) {
//				System.out.print(Psyndrome.get(j)+" ");
//			}
//			System.out.print("\n");
		}
		
		//load data and initialize data vector
		try {
			String sCurrentLine;
			String []tokens;
			String deli = "[ ]+";
			br = new BufferedReader(new FileReader("src/testData.txt"));
			for(int i=0; (sCurrentLine = br.readLine()) != null; i++){
				Vector <Integer>example = new Vector<Integer>();
				tokens = sCurrentLine.split(deli);
				example.add(Integer.parseInt(tokens[1]));
				example.add(Integer.parseInt(tokens[2]));
				example.add(Integer.parseInt(tokens[3]));
				example.add(Integer.parseInt(tokens[4]));
				example.add(Integer.parseInt(tokens[5]));
				T.add(example);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//do the test
		int totalT = T.size();
		int correct = 0;
		for (int i = 0; i < totalT; i++) {
			Vector<Vector<Double>>tester = new Vector<Vector<Double>>();
			double ds = T.get(i).get(4);
			double ds1 = 0;
			double p = 0;
			int max = 0;
			
			for (int j = 0; j < PA.size(); j++) {
				Vector<Double>entry = PA.get(j);
				double s = T.get(i).get(0);
				double f = T.get(i).get(1);
				double d = T.get(i).get(2);
				double t = T.get(i).get(3);
				double s1 = entry.get(0);
				double f1 = entry.get(1);
				double d1 = entry.get(2);
				double t1 = entry.get(3);
				if (s == s1 && f==f1 && d==d1 && t==t1) {
					tester.add(entry);
				}
			}
			for (int j = 0; j < tester.size(); j++) {
				if (p<=tester.get(j).get(7)) {
					p = tester.get(j).get(7);
					max = j;
				}
			}
			if (ds == tester.get(max).get(4)) {
				correct++;
			}
		}
		
		System.out.println(correct);
		
		for (int i = 0; i < PA.size(); i++) {
			for (int j = 0; j < PA.get(i).size(); j++) {
				System.out.print(PA.get(i).get(j)+" ");
			}
			System.out.print("\n");
		}
	}
}
