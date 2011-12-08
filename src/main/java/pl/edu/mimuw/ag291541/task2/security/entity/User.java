package pl.edu.mimuw.ag291541.task2.security.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import pl.edu.mimuw.ag291541.task2.entity.AnnouncementInstance;

@Entity
@Table(name = "secuser", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"name", "surname" }) })
public class User {
	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	@Column(updatable = false)
	private String name;
	@NotNull
	@Column(updatable = false)
	private String surname;
	@ManyToMany(mappedBy = "members", cascade = { CascadeType.ALL })
	private Set<Group> groups = new HashSet<Group>();
	@OneToMany(mappedBy = "receiver", cascade = { CascadeType.ALL })
	private Set<AnnouncementInstance> announcements = new HashSet<AnnouncementInstance>();

	public User() {
	}

	public User(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Set<AnnouncementInstance> getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(Set<AnnouncementInstance> announcements) {
		this.announcements = announcements;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}
}
