package pl.edu.mimuw.ag291541.task2.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import pl.edu.mimuw.ag291541.task2.security.ACLRights;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id",
		"objecthashcode" }) })
public class InstanceAce {
	@GeneratedValue
	@Id
	private Long id;
	private Long user_id;
	@NotNull
	private ACLRights rightsType;
	@NotNull
	@Column(name = "objecthashcode")
	private Integer objectHashCode;

	public InstanceAce() {
	}

	public InstanceAce(Long user_id, ACLRights rightsType,
			Integer objectHashCode) {
		super();
		this.user_id = user_id;
		this.rightsType = rightsType;
		this.objectHashCode = objectHashCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
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

	public Integer getObjectHashCode() {
		return objectHashCode;
	}

	public void setObjectHashCode(Integer objectHashCode) {
		this.objectHashCode = objectHashCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((objectHashCode == null) ? 0 : objectHashCode.hashCode());
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof InstanceAce))
			return false;
		InstanceAce other = (InstanceAce) obj;
		if (objectHashCode == null) {
			if (other.objectHashCode != null)
				return false;
		} else if (!objectHashCode.equals(other.objectHashCode))
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
		return "InstanceAce [id=" + id + ", user_id=" + user_id
				+ ", rightsType=" + rightsType + ", objectHashCode="
				+ objectHashCode + "]";
	}

}
