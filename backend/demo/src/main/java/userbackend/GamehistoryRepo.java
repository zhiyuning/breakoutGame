package userbackend;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GamehistoryRepo extends JpaRepository<Gamehistory, Long>{
}
