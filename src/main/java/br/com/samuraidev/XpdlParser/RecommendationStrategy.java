package br.com.samuraidev.XpdlParser;

public interface RecommendationStrategy {

	public ProcessContext getRecommendation(ProcessContext context, String activityId);

}
