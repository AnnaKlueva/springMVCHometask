package anna.klueva;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@JsonRootName(value = "dog")
@Entity
@Table(name = "DOGTABLE")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class Dog implements Serializable {

    @NotNull(message = "The id can't be empty")
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @NotNull(message = "The name can't be empty")
    @Size(
            min=1,
            max=100,
            message = "The name '${validatedValue}' must be between {min} and {max} characters long")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Past(message = "Must be a past date")
    @Temporal(TemporalType.DATE)
    /*@JsonFormat(pattern="yyyy-MM-dd")*/
    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Min(value = 0, message = "The height must be more then 0")
    @Column(name = "height", nullable = false)
    private double height;

    @Min(value = 0, message = "The weight must be more then 0")
    @Column(name = "weight", nullable = false)
    private double weight;

    /*public Dog() {
        this.name = "Default dog";
        this.dateOfBirth = new Date();
        this.height = 100.0;
        this.weight = 10.0;
    }*/

/*    public Dog(String name, Date dateOfBirth, double height, double weight) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {return weight;}

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }*/
}
