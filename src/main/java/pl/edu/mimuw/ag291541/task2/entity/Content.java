package pl.edu.mimuw.ag291541.task2.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class Content {
	@GeneratedValue
	@Id
	private Long id;
	@NotNull
	private String title;
	private String body;

	public Content() {
	}

	public Content(String title, String body) {
		this.title = title;
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Content [id=" + id + ", title=" + title + ", body=" + body
				+ "]";
	}
}
