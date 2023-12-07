package userbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GameuserNotFoundAdvice {
	@ResponseBody
	@ExceptionHandler(GameuserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String GameUserNotFoundHandler(GameuserNotFoundException ex) {
		return ex.getMessage();
	}
}
