package br.com.samuraidev.XpdlParser;

import java.util.Date;
import java.util.Random;

import br.com.samuraidev.XpdlParser.enums.ActivityType;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		// FIXME: criar e fazer parser de um processo incompleto
		ProcessContext uncompletedProcess = new ProcessContext();
		// uncompletedProcess.parseXpdl("resources/xpdl/advise_of_charge.xpdl");
		uncompletedProcess
				.parseXpdl("resources/xpdl/bill_delayed_payment.xpdl");

		// select a random activity to call the recommendation from
		Date date = new Date();
		Random randomizer = new Random(date.getTime());
		Activity randomActivity = uncompletedProcess.getActivitiesList().get(
				randomizer.nextInt(uncompletedProcess.getActivitiesList()
						.size()));
		while (ActivityType.EVENT.equals(randomActivity.getType())) {
			randomActivity = uncompletedProcess.getActivitiesList().get(
					randomizer.nextInt(uncompletedProcess.getActivitiesList()
							.size()));
		}
		System.out.println("Random activity to call the recommendation from");
		System.out.println(randomActivity);

		System.out.println("SameActivityStrategy");
		System.out.println("return the same activity as recommendation");
		RecommendationStrategy sameActivityRecommendation = new SameActivityStrategy();
		ProcessContext sameActivityRecommendationContext = sameActivityRecommendation
				.getRecommendation(uncompletedProcess, randomActivity.getId());
		for (Transition trans : sameActivityRecommendationContext
				.getTransitions()) {
			System.out.println(trans);
		}

		System.out.println("ProcessBaseStrategy");
		System.out.println("return recommendation based on set of processes");
		RecommendationStrategy processBaseRecommendation = new ProcessLibraryStrategy();
		ProcessContext processBaseRecommendationContext = processBaseRecommendation
				.getRecommendation(uncompletedProcess, randomActivity.getId());
		for (Transition trans : processBaseRecommendationContext
				.getTransitions()) {
			System.out.println(trans);
		}
	}
}
