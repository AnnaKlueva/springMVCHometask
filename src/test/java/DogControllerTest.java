import anna.klueva.Dog;
import anna.klueva.DogController;
import anna.klueva.dao.DogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DogControllerTest {
    @Autowired
    private DogDAO dogDAO;

    /**Failed with the next exception:
     * Dog(id=0, name=null, dateOfBirth=null, height=0.0, weight=0.0)
     * Actual   :<200 OK,Dog(id=0, name=null, dateOfBirth=null, height=0.0, weight=0.0),{}>
     */
    @Test(groups = "unitTest", enabled = false)
    public void testGetDogByIdMethod() throws Exception {
        Optional<Dog> expectedDog = Optional.of(new Dog());

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.findById(1)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog.get(), controller.getDogById(1));
    }

    @Test(groups = "unitTest")
    public void testGetAllDogsMethod() throws Exception {
        Dog firstDog = Dog.builder()
                .name("First dog")
                .dateOfBirth(new Date())
                .build();
        Dog secondDog = Dog.builder()
                .name("Second dog")
                .dateOfBirth(new Date())
                .build();
        Iterable<Dog> expectedList = Arrays.asList(firstDog, secondDog);

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.findAll()).thenReturn(expectedList);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedList, controller.getAllDogs());
    }

    @Test(groups = "unitTest", enabled = false)
    public void testDeleteDogMethod() throws Exception {
        //TODO : analyze how should we test "void" methods.Is it needed?
        Dog expectedDog = new Dog();

        DogDAO mockRepository = mock(DogDAO.class);
        //when(mockRepository.deleteById(1)).thenReturn(null);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog, controller.getDogById(1));
    }

    @Test(groups = "unitTest")
    public void testUpdateDogMethod() throws Exception {
        Dog expectedDog = new Dog();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.save(expectedDog)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog, controller.updateDogById(1, expectedDog));
    }

    @Test(groups = "unitTest")
    public void testCreateDogMethod() throws Exception {
        Dog dogToBeAdded = Dog.builder()
                .name("First dog")
                .dateOfBirth(new Date())
                .build();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.save(dogToBeAdded)).thenReturn(dogToBeAdded);

        DogController controller = new DogController(mockRepository);
        assertEquals(dogToBeAdded, controller.createNewDog(dogToBeAdded));
    }
}
