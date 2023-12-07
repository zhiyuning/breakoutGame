package userbackend;

class GameuserNotFoundException extends RuntimeException{
	
	GameuserNotFoundException(Long id) {
    super("Could not find the user " + id);
  }
}
