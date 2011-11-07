package ch.ubx.startlist.server;

import javax.persistence.Id;

public class DummyEntry {
	@Id
	protected Long id;
	private String label;

	public DummyEntry() {
	}

	public DummyEntry(String label) {
		super();
		this.setLabel(label);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean equals(Object obj) {
		return ((DummyEntry) obj).getLabel().compareTo(label) == 0;
	}

}