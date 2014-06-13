package br.com.samuraidev.XpdlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.samuraidev.XpdlParser.enums.ActivityType;

public class ProcessLibraryStrategy implements RecommendationStrategy {

	private List<ProcessContext> processLibrary = new ArrayList<ProcessContext>();

	public ProcessContext getRecommendation(ProcessContext context,
			String activityId) {
		ProcessContext recommendationContext = new ProcessContext();
		// Context de retorno deve possuir um start event
		// Para saber onde encaixar o fluxo recomendado
		String startUUID = UUID.randomUUID().toString();
		Activity startEvent = new Activity(ActivityType.EVENT, startUUID,
				"Start");
		recommendationContext.getActivitiesList().add(startEvent);
		loadProcessLibrary();
		// Achar a activity com codebooks mais semelhantes e recomendar
		// codebooks que tenham essa activity no inicio
		ProcessContext similarContext = getSimilarContext(context,
				getProcessLibrary(), activityId);
		List<CodeBook> activityCodebooks = context.getCodebooksWithActivity(2,
				activityId);
		Activity activity = context.getActivityById(activityId);
		Activity similarActivity = getSimililarActivity(activityCodebooks,
				similarContext, activity);
		System.out.println(similarActivity);
		// Recomendar codebooks removendo as activities que precedem a
		// activity similar
		List<CodeBook> codebooks = similarContext.getCodebooksWithActivity(2,
				similarActivity.getId());
		List<Transition> recommendedTransitions = new ArrayList<Transition>();
		for (CodeBook cb : codebooks) {
			boolean encontrouActivity = false;
			for (Transition trans : cb.getTransitions()) {
				if (encontrouActivity) { // && !hasTransition(trans,
											// recommendedTransitions)
					recommendedTransitions.add(trans);
				}
				if (trans.getFrom().getId().equals(similarActivity.getId())) {
					encontrouActivity = true;
				}
			}
		}
		System.out.println("----- RECOMMENDATION -----");
		for (Transition t : recommendedTransitions) {
			System.out.println(t);
		}
		return recommendationContext;
	}

	// private boolean hasTransition(Transition trans,
	// List<Transition> recommendedTransitions) {
	// for (Transition transition : recommendedTransitions) {
	// if (transition.equals(trans)) {
	// return true;
	// }
	// }
	// return false;
	// }

	private Activity getSimililarActivity(List<CodeBook> activityCodebooks,
			ProcessContext similarContext, Activity activity) {
		ActivityType activityType = activity.getType();
		// Encontrar o id que mais se repete nos codebooks semelhantes
		List<Activity> activities = new ArrayList<Activity>();
		List<CodeBook> libraryCodebooks = getEqualCodebooks(activityCodebooks,
				similarContext);
		HashMap<String, Integer> activityCount = new HashMap<String, Integer>();
		for (CodeBook cb : libraryCodebooks) {
			for (Transition trans : cb.getTransitions()) {
				if (trans.getFrom().getType().equals(activityType)) {
					Integer count = activityCount.get(trans.getFrom().getId());
					if (count == null) {
						activityCount.put(trans.getFrom().getId(), 1);
						activities.add(trans.getFrom());
					} else {
						activityCount.put(trans.getFrom().getId(), count + 1);
					}
				}
			}
			// Pega a ultima transição
			Transition trans = cb.getTransitions().get(
					cb.getTransitions().size() - 1);
			if (trans.getTo().getType().equals(activityType)) {
				Integer count = activityCount.get(trans.getTo().getId());
				if (count == null) {
					activityCount.put(trans.getTo().getId(), 1);
					activities.add(trans.getTo());
				} else {
					activityCount.put(trans.getTo().getId(), count + 1);
				}
			}
		}
		String bestMatchId = null;
		Integer bestCount = 0;
		for (Map.Entry<String, Integer> entry : activityCount.entrySet()) {
			System.out.print("key,val: ");
			System.out.println(entry.getKey() + "," + entry.getValue());
			if (entry.getValue() > bestCount) {
				bestMatchId = entry.getKey();
				bestCount = entry.getValue();
			}
		}
		Activity similarActivity = null;
		for (Activity act : activities) {
			if (act.getId().equals(bestMatchId)) {
				similarActivity = act;
				break;
			}
		}
		return similarActivity;
	}

	private ProcessContext getSimilarContext(ProcessContext context,
			List<ProcessContext> library, String activityId) {
		// Contar a quantidade de codebooks size 2 iguais
		Integer bestCount = 0;
		ProcessContext bestContext = null;
		List<CodeBook> contextCBs = context.getCodebooksWithActivity(2,
				activityId);
		for (ProcessContext contextFromLibrary : library) {
			List<CodeBook> libraryCBs = contextFromLibrary.getCodebooks(2);
			Integer codebookCount = countEqualCodebooks(contextCBs, libraryCBs);
			if (codebookCount > bestCount) {
				bestCount = codebookCount;
				bestContext = contextFromLibrary;
				System.out.println(bestCount);
			}
		}
		return bestContext;
	}

	/***
	 * 
	 * Conta a quantidade de codebooks construidos com os mesmos tipos
	 * atividades
	 * 
	 * @param contextCBs
	 * @param libraryCBs
	 * @return Integer value of equal codebooks
	 */
	private Integer countEqualCodebooks(List<CodeBook> contextCBs,
			List<CodeBook> libraryCBs) {
		Integer count = 0;
		for (CodeBook cCB : contextCBs) {
			for (CodeBook lCB : libraryCBs) {
				if (cCB.equals(lCB)) {
					count++;
					break;
				}
			}
		}
		return count;
	}

	private List<CodeBook> getEqualCodebooks(List<CodeBook> codebooks,
			ProcessContext context) {
		List<CodeBook> result = new ArrayList<CodeBook>();
		for (CodeBook cCB : codebooks) {
			if (context == null) {
				throw new RuntimeException("OMFG");
			}
			for (CodeBook lCB : context.getCodebookList(2)) {
				if (cCB.equals(lCB)) {
					result.add(lCB);
					break;
				}
			}
		}
		return result;
	}

	private void loadProcessLibrary() {
		ProcessContext adviseOfCharge = new ProcessContext();
		adviseOfCharge.parseXpdl("resources/xpdl/advise_of_charge.xpdl");
		getProcessLibrary().add(adviseOfCharge);

		ProcessContext bestPathForecast = new ProcessContext();
		bestPathForecast.parseXpdl("resources/xpdl/best_path_forecast.xpdl");
		getProcessLibrary().add(bestPathForecast);

		// ProcessContext billDelayedPayment = new ProcessContext();
		// billDelayedPayment
		// .parseXpdl("resources/xpdl/bill_delayed_payment.xpdl");
		// getProcessLibrary().add(billDelayedPayment);

		ProcessContext billingAdviceOfCharge = new ProcessContext();
		billingAdviceOfCharge
				.parseXpdl("resources/xpdl/billing_advice_of_charge.xpdl");
		getProcessLibrary().add(billingAdviceOfCharge);

	}

	public List<ProcessContext> getProcessLibrary() {
		return processLibrary;
	}

	public void setProcessLibrary(List<ProcessContext> processLibrary) {
		this.processLibrary = processLibrary;
	}

}
