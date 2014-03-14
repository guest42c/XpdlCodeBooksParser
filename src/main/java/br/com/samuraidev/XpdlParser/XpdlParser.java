package br.com.samuraidev.XpdlParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import br.com.samuraidev.XpdlParser.enums.ActivityType;

public class XpdlParser {

	SAXBuilder builder;

	File xmlFile;

	List<Transition> transitions;

	List<Activity> activitiesList;

	List<CodeBook> codebookList;

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
								List<Element> transitionsWfp = wfpChild
										.getChildren();
								for (Element transitionWpf : transitionsWfp) {
									String fromStr = transitionWpf
											.getAttributeValue("From");
									String toStr = transitionWpf
											.getAttributeValue("To");
									Activity from = getActivityById(fromStr);
									Activity to = getActivityById(toStr);
									Transition transition = new Transition(
											from, to);
									getTransitions().add(transition);
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

	public List<CodeBook> getCodebookList(Integer size) {

		return codebookList;
	}

	public void setCodebookList(List<CodeBook> codebookList) {
		this.codebookList = codebookList;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public List<Activity> getActivitiesList() {
		return activitiesList;
	}

	public void setActivitiesList(List<Activity> activitiesList) {
		this.activitiesList = activitiesList;
	}

	public List<CodeBook> getCodebooks(Integer size) {
		setCodebookList(new ArrayList<CodeBook>());
		for (Transition trans : getTransitions()) {
			CodeBook codebook = new CodeBook();
			Transition currentTransition = trans;
			while (codebook.getSize() < size
					&& hasNextTransition(currentTransition)) {
				codebook.addTransition(currentTransition);
				currentTransition = getNextTransition(currentTransition);
			}
			if (size.equals(codebook.getSize())) {
				getCodebookList().add(codebook);
			}
		}
		return codebookList;
	}

	public List<CodeBook> getCodebookList() {
		return codebookList;
	}

	public boolean hasNextTransition(Transition currentTrans) {
		String next = currentTrans.getTo().getId();
		for (Transition transition : getTransitions()) {
			if (next.equals(transition.getFrom().getId())) {
				return true;
			}
		}
		return false;
	}

	public Transition getNextTransition(Transition currentTrans) {
		String next = currentTrans.getTo().getId();
		for (Transition transition : getTransitions()) {
			if (next.equals(transition.getFrom().getId())) {
				return transition;
			}
		}
		return null;
	}
}
