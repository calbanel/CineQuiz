package cinequiz.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import cinequiz.backend.model.User;

public interface UserRepository extends MongoRepository<User,String>{
    public Optional<User> findByEmail(String email);

}