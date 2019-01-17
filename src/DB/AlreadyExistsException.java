package DB;

public class AlreadyExistsException extends Exception {
    public AlreadyExistsException (String errorMessage){
        super(errorMessage);
    }
}
