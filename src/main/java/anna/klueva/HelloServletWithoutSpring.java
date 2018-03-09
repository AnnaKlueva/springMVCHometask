package anna.klueva;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet(value="/dog", name="helloServlet")
public class HelloServletWithoutSpring extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        int name =response.getStatus();
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        for (Dog dog : getDogCollection()) {
            response.getWriter().println(dog);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        //Add verification for OutOfBoundException
        String result = mapper.writeValueAsString(getDogCollection());
        response.getWriter().println(result);
    }

    private List<Dog> getDogCollection() {
        /*Dog dog = new Dog("Bob", new Date(2014,11,6),23,1);*/
        List<Dog> listWithDogs = new ArrayList<>();
       /* listWithDogs.add(dog);*/
        listWithDogs.add(new Dog());
        return listWithDogs;
    }
}
