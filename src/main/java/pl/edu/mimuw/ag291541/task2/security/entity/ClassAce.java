package pl.edu.mimuw.ag291541.task2.security.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id",
		"canonicalTypeName" }) })
public class ClassAce {
	@GeneratedValue
	@Id
	private Long id;
	private Long user_id;
	@NotNull
	private ACLRights rightsType;
	@NotNull
	private String canonicalTypeName;

	public ClassAce() {
	}

	public ClassAce(Long user_id, ACLRights rightsType, String canonicalTypeName) {
		this.user_id = user_id;
		this.rightsType = rightsType;
		this.canonicalTypeName = canonicalTypeName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUser() {
		return user_id;
	}

	public void setUser(Long user_id) {
		this.user_id = user_id;
	}

	public ACLRights getRightsType() {
		return rightsType;
	}

	public void setRightsType(ACLRights rightsType) {
		this.rightsType = rightsType;
	}

	public String getCanonicalTypeName() {
		return canonicalTypeName;
	}

	public void setCanonicalTypeName(String canonicalTypeName) {
		this.canonicalTypeName = canonicalTypeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((canonicalTypeName == null) ? 0 : canonicalTypeName
						.hashCode());
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ClassAce))
			return false;
		ClassAce other = (ClassAce) obj;
		if (canonicalTypeName == null) {
			if (other.canonicalTypeName != null)
				return false;
		} else if (!canonicalTypeName.equals(other.canonicalTypeName))
			return false;
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClassAce [id=" + id + ", user_id=" + user_id + ", rightsType="
				+ rightsType + ", canonicalTypeName=" + canonicalTypeName + "]";
	}
}
