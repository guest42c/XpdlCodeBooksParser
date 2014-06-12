package br.com.samuraidev.XpdlParser;

import java.util.ArrayList;
import java.util.List;
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
		ProcessContext similar = getSimilarContext(context, getProcessLibrary());
		// TODO: como escolher a recomendacao a partir do processo mais similar
		return recommendationContext;
	}

	private ProcessContext getSimilarContext(ProcessContext context,
			List<ProcessContext> library) {
		// Contar a quantidade de codebooks size 2 iguais
		Float bestCount = (float) 0;
		ProcessContext bestContext = null;
		for (ProcessContext contextFromLibrary : library) {
			List<CodeBook> contextCBs = context.getCodebooks(2);
			List<CodeBook> libraryCBs = contextFromLibrary.getCodebooks(2);
			Integer codebookCount = countEqualCodebooks(contextCBs, libraryCBs);
			Float normalizedCount = (float) ((2.0 * codebookCount) / (contextCBs
					.size() + libraryCBs.size()));
			if (normalizedCount > bestCount) {
				bestCount = normalizedCount;
				bestContext = contextFromLibrary;
				System.out.println(bestCount);
			}
		}
		return bestContext;
	}

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

	private void loadProcessLibrary() {
		ProcessContext adviseOfCharge = new ProcessContext();
		adviseOfCharge.parseXpdl("resources/xpdl/advise_of_charge.xpdl");
		getProcessLibrary().add(adviseOfCharge);

		ProcessContext bestPathForecast = new ProcessContext();
		bestPathForecast.parseXpdl("resources/xpdl/best_path_forecast.xpdl");
		getProcessLibrary().add(bestPathForecast);

		ProcessContext billDelayedPayment = new ProcessContext();
		billDelayedPayment
				.parseXpdl("resources/xpdl/bill_delayed_payment.xpdl");
		getProcessLibrary().add(billDelayedPayment);

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
