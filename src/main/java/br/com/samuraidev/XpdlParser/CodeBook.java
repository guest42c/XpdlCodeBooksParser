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
			getTransitions().add(codebook);
		}
	}

	public Integer getSize() {
		return getTransitions() != null ? getTransitions().size() : 0;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Transition transition = null;
		for (int i = 0; i < getTransitions().size(); i++) {
			transition = getTransitions().get(i);
			sb.append(transition.getFrom().getType() + "->");
		}
		if (transition != null && transition.getTo() != null) {
			sb.append(transition.getTo().getType());
		}
		return sb.toString();
	}

	public boolean equals(CodeBook cb) {
		if (cb.getTransitions().size() != this.getTransitions().size()) {
			return false;
		} else {
			for (int i = 0; i < this.getTransitions().size(); i++) {
				if (!this.getTransitions().get(i).getFrom().getType()
						.equals(cb.getTransitions().get(i).getFrom().getType())
						|| !this.getTransitions()
								.get(i)
								.getTo()
								.getType()
								.equals(cb.getTransitions().get(i).getTo()
										.getType())) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean hasId(String id) {
		for (Transition trans : getTransitions()) {
			if (trans.getFrom().getId().equals(id)
					|| trans.getTo().getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	public List<Transition> getTransitions() {
		return codebooks;
	}

	public void setCodebooks(List<Transition> codebooks) {
		this.codebooks = codebooks;
	}

	public void addTransition(Transition transition) {
		getTransitions().add(transition);
	}

}
