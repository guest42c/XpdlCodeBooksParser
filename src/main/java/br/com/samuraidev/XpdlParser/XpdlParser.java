package br.com.samuraidev.XpdlParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XpdlParser {

	SAXBuilder builder;

	File xmlFile;

	List<Transition> transitions;

	List<Activity> activitiesList;

	public XpdlParser() {
		builder = new SAXBuilder();
		transitions = new ArrayList<Transition>();
		activitiesList = new ArrayList<Activity>();
	}

	private Element getChildWithName(Element activity, String name) {
		for (Object activityChild : activity.getChildren()) {
			if (activityChild instanceof Element) {
				Element child = (Element) activityChild;
				if (child.getName().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

	private ActivityType getActivityType(Element activity) {
		if (getChildWithName(activity, "Event") != null) {
			return ActivityType.EVENT;
		}
		if (getChildWithName(activity, "Implementation") != null) {
			Element implementation = getChildWithName(activity,
					"Implementation");
			if (getChildWithName(implementation, "Task") != null) {
				return ActivityType.TASK_IMPLEMENTATION;
			}
			return ActivityType.IMPLEMENTATION;
		}
		if (getChildWithName(activity, "Route") != null) {
			return ActivityType.ROUTE;
		}
		if (getChildWithName(activity, "Gateway") != null) {
			return ActivityType.GATEWAY;
		}
		return ActivityType.NONE;
	}

	@SuppressWarnings("unchecked")
	public void parse(String file) {
		xmlFile = new File(file);
		try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren();

			for (Element node : list) {
				if (node.getName().equals("WorkflowProcesses")) {
					List<Element> workflowprocesses = node.getChildren();
					for (Element wfp : workflowprocesses) {
						List<Element> wfpChildren = wfp.getChildren();
						for (Element wfpChild : wfpChildren) {
							if (wfpChild.getName().equals("Activities")) {
								// Lista todas atividades do processo
								List<Element> activities = wfpChild
										.getChildren();
								for (Element activity : activities) {
									Activity actvty = new Activity();
									actvty.setId(activity
											.getAttributeValue("Id"));
									actvty.setName(activity
											.getAttributeValue("Name"));
									actvty.setType(getActivityType(activity));
									activitiesList.add(actvty);
								}
							} else if (wfpChild.getName().equals("Transitions")) {
								// Lista as transições entre os elementos
								List<Element> transitions = wfpChild
										.getChildren();
								for (Element transition : transitions) {
									String fromStr = transition
											.getAttributeValue("From");
									String toStr = transition
											.getAttributeValue("To");
									Activity from = getActivityById(fromStr);
									Activity to = getActivityById(toStr);
									Transition transit = new Transition(from,
											to);
									System.out.println("---");
									System.out.println(transit);
								}
							}
						}
					}
				}
			}

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	private Activity getActivityById(String id) {
		for (Activity activity : activitiesList) {
			if (id.equals(activity.getId())) {
				return activity;
			}
		}
		return null;
	}
}
