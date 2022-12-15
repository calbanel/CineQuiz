package cinequiz.backend.ressource;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.annotations.ApiOperation;


import org.springframework.http.ResponseEntity;
import cinequiz.backend.model.User;
import cinequiz.backend.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UserController{
    @Autowired
    private UserRepository repository;

    @PostMapping("/add-user")
    @ApiOperation(value = "Adds a new user")
    public User addUser(@RequestBody User user){
		return repository.save(user);
		// System.out.println("OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK" + user);
        // return new ResponseEntity<User>(tmp, HttpStatus.CREATED); 
    }

    @GetMapping("/find-all-users")
    @ApiOperation(value = "Finds all users")
	public List<User> getUsers() {
		return repository.findAll();
	}

	@GetMapping("/find-user/{id}")
    @ApiOperation(value = "Finds a user having a specific id")
	public Optional<User> getUser(@PathVariable int id) {
		return repository.findById(id);
	}

	@DeleteMapping("/delete-user/{id}")
    @ApiOperation(value = "Deletes a user having a specific id")
	public String deleteUser(@PathVariable int id) {
		repository.deleteById(id);
		return "Deleted user with id : " + id;
	}

}
