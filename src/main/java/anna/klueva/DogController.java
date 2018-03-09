package anna.klueva;

import anna.klueva.dao.DogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public Dog updateDogById(@PathVariable int dogId, @RequestBody @Valid Dog dog/*, BindingResult bindingResult*/) throws SQLException {
       /*if (bindingResult.hasErrors()) {
           throw new RuntimeException("Invalid input object");
       }*/
       Dog result = new Dog();
       if(dogDAO.existsById(dogId)){
           Dog foundDog = dogDAO.findById(dogId).get();
           setDataToDog(dog, foundDog);
           result = dogDAO.save(foundDog);
       }
       return result;
    }

    @DeleteMapping(value="/{dogId}")
    public void deleteDogById(@PathVariable int dogId) throws SQLException {
        dogDAO.deleteById(dogId);
    }

    private void setDataToDog(Dog dog, Dog foundDog) {
        foundDog.setName(dog.getName());
        foundDog.setDateOfBirth(dog.getDateOfBirth());
        foundDog.setHeight(dog.getHeight());
        foundDog.setWeight(dog.getWeight());
    }
}
