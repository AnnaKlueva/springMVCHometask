package anna.klueva.errorHandling;

/**
 * Created by akliuieva on 2/15/18.
 */
public class FieldErrorDTO {

    private String field;
    private String message;

    public FieldErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }
}