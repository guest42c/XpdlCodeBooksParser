package br.com.samuraidev.XpdlParser;

import java.util.ArrayList;
import java.util.List;

public class CodeBook {

	private List<Transition> codebooks;

	public CodeBook() {
		super();
		codebooks = new ArrayList<Transition>();
	}

	public CodeBook(Transition... codebooks) {
		super();
		for (Transition codebook : codebooks) {
			getCodebooks().add(codebook);
		}
	}

	public Integer getSize() {
		return getCodebooks() != null ? getCodebooks().size() : 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Transition transition = null;
		for (int i = 0; i < getCodebooks().size(); i++) {
			transition = getCodebooks().get(i);
			sb.append(transition.getFrom().getType() + "->");
		}
		if (transition != null && transition.getTo() != null) {
			sb.append(transition.getTo().getType());
		}
		return sb.toString();
	}

	public List<Transition> getCodebooks() {
		return codebooks;
	}

	public void setCodebooks(List<Transition> codebooks) {
		this.codebooks = codebooks;
	}

	public void addTransition(Transition transition) {
		getCodebooks().add(transition);
	}

}
