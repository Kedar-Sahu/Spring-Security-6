package in.main.service;

import java.util.List;

import in.main.dto.UserRequest;
import in.main.entity.User;

public interface UserService {

	User create(User user);
	User getUser(Integer id);
	List<User> getAll();
	Boolean delete(Integer id);
	
	String login(UserRequest userRequest);
}
