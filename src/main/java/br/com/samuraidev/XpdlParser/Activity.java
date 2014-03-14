package br.com.samuraidev.XpdlParser;

import br.com.samuraidev.XpdlParser.enums.ActivityType;

public class Activity {

	private ActivityType type;

	private String id;

	private String name;

	public Activity() {
		super();
	}

	public Activity(ActivityType type, String id, String name) {
		super();
		this.type = type;
		this.id = id;
		this.name = name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ");
		sb.append(id);
		sb.append("\n");
		sb.append("name: ");
		sb.append(name);
		sb.append("\n");
		sb.append("Type: ");
		sb.append(type);
		sb.append("\n");
		return sb.toString();
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
