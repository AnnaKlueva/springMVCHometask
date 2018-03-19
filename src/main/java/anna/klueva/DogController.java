package anna.klueva;

import anna.klueva.dao.DogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/dog")
public class DogController {
    @Autowired
    private DogDAO dogDAO;

    public DogController(DogDAO repository) {
        dogDAO = repository;
    }

    @GetMapping("/{dogId}")
    public ResponseEntity<Dog> getDogById(@PathVariable int dogId) throws SQLException {
        Optional<Dog> response = dogDAO.findById(dogId);
        return response.isPresent() ? new ResponseEntity<>(response.get(), OK) : new ResponseEntity<>( NOT_FOUND);
    }

    @GetMapping("/allDogs")
    public Iterable<Dog> getAllDogs() throws SQLException {
        return dogDAO.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Dog createNewDog(@RequestBody @Valid Dog dog) {
        return dogDAO.save(dog);
    }

    @PutMapping(value="/{dogId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Dog> updateDogById(@PathVariable int dogId, @RequestBody @Valid Dog dog) throws SQLException {
       ResponseEntity<Dog> result;
       if(dogDAO.existsById(dogId)){
           Dog foundDog = dogDAO.findById(dogId).get();
           setDataToDog(dog, foundDog);
           result = new ResponseEntity<>(dogDAO.save(foundDog), OK);
       }
       else{
           result = new ResponseEntity<>( NOT_FOUND );
       }
       return result;
    }

    @DeleteMapping(value="/{dogId}")
    public ResponseEntity<String> deleteDogById(@PathVariable int dogId) throws SQLException {
        ResponseEntity<String> response;
        if(dogDAO.existsById(dogId)){
            dogDAO.deleteById(dogId);
            response = new ResponseEntity<>(OK);
        }
        else {
            response = new ResponseEntity<>("Item with id="+dogId+" is not exist", NOT_FOUND);
        }
        return response;
    }

    private void setDataToDog(Dog dog, Dog foundDog) {
        foundDog.setName(dog.getName());
        foundDog.setDateOfBirth(dog.getDateOfBirth());
        foundDog.setHeight(dog.getHeight());
        foundDog.setWeight(dog.getWeight());
    }
}
