package br.com.samuraidev.XpdlParser;

public class Transition {

	private Activity from;
	private Activity to;

	public Transition(Activity from, Activity to) {
		this.from = from;
		this.to = to;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("From: \n");
		sb.append(from);
		sb.append("To: \n");
		sb.append(to);
		return sb.toString();
	}

	public Activity getFrom() {
		return from;
	}

	public void setFrom(Activity from) {
		this.from = from;
	}

	public Activity getTo() {
		return to;
	}

	public void setTo(Activity to) {
		this.to = to;
	}

}
