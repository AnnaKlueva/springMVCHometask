import anna.klueva.Dog;
import anna.klueva.DogController;
import anna.klueva.dao.DogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DogControllerTest {
    @Autowired
    private DogDAO dogDAO;

    @Test(enabled = false)
    public void testGetDogByIdMethod() throws Exception {
        Optional<Dog> expectedDog = Optional.of(new Dog());

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.findById(1)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog.get(), controller.getDogById(1));
    }

   /* @Test(enabled = false)
    public void testGetAllDogsMethod() throws Exception {
        //TODO : change mocked methods
        Dog expectedDog = new Dog();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.getDogById(1)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog, controller.getDogById(1));
    }

    @Test(enabled = false)
    public void testDeleteDogMethod() throws Exception {
        //TODO : change mocked methods
        Dog expectedDog = new Dog();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.getDogById(1)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog, controller.getDogById(1));
    }

    @Test(enabled = false)
    public void testUpdateDogMethod() throws Exception {
        //TODO : change mocked methods
        Dog expectedDog = new Dog();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.getDogById(1)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog, controller.getDogById(1));
    }

    @Test(enabled = false)
    public void testAddDogMethod() throws Exception {
        //TODO : change mocked methods
        Dog expectedDog = new Dog();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.getDogById(1)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);
        assertEquals(expectedDog, controller.getDogById(1));
    }*/
}
