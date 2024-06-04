package ucb.edu.bo.Proyecto.dto.responses;

import java.util.HashMap;
import java.util.Map;

public class ValidationErrorsResponse {
    private Map<String, String> errors;

    public ValidationErrorsResponse() {
        this.errors = new HashMap<>();
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String key, String descripcion) {
        this.errors.put(key, descripcion);
    }
}
