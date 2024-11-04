package in.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.main.entity.User;


public interface UserRepository extends JpaRepository<User,Integer>{
	
	User findByUsername(String username);

}
