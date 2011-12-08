package pl.edu.mimuw.ag291541.task2.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "announcement")
public class Announcement extends Content {
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "announcement")
	private Set<AnnouncementInstance> instances = new HashSet<AnnouncementInstance>();

	public Announcement() {
	}

	public Announcement(String title, String name) {
		super(title, name);
	}

	public Set<AnnouncementInstance> getInstances() {
		return instances;
	}

	public void setInstances(Set<AnnouncementInstance> instances) {
		this.instances = instances;
	}

	@Override
	public String toString() {
		return "Announcement [instances=" + instances + "] derived from "
				+ super.toString();
	}
}
