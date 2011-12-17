package pl.edu.mimuw.ag291541.task2.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import pl.edu.mimuw.ag291541.task2.security.annotation.AclGuarded;

@Entity
@DiscriminatorValue("Announcement")
@AclGuarded
public class Announcement extends Content {
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "announcement")
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
	/* IT relies on hashCode and equals provided by subclass (that is Content). */
}
