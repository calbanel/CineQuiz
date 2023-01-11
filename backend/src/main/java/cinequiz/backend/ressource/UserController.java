package cinequiz.backend.ressource;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.annotations.ApiOperation;
import cinequiz.backend.model.Game;
import cinequiz.backend.model.User;
import cinequiz.backend.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
public class UserController {
	@Autowired
	private UserRepository repository;

	@PostMapping("/connection")
	@ApiOperation(value = "Connects the user if they exist")
	public ResponseEntity<?> login(@RequestBody User user) {
		Optional<User> userToFind = this.repository.findByEmail(user.getEmail());
		if (userToFind.isPresent()) {
			User userRequested = userToFind.get();
			if (user.getPassword().equals(userRequested.getPassword())) {
				return ResponseEntity.status(201).body(userRequested);
			} else {
				return ResponseEntity.status(403).body("Wrong Password");
			}
		} else {
			return ResponseEntity.status(403).body("Wrong email");
		}
	}

	@PostMapping("/add-user")
	@ApiOperation(value = "Adds a new user")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		Optional<User> userToFind = this.repository.findByEmail(user.getEmail());
		if (userToFind.isPresent()) {
			return ResponseEntity.status(403).body("Email already used");
		} else {
			return ResponseEntity.status(201).body(repository.save(user));
		}
	}

	@GetMapping("/find-all-users")
	@ApiOperation(value = "Finds all users")
	public List<User> getUsers() {
		return repository.findAll();
	}

	@PostMapping("/update-user/")
	@ApiOperation(value = "Updates a user")
	public User updateUser(@RequestBody User user) {
		return repository.save(user);
	}

	@GetMapping("/find-user-by-email/{email}")
	@ApiOperation(value = "Finds a user having a specific email")
	public Optional<User> getUserByEmail(@PathVariable String email) {
		return repository.findByEmail(email);
	}

	@GetMapping("/find-user/{id}")
	@ApiOperation(value = "Finds a user having a specific id")
	public Optional<User> getUser(@PathVariable String id) {
		return repository.findById(id);
	}

	@DeleteMapping("/delete-user/{id}")
	@ApiOperation(value = "Deletes a user having a specific id")
	public String deleteUser(@PathVariable String id) {
		repository.deleteById(id);
		return "Deleted user with id : " + id;
	}

	@PostMapping("/add-game-to-user/{id}")
	@ApiOperation(value = "Adds a new user")
	public ResponseEntity<?> addUser(@PathVariable String id, @RequestBody Game game) {
		Optional<User> optionalUser = repository.findById(id);
		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(403).body("Bad user ID");
		} else {
			User user = optionalUser.get();
			user.addGame(game);
			return ResponseEntity.status(200).body(repository.save(user));
		}
	}

}
