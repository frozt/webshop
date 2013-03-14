package entities;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: users
 *
 */
@Entity
public class Users implements Serializable {

	   
	@Id
	private String username;
	private String password;
	private boolean isActive;
	private static final long serialVersionUID = 1L;

	public Users() {
		super();
	}   
	public Users(String username, String password, boolean isActive)
	{
		this.username=username;
		this.password=password;
		this.isActive=isActive;
	}
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}   
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}   
	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        return this.username == other.username && this.password == other.password;
    }
   
}
