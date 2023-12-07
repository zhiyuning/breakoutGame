package userbackend;

import java.util.ArrayList;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "GAME_USER")
public class Gameuser {
	
	//automatically generated id
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gameuser_id")
	private Long gameuser_id;

//	private static int plays = 0;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
//	@Column(name = "high_score")
//	private int HighScore;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gameuser", cascade = CascadeType.ALL)
    private List<Gamehistory> history = new ArrayList<>();

	Gameuser() {}

	public Gameuser(String username, String password) {
		System.out.println(username);
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return this.username;
	} 
	
	public Long getId() {
		return this.gameuser_id;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setName(String username) {
		this.username = username;
	} 
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setId(Long id) {
		this.gameuser_id = id;
	}

	public List<Gamehistory> getHistory(){
		return this.history;
	}
	
	public void setHistory(List<Gamehistory> history) {
		this.history = history;
	}
	
	public void addHistory(List<Gamehistory> history) {
		for(Gamehistory g : history) {
			this.history.add(g);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Gameuser))
			return false;
		Gameuser user = (Gameuser) o;
		return Objects.equals(this.gameuser_id, user.gameuser_id) && Objects.equals(this.username, user.username)
				&& Objects.equals(this.password, user.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.gameuser_id, this.username, this.password);
	}

	@Override
	public String toString() {
		return "GameUser{" + "id=" + this.gameuser_id + ", username='" + this.username + '\'' + ", password='" + this.password + '\'' + '}';
  }
}
