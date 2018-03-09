
import anna.klueva.Dog;
import anna.klueva.DogController;
import anna.klueva.RestErrorHandler;
import anna.klueva.dao.DogDAO;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.testng.annotations.Test;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by akliuieva on 12/21/17.
 */
public class DogDAOTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    /**Link with examples:
     * https://www.petrikainulainen.net/programming/spring-framework/unit-testing-of-spring-mvc-controllers-rest-api
     * */

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
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
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

        ResultActions result = mockMvc.perform(get("/dog/100"))
                .andDo(print())
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

        ResultActions result = mockMvc.perform(get("/dog/allDogs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(firstDog.getId())))
                .andExpect(jsonPath("$[0].name", is(firstDog.getName())))
                .andExpect(jsonPath("$[0].height", is(firstDog.getHeight())))
                .andExpect(jsonPath("$[0].weight", is(firstDog.getWeight())))
        //TODO: investigate why dateOfBirth is not converted in date object
        //  .andExpect(jsonPath("$.dateOfBirth", is(expectedDog.get().getDateOfBirth())));
                .andExpect(jsonPath("$[1].id", is(secondDog.getId())))
                .andExpect(jsonPath("$[1].name", is(secondDog.getName())))
                .andExpect(jsonPath("$[1].height", is(secondDog.getHeight())))
                .andExpect(jsonPath("$[1].weight", is(secondDog.getWeight())));

        verify(mockRepository, times(1)).findAll();
        verifyNoMoreInteractions(mockRepository);
    }

    //TODO: add verifications for valid object
    /**
     * id - not null, unique, auto generate
     * name - 1-100 symbols, not null
     date of birth - must be before NOW, optional
     height, weight - must be greater than 0, not null
     */
    @Test(groups = "restApi"/*, enabled = false*/)
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

        ResultActions result = mockMvc.perform(post("/dog/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedDog)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType
                        (MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(expectedDog.getId())))
                .andExpect(jsonPath("$.name", is(expectedDog.getName())))
                .andExpect(jsonPath("$.height", is(expectedDog.getHeight())))
                .andExpect(jsonPath("$.weight", is(expectedDog.getWeight())))
                .andExpect(jsonPath("$.dateOfBirth", is(expectedDog.getDateOfBirth().getTime())));
    }

    @Test(groups = "restApi", enabled = false)
    public void verifyAddDogRequest_400Badrequest() throws Exception {
        Dog expectedDog = Dog.builder()
                .name("Correct dog")
                .dateOfBirth(new Date())
                .height(-25)
                .weight(10)
                .build();

        DogDAO mockRepository = mock(DogDAO.class);

        DogController controller = new DogController(mockRepository);

        MockMvc mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new RestErrorHandler(mock(MessageSource.class)))
                .build();

        ResultActions result = mockMvc.perform(post("/dog/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expectedDog)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[*].message", containsString("The height must be more then 0")));
        verifyZeroInteractions(mockRepository);
    }
}
