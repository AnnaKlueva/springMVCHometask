import anna.klueva.Dog;
import anna.klueva.DogController;
import anna.klueva.dao.DogDAO;
import anna.klueva.errorHandling.RestErrorHandler;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Link with examples:
 * https://www.petrikainulainen.net/programming/spring-framework/unit-testing-of-spring-mvc-controllers-rest-api
 */
public class DogDAOTest {

    @Test(groups = "restApi")
    public void verifyGetByIdRequestOkResponse() throws Exception {
        Dog dog = Dog.builder()
                .name("Jack")
                .dateOfBirth(new Date())
                .height(10)
                .weight(5)
                .build();
        Optional<Dog> expectedDog = Optional.of(dog);

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.findById(1)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/dog/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(expectedDog.get().getId())))
                .andExpect(jsonPath("$.name", is(expectedDog.get().getName())))
                .andExpect(jsonPath("$.height", is(expectedDog.get().getHeight())))
                .andExpect(jsonPath("$.weight", is(expectedDog.get().getWeight())))
                .andExpect(jsonPath("$.dateOfBirth", is(expectedDog.get().getDateOfBirth().getTime())));

        verify(mockRepository, times(1)).findById(1);
        verifyNoMoreInteractions(mockRepository);
    }

    @Test(groups = "restApi")
    public void verifyGetByIdRequestNotFoundEntry_ShouldReturnHttpStatusCode404() throws Exception {
        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.findById(100)).thenReturn(Optional.empty());

        DogController controller = new DogController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/dog/100"))
                .andExpect(status().isNotFound());
    }

    @Test(groups = "restApi")
    public void verifyGetAllRequest() throws Exception {
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

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/dog/allDogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(firstDog.getId())))
                .andExpect(jsonPath("$[0].name", is(firstDog.getName())))
                .andExpect(jsonPath("$[0].height", is(firstDog.getHeight())))
                .andExpect(jsonPath("$[0].weight", is(firstDog.getWeight())))
                .andExpect(jsonPath("$[0].dateOfBirth", is(firstDog.getDateOfBirth().getTime())))
                .andExpect(jsonPath("$[1].id", is(secondDog.getId())))
                .andExpect(jsonPath("$[1].name", is(secondDog.getName())))
                .andExpect(jsonPath("$[1].height", is(secondDog.getHeight())))
                .andExpect(jsonPath("$[1].weight", is(secondDog.getWeight())));

        verify(mockRepository, times(1)).findAll();
        verifyNoMoreInteractions(mockRepository);
    }

    @Test(groups = "restApi")
    public void verifyAddDogRequest_200Ok() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("Correct dog")
                .dateOfBirth(new Date())
                .height(25)
                .weight(10)
                .build();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.save(expectedDog)).thenReturn(expectedDog);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/dog/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedDog)))
                .andExpect(status().isOk())
                .andExpect(content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(expectedDog.getId())))
                .andExpect(jsonPath("$.name", is(expectedDog.getName())))
                .andExpect(jsonPath("$.height", is(expectedDog.getHeight())))
                .andExpect(jsonPath("$.weight", is(expectedDog.getWeight())))
                .andExpect(jsonPath("$.dateOfBirth", is(expectedDog.getDateOfBirth().getTime())));
    }

    @Test(groups = "restApi")
    public void verifyAddDogRequest_404BadRequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("Incorrect dog")
                .dateOfBirth(new Date())
                .height(-25)
                .weight(10)
                .build();

        DogDAO mockRepository = mock(DogDAO.class);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new RestErrorHandler(mock(MessageSource.class)))
                .build();

        mockMvc.perform(post("/dog/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedDog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].message", containsString("The height must be more then 0")));
        verifyZeroInteractions(mockRepository);
    }

    @Test(groups = "restApi")
    public void verifyUpdateRequest_200Ok() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("Correct dog")
                .dateOfBirth(new Date())
                .height(25)
                .weight(10)
                .build();

        Dog updatedDog = Dog.builder()
                .name("Updated dog")
                .dateOfBirth(new Date())
                .height(15)
                .weight(5)
                .build();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.existsById(1)).thenReturn(true);
        when(mockRepository.findById(1)).thenReturn(Optional.of(expectedDog));
        when(mockRepository.save(updatedDog)).thenReturn(updatedDog);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(put("/dog/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDog)))
                .andExpect(status().isOk())
                .andExpect(content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(expectedDog.getId())))
                .andExpect(jsonPath("$.name", is(updatedDog.getName())))
                .andExpect(jsonPath("$.height", is(updatedDog.getHeight())))
                .andExpect(jsonPath("$.weight", is(updatedDog.getWeight())))
                .andExpect(jsonPath("$.dateOfBirth", is(updatedDog.getDateOfBirth().getTime())));
    }

    @Test(groups = "restApi")
    public void verifyUpdateRequest_404NotFound() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("Correct dog")
                .dateOfBirth(new Date())
                .height(25)
                .weight(10)
                .build();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.existsById(1)).thenReturn(false);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(put("/dog/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedDog)))
                .andExpect(status().isNotFound());
    }

    @Test(groups = "restApi")
    public void verifyDeleteRequest_200Ok() throws Exception {
        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.existsById(1)).thenReturn(true);
        doNothing().when(mockRepository).deleteById(1);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(delete("/dog/1"))
                .andExpect(status().isOk());
    }

    @Test(groups = "restApi")
    public void verifyDeleteRequest_400NotFound() throws Exception {
        Dog expectedDog = new Dog();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.existsById(1)).thenReturn(false);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(delete("/dog/1")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedDog)))
                .andExpect(status().isNotFound());
    }

    @Test(groups = "restApi")
    public void verifyFindAll_EmptyDB() throws Exception {
        Iterable<Dog> expectedList = Arrays.asList();

        DogDAO mockRepository = mock(DogDAO.class);
        when(mockRepository.findAll()).thenReturn(expectedList);
        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/dog/allDogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
