package br.com.samuraidev.XpdlParser;

import java.util.List;
import java.util.UUID;

import br.com.samuraidev.XpdlParser.enums.ActivityType;

public class SameActivityStrategy implements RecommendationStrategy {

	public ProcessContext getRecommendation(ProcessContext context,
			String activityId) {
		ProcessContext recommendationContext = new ProcessContext();
		List<Activity> activities = context.getActivitiesList();
		// Context de retorno deve possuir um start event
		// Para saber onde encaixar o fluxo recomendado
		String startUUID = UUID.randomUUID().toString();
		Activity startEvent = new Activity(ActivityType.EVENT, startUUID,
				"Start");
		recommendationContext.getActivitiesList().add(startEvent);
		for (Activity activity : activities) {
			if (activity.getId().equals(activityId)) {
				recommendationContext.getActivitiesList().add(activity);
				recommendationContext.getTransitions().add(
						new Transition(startEvent, activity));
			}
		}
		return recommendationContext;
	}
}