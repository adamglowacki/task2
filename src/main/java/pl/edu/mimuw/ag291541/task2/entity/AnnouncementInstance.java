package pl.edu.mimuw.ag291541.task2.entity;

import java.util.Calendar;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import pl.edu.mimuw.ag291541.task2.security.entity.User;

public class AnnouncementInstance {
	@GeneratedValue
	@Id
	private Long id;
	private boolean readStatus;
	private Calendar readDate;
	@ManyToOne(optional = false)
	private User receiver;
	@ManyToOne
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
}
