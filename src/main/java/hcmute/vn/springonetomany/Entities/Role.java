package hcmute.vn.springonetomany.Entities;

import lombok.Getter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 45)
	private String name;

	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new LinkedHashSet<>();

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Role() { }
	
	public Role(String name) {
		this.name = name;
	}
	
	public Role(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Role(Integer id) {
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
