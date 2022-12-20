package cinequiz.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import cinequiz.backend.model.User;

public interface UserRepository extends MongoRepository<User,String>{

}