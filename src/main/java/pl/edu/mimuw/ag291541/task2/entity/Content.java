package pl.edu.mimuw.ag291541.task2.entity;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import pl.edu.mimuw.ag291541.task2.security.annotation.AclGuarded;

@Entity
@Table(name = "content")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "contenttype", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Content")
@AclGuarded
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
