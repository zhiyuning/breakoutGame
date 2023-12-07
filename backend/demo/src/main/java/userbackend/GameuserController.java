package userbackend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/gameusers")
public class GameuserController {
	//private final GameuserModelAssembler assembler;
	
	@Autowired
	private GameuserRepo gameuserRepo;
	
	@Autowired
    private GamehistoryRepo gamehistoryRepo;
	
	@PostMapping("/saveUser")
	public String saveUser(@RequestBody Gameuser gameuser) {
		System.out.println("User save called...");
		
		//check if duplicate user found (refer to the same username)
		Optional<Gameuser> existingUser = gameuserRepo.findByUsername(gameuser.getUsername());
		//check if user exist
		if(existingUser.isPresent()) {
			return "User already exists!";
		}
		
		
		//create sample user
		Gameuser user_in = new Gameuser(gameuser.getUsername(), gameuser.getPassword());
		List<Gamehistory> new_history = new ArrayList<>();
		for(Gamehistory his_in : gameuser.getHistory()) {
			Gamehistory his = new Gamehistory(his_in.getScore(), his_in.getStatus());
			
			his.setGameuser(user_in);
			new_history.add(his);
		}
		
		//add history to the user
		user_in.setHistory(new_history);
		
		//save user
		Gameuser user_out = gameuserRepo.save(user_in);
		System.out.println("user out :: " + user_out);
		System.out.println("Saved!");
		return "User saved!";
	}
	
	//login, if not found, will register new user
	@PostMapping("/loginUser")
	public String loginUser(@RequestBody Map<String, Object> requestData) {
		System.out.println("Regis called...");
		String username = (String) requestData.get("username");
	    String password = (String) requestData.get("password");
	    
		if(username.equals("") || password.equals("")) {
			return "EMPTY";
		}
		Optional<Gameuser> existingUser = gameuserRepo.findByUsername(username);
		//check if user exist
		if(existingUser.isPresent()) {
			Gameuser user = existingUser.get();
			//Then check the password
			String correct_pass = user.getPassword();
			System.out.println("found pass: " + password);
			System.out.println("cur pass: " + correct_pass);
			if(password.equals(correct_pass)) {
				return "LOGIN_SUCCESS";
			} else {
				return "WRONG";
			}
		}
		
		//if not, regis new user
		Gameuser user_in = new Gameuser(username, password);
		List<Gamehistory> new_history = new ArrayList<>();
		
		//add empty history to the user
		user_in.setHistory(new_history);
		
		//save user
		gameuserRepo.save(user_in);
		System.out.println("Register successfully!");
		return "REGIS_SUCCESS";
	}
	
	
	//save history to the given user
	@PostMapping("/saveHistory")
	public String saveHistory(@RequestBody Map<String, Object> requestData) {
		System.out.println("History save called...");
		
		String username = (String) requestData.get("username");
	    String status = (String) requestData.get("status");
	    int score = (int) requestData.get("score");
		
		//fetch user
		Optional<Gameuser> existingUser = gameuserRepo.findByUsername(username);
		if(!existingUser.isPresent()) {
			return "FETCH FAILED";
		}
		Gameuser user = existingUser.get();
		//List of history
		List<Gamehistory> histories = new ArrayList<>();
		
		//new history
		Gamehistory history = new Gamehistory(score, status);
		history.setGameuser(user);
		histories.add(history);
		
		//add history to the user
		user.addHistory(histories);
		
		//save the res
		gameuserRepo.save(user);
		System.out.println("Saved!!!");
		return "HISTORY_SAVED";
	}
	
	//get current user's all game history
	@GetMapping("/getUser/{name}")
	public String getUser(@PathVariable(name = "name") String name) {
		System.out.println("User get called...");
		
		//fetch user
		Optional<Gameuser> existingUser = gameuserRepo.findByUsername(name);
		if(!existingUser.isPresent()) {
			return "FETCH FAILED";
		}
		Gameuser user = existingUser.get();
		System.out.println("\nUser details :: \n" + user);
        System.out.println("\nDone!!!");
        return "USER_FETCHED";
	}
	
	//get all peoeple's score rank
	@GetMapping("/getRank")
	public String getRank() {
		List<Gamehistory> gameHistoryList = gamehistoryRepo.findAll();
		return gameHistoryList.toString();
	}
}
