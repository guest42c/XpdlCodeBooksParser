package br.com.samuraidev.XpdlParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XpdlParser {

	SAXBuilder builder;

	File xmlFile;

	public XpdlParser() {
		builder = new SAXBuilder();
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

	private String getActivityType(Element activity) {
		if (getChildWithName(activity, "Event") != null) {
			return "Event";
		}
		if (getChildWithName(activity, "Implementation") != null) {
			Element implementation = getChildWithName(activity,
					"Implementation");
			if (getChildWithName(implementation, "Task") != null) {
				return "Task Implementation";
			}
			return "Implementation";
		}
		if (getChildWithName(activity, "Route") != null) {
			return "Route";
		}
		return "";
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
									System.out.println("---");
									System.out.println("Id: "
											+ activity.getAttributeValue("Id"));
									System.out.println("Name: "
											+ activity
													.getAttributeValue("Name"));
									System.out
											.println(getActivityType(activity));
								}
							} else if (wfpChild.getName().equals("Transitions")) {
								// Lista as transições entre os elementos
								List<Element> transitions = wfpChild
										.getChildren();
								for (Element transition : transitions) {
									System.out.println("---");
									System.out.println("From: "
											+ transition
													.getAttributeValue("From"));
									System.out.println("To: "
											+ transition
													.getAttributeValue("To"));
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
}
