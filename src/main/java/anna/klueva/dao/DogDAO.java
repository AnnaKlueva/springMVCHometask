package anna.klueva.dao;

import anna.klueva.Dog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogDAO extends CrudRepository<Dog, Integer>{
}
