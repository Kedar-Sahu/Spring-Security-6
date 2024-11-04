package in.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.main.dto.UserRequest;
import in.main.entity.User;
import in.main.service.UserService;

@RestController
public class HomeController {
	
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserRequest userRequest){
		String token=userService.login(userRequest);
		if(token==null) {
			return new ResponseEntity<>("invalid credencial",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(token,HttpStatus.OK);
	}
	
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody User user){
		return new ResponseEntity<>(userService.create(user),HttpStatus.CREATED);
	}

	@GetMapping("/getUser/{id}")
	public ResponseEntity<User> getUser(@PathVariable Integer id){
		return new ResponseEntity<>(userService.getUser(id),HttpStatus.OK);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<User>> getAll(){
		return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id){
		Boolean user=userService.delete(id);
		if(user) {
			return new ResponseEntity<>("user delete successfully",HttpStatus.OK);
		}
		return new ResponseEntity<>("user delete failed",HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
