package userbackend;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "GAME_HISTORY")
public class Gamehistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gamehistory_id")
    private Long gamehistory_id;

	@Column(name = "score")
	private int score;
	
	@Column(name = "status")
    private String status;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "gameuser_id")
    private Gameuser gameuser;
	
	Gamehistory(){}
	
	public Gamehistory(int score, String status) {
        this.score = score;
        this.status = status;
    }
	
	public Long getId() {
		return this.gamehistory_id;
	}

	public int getScore() {
		return this.score;
	}

	public String getStatus() {
	    return this.status;
	}

	public void setId(Long id) {
	    this.gamehistory_id = id;
	}

	public void setScore(int score) {
	    this.score = score;
	}

	public void setStatus(String status) {
	    this.status = status;
	}
	
	public void setGameuser(Gameuser gameuser) {
        this.gameuser = gameuser;
        // Ensure bidirectional relationship is managed on both sides
        if (gameuser != null && !gameuser.getHistory().contains(this)) {
            gameuser.getHistory().add(this);
        }
    }
	
	public Gameuser getGameuser() {
		return this.gameuser;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
	      return true;
	    if (!(o instanceof Gamehistory))
	      return false;
	    Gamehistory gamehistory = (Gamehistory) o;
	    return Objects.equals(this.gamehistory_id, gamehistory.gamehistory_id) && Objects.equals(this.score, gamehistory.score)
	        && this.status == gamehistory.status;
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(this.gamehistory_id, this.score, this.status);
	}

	@Override
	public String toString() {
	    return "History{" + "id=" + this.gamehistory_id + ", score='" + this.score + '\'' + ", status=" + this.status + ", player=" + this.gameuser.getUsername() + '}';
	}
}
