package CloudSim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.Interval;

import IoT.*;

import IoT.SerializeFlows;

public class OffloadCloudOFDecider {

	public static double Computeqoe(List<QoE> qoe, String mac) {

		double qoes = qoe.get(0).getQoS_A() - qoe.get(0).getQoS_M();
		for (QoE e : qoe) {
			if (e.getCloudfogsource().getMac().equals(mac))
				if ((e.getQoS_A() - e.getQoS_M()) < qoes) {
					qoes = e.getQoS_A() - e.getQoS_M();
				}
		}
		return qoes;

	}

	public static double ComputeTSOf(List<QoE> qoe, String macSource1, String macSource2) {
		double TS;
		double qoes2 = Computeqoe(qoe, macSource1);
		ArrayList<CloudFog> devices = new ArrayList<CloudFog>();
		devices = EcoSystem.getCloudStorage();
		List<QoE> Qoes = new ArrayList<QoE>();
		for (CloudFog d : devices) {
			if (d.getMac().equals(macSource1)) {
				Qoes = d.getQoE();
			}
		}
		double qoes = Qoes.get(0).getQoS_A() - Qoes.get(0).getQoS_M();
		int x = 0;
		for (QoE e : Qoes) {
			if (e.getCloudfogsource().getMac().equals(macSource2))
				x++;
			if ((e.getQoS_A() - e.getQoS_M()) < qoes) {
				qoes = e.getQoS_A() - e.getQoS_M();
			}
		}
		TS = (qoes * Math.exp(-x)) * qoes2;
		return TS;

	}

	public static LinkedList<FlowStructure> FindRecipient(String MAC1, LinkedList<FlowStructure> concurentflows) {
		for (FlowStructure f : concurentflows) {
			String MAC2 = f.getSource();
			ArrayList<CloudFog> devices = new ArrayList<CloudFog>();
			devices = EcoSystem.getStorage();
			for (CloudFog cf : devices) {
				if (cf.getQoE() != null && !(cf.getMac().equals(MAC1))) {
					cf.setTS(ComputeTSOf(cf.getQoE(), MAC1, MAC2));
				}
			}
			Collections.sort(devices, new Comparator<CloudFog>() {

				public int compare(CloudFog c1, CloudFog c2) {
					return Double.compare(c1.getTS(), c2.getTS());
				}
			});
			CloudFog storage = devices.get(devices.size() - 2);

			f.setRecipient(storage);
		}
		return concurentflows;
	}

	public static LinkedList<FlowStructure> Findconcurent() throws IOException, ClassNotFoundException {
		Flows Myflows = SerializeFlows.serializeDataIn();
		LinkedList<FlowStructure> concurentflows = new LinkedList<FlowStructure>();
		for (FlowStructure f : Myflows.getCurrentflows()) {
			if (f.getValidity().containsNow()) {
				concurentflows.add(f);
			}
		}
		return concurentflows;
	}

	public static LinkedList<FlowStructure> RankFlows(LinkedList<FlowStructure> concurentflows) {
		LinkedList<FlowStructure> equalflows = new LinkedList<FlowStructure>();
		LinkedList<FlowStructure> overlapsflows = new LinkedList<FlowStructure>();
		LinkedList<FlowStructure> duringflows = new LinkedList<FlowStructure>();
		LinkedList<FlowStructure> startsflows = new LinkedList<FlowStructure>();
		LinkedList<FlowStructure> finishesflows = new LinkedList<FlowStructure>();
		LinkedList<FlowStructure> precedsflows = new LinkedList<FlowStructure>();
		LinkedList<FlowStructure> meetsflows = new LinkedList<FlowStructure>();
		for (int i = 0; i < concurentflows.size(); i++) {
			for (int j = i + 1; j < concurentflows.size(); j++) {
				if (getRelation(concurentflows.get(i), concurentflows.get(j)).equals("equals")) {
					equalflows.add(concurentflows.get(i));
					equalflows.add(concurentflows.get(j));
				} else if (getRelation(concurentflows.get(i), concurentflows.get(j)).equals("overlaps")) {
					overlapsflows.add(concurentflows.get(i));
					overlapsflows.add(concurentflows.get(j));
				} else if (getRelation(concurentflows.get(i), concurentflows.get(j)).equals("during")) {
					duringflows.add(concurentflows.get(i));
					duringflows.add(concurentflows.get(j));
				} else if (getRelation(concurentflows.get(i), concurentflows.get(j)).equals("starts")) {
					startsflows.add(concurentflows.get(i));
					startsflows.add(concurentflows.get(j));
				} else if (getRelation(concurentflows.get(i), concurentflows.get(j)).equals("finishes")) {
					finishesflows.add(concurentflows.get(i));
					finishesflows.add(concurentflows.get(j));
				} else if (getRelation(concurentflows.get(i), concurentflows.get(j)).equals("preceds")) {
					precedsflows.add(concurentflows.get(i));
					precedsflows.add(concurentflows.get(j));
				} else if (getRelation(concurentflows.get(i), concurentflows.get(j)).equals("meets")) {
					meetsflows.add(concurentflows.get(i));
					meetsflows.add(concurentflows.get(j));
				}

			}
		}

		if (!overlapsflows.isEmpty()) {
			concurentflows = treatoverlaps(concurentflows, overlapsflows);
			RankFlows(concurentflows);
		} else if (!finishesflows.isEmpty()) {
			concurentflows = treatfinishes(concurentflows, finishesflows);
			RankFlows(concurentflows);
		} else if (!startsflows.isEmpty()) {
			concurentflows = treatstarts(concurentflows, startsflows);
			RankFlows(concurentflows);
		} else if (!duringflows.isEmpty()) {
			concurentflows = treatduring(concurentflows, duringflows);
			RankFlows(concurentflows);
		}
		Collections.sort(concurentflows, new Comparator<FlowStructure>() {

			public int compare(FlowStructure c1, FlowStructure c2) {
				return c1.getValidity().getStart().compareTo(c2.getValidity().getStart());
			}
		});

		return concurentflows;
	}

	public static LinkedList<FlowStructure> treatduring(LinkedList<FlowStructure> concurentflows,
			LinkedList<FlowStructure> duringflows) {
		while (!duringflows.isEmpty()) {
			int i = 0;
			int j = 1;
			if (duringflows.get(i).getValidity().contains(duringflows.get(j).getValidity())) {
				concurentflows.remove(duringflows.get(i));
				Interval newInterval = new Interval(duringflows.get(j).getValidity().getEnd(),
						duringflows.get(i).getValidity().getEnd());
				duringflows.get(i).setValidity(newInterval);
				concurentflows.add(duringflows.get(i));
			} else {
				concurentflows.remove(duringflows.get(j));
				Interval newInterval = new Interval(duringflows.get(i).getValidity().getEnd(),
						duringflows.get(j).getValidity().getEnd());
				duringflows.get(j).setValidity(newInterval);
				concurentflows.add(duringflows.get(j));
			}
			duringflows.remove(j);
			duringflows.remove(i);
		}
		return concurentflows;
	}

	public static LinkedList<FlowStructure> treatstarts(LinkedList<FlowStructure> concurentflows,
			LinkedList<FlowStructure> startsflows) {
		while (!startsflows.isEmpty()) {
			int i = 0;
			int j = 1;
			if (startsflows.get(i).getValidity().getEnd().isBefore(startsflows.get(j).getValidity().getEnd())) {
				concurentflows.remove(startsflows.get(j));
				Interval newInterval = new Interval(startsflows.get(i).getValidity().getEnd(),
						startsflows.get(j).getValidity().getEnd());
				startsflows.get(j).setValidity(newInterval);
				concurentflows.add(startsflows.get(j));
			} else {
				concurentflows.remove(startsflows.get(i));
				Interval newInterval = new Interval(startsflows.get(j).getValidity().getEnd(),
						startsflows.get(i).getValidity().getEnd());
				startsflows.get(i).setValidity(newInterval);
				concurentflows.add(startsflows.get(i));
			}
			startsflows.remove(j);
			startsflows.remove(i);
		}

		return concurentflows;
	}

	public static LinkedList<FlowStructure> treatfinishes(LinkedList<FlowStructure> concurentflows,
			LinkedList<FlowStructure> finishesflows) {
		while (!finishesflows.isEmpty()) {
			int i = 0;
			int j = 1;
			if (finishesflows.get(i).getValidity().getStart().isBefore(finishesflows.get(j).getValidity().getStart())) {
				concurentflows.remove(finishesflows.get(i));
				Interval newInterval = new Interval(finishesflows.get(i).getValidity().getStart(),
						finishesflows.get(j).getValidity().getStart());
				finishesflows.get(i).setValidity(newInterval);
				concurentflows.add(finishesflows.get(i));
			} else {
				concurentflows.remove(finishesflows.get(j));
				Interval newInterval = new Interval(finishesflows.get(j).getValidity().getStart(),
						finishesflows.get(i).getValidity().getStart());
				finishesflows.get(j).setValidity(newInterval);
				concurentflows.add(finishesflows.get(j));
			}
			finishesflows.remove(j);
			finishesflows.remove(i);
		}

		return concurentflows;
	}

	public static LinkedList<FlowStructure> treatoverlaps(LinkedList<FlowStructure> concurentflows,
			LinkedList<FlowStructure> overlapsflows) {
		while (!overlapsflows.isEmpty()) {
			int i = 0;
			int j = 1;
			if (overlapsflows.get(i).getValidity().overlaps(overlapsflows.get(j).getValidity())) {
				concurentflows.remove(overlapsflows.get(i));
				Interval newInterval = new Interval(overlapsflows.get(i).getValidity().getStart(),
						overlapsflows.get(j).getValidity().getStart());
				overlapsflows.get(i).setValidity(newInterval);
				concurentflows.add(overlapsflows.get(i));
			} else {
				concurentflows.remove(overlapsflows.get(j));
				Interval newInterval = new Interval(overlapsflows.get(j).getValidity().getStart(),
						overlapsflows.get(i).getValidity().getStart());
				overlapsflows.get(j).setValidity(newInterval);
				concurentflows.add(overlapsflows.get(j));
			}
			overlapsflows.remove(j);
			overlapsflows.remove(i);
		}
		return concurentflows;
	}

	public static String getRelation(FlowStructure f1, FlowStructure f2) {
		String relation = "";

		if (f1.getValidity().equals(f2.getValidity()))
			relation = "equals";
		else if (f1.getValidity().overlaps(f2.getValidity()) || f2.getValidity().overlaps(f1.getValidity()))
			relation = "overlaps";
		else if (f1.getValidity().contains(f2.getValidity()) || f2.getValidity().contains(f1.getValidity()))
			relation = "during";
		else if (f1.getValidity().getStart().equals(f2.getValidity().getStart()))
			relation = "starts";
		else if (f1.getValidity().getEnd().equals(f2.getValidity().getEnd()))
			relation = "finishes";
		else if (f1.getValidity().getEnd().isBefore(f2.getValidity().getStart())
				|| f2.getValidity().getEnd().isBefore(f1.getValidity().getStart()))
			relation = "preceds";
		else if (f1.getValidity().getEnd().isEqual(f2.getValidity().getStart())
				|| f2.getValidity().getEnd().isEqual(f1.getValidity().getStart()))
			relation = "meets";

		return relation;
	}

	public static boolean compareFlows(String f1, String f2, int nbline) throws IOException {
		BufferedReader reader1 = new BufferedReader(new FileReader(f1));

		BufferedReader reader2 = new BufferedReader(new FileReader(f2));

		String line1 = reader1.readLine();

		String line2 = reader2.readLine();

		boolean areEqual = true;

		int lineNum = 0;

		while (lineNum <= nbline) {
			if (line1 == null || line2 == null) {
				areEqual = false;

				break;
			} else if (!line1.equalsIgnoreCase(line2)) {
				areEqual = false;

				break;
			}

			line1 = reader1.readLine();

			line2 = reader2.readLine();

			lineNum++;
		}

		reader1.close();

		reader2.close();

		return areEqual;
	}

	public static LinkedList<FlowStructure> Removeredendant(LinkedList<FlowStructure> concurentflows)
			throws IOException, ClassNotFoundException {
		for (int i = 0; i < concurentflows.size(); i++) {
			for (int j = i; j < concurentflows.size(); j++) {
				if (concurentflows.get(i).getSource().equals(concurentflows.get(j).getSource())
						&& concurentflows.get(i).getRecipient().equals(concurentflows.get(j).getRecipient()))

					if (compareFlows(concurentflows.get(i).getFlowName(), concurentflows.get(j).getFlowName(),
							Math.min(concurentflows.get(i).getNbline(), concurentflows.get(i).getNbline()))) {
						if (concurentflows.get(i).getNbline() == (Math.min(concurentflows.get(i).getNbline(),
								concurentflows.get(i).getNbline())))
							concurentflows.remove(i);
						else
							concurentflows.remove(j);

					}
			}
		}
		return concurentflows;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		LinkedList<FlowStructure> concurentflows = Findconcurent();

		concurentflows = FindRecipient(MyCloudOFSender.getMac(), concurentflows);
		concurentflows = Removeredendant(concurentflows);
		concurentflows = RankFlows(concurentflows);
		for (FlowStructure f : concurentflows) {
			CloudFog Recipient = f.getRecipient();
			new MyCloudOFSender(Recipient.getIP(), Recipient.getPort(), f);

		}
	}

}