package ucb.edu.bo.Proyecto.dto.exceptions;

public class RegisterNotFoundException extends RuntimeException{
    public RegisterNotFoundException(String message){
        super(message);
    }
}
