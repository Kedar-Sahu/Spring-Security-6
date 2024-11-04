package in.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import in.main.dto.UserRequest;
import in.main.entity.User;
import in.main.repository.UserRepository;
import in.main.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public String login(UserRequest userRequest) {
		Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
		if(authenticate.isAuthenticated()) {
			//return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
			return jwtService.generateToken(userRequest.getUsername());
		}
		return null;
	}
	
	@Override
	public User create(User user) {
		String password=passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		return userRepository.save(user);
	}

	@Override
	public User getUser(Integer id) {
		User user=userRepository.findById(id).orElseThrow(()-> new RuntimeException("user not found"));
		return user;
	}

	@Override
	public List<User> getAll() {
		return userRepository.findAll();
	}

	@Override
	public Boolean delete(Integer id) {
		User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found"));
		if(user==null) {
			return false;
		}
		userRepository.delete(user);
		return true;
	}

	

}
