package pl.edu.mimuw.ag291541.task2.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.edu.mimuw.ag291541.task2.security.annotation.AclGuarded;
import pl.edu.mimuw.ag291541.task2.security.entity.User;

@Entity
@Table(name = "announcement_instance")
@AclGuarded
public class AnnouncementInstance {
	@GeneratedValue
	@Id
	private Long id;
	private boolean readStatus;
	private Calendar readDate;
	@ManyToOne(optional = false)
	private User receiver;
	@ManyToOne(optional = false)
	private Announcement announcement;

	public AnnouncementInstance() {
	}

	public AnnouncementInstance(User receiver, Announcement announcement) {
		this.receiver = receiver;
		this.announcement = announcement;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isReadStatus() {
		return readStatus;
	}

	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}

	public Calendar getReadDate() {
		return readDate;
	}

	public void setReadDate(Calendar readDate) {
		this.readDate = readDate;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	@Override
	public String toString() {
		return "AnnouncementInstance [id=" + id + ", readStatus=" + readStatus
				+ ", readDate=" + readDate + ", receiver=" + receiver + "]";
	}

	public void markRead() {
		setReadDate(Calendar.getInstance());
		setReadStatus(true);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((announcement == null) ? 0 : announcement.hashCode());
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AnnouncementInstance))
			return false;
		AnnouncementInstance other = (AnnouncementInstance) obj;
		if (announcement == null) {
			if (other.announcement != null)
				return false;
		} else if (!announcement.equals(other.announcement))
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		return true;
	}
}
