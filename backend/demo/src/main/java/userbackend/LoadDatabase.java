package userbackend;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(GameuserRepo repository, GamehistoryRepo historyRepository) {

      return args -> {

          //Add sample history data
          Gameuser user1 = new Gameuser("Garen", "123123");
          
          Gamehistory gamehistory1 = new Gamehistory(1, "SUCCESS");
          Gamehistory gamehistory2 = new Gamehistory(2, "SUCCESS");
          gamehistory1.setGameuser(user1);
          gamehistory2.setGameuser(user1);
          //gamehistory2.setGameuser(user1);
          
          List<Gamehistory> historylist = new ArrayList<Gamehistory>();
          historylist.add(gamehistory1);
          historylist.add(gamehistory2);
          
          user1.setHistory(historylist);
          
          repository.save(user1);
          
          Gameuser user2 = new Gameuser("Lux", "sassword");
          
          Gamehistory gamehistory21 = new Gamehistory(1000, "FAILED");
          Gamehistory gamehistory22 = new Gamehistory(340, "SUCCESS");
          gamehistory21.setGameuser(user2);
          gamehistory22.setGameuser(user2);
          //gamehistory2.setGameuser(user1);
          
          List<Gamehistory> historylist2 = new ArrayList<Gamehistory>();
          historylist2.add(gamehistory21);
          historylist2.add(gamehistory22);
          
          user1.setHistory(historylist2);
          
          repository.save(user2);
      };
  }
}
