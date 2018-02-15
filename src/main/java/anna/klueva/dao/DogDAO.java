package anna.klueva.dao;

import anna.klueva.Dog;

import java.sql.SQLException;
import java.util.Collection;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
/*@Transactional*/
public interface DogDAO extends CrudRepository<Dog, Integer>{
}
