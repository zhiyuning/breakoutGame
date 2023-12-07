package userbackend;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameuserRepo extends JpaRepository<Gameuser, Long>{
	Optional<Gameuser> findByUsername(String username);
}
