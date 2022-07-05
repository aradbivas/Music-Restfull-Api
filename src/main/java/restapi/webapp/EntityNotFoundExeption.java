package restapi.webapp;

public class EntityNotFoundExeption extends RuntimeException{
    public EntityNotFoundExeption(Long id){
        super("There is not entity corresponding to id = " + id);

        /*
        This custom exception is useless without invocation from one or more controllers
        @ExceptionHandler - use the exception in a specific controller
        @ControllerAdvice - use the exception in more than one controller
         */
    }
    public EntityNotFoundExeption(String email){
        super("There is not entity corresponding to email = " + email);

        /*
        This custom exception is useless without invocation from one or more controllers
        @ExceptionHandler - use the exception in a specific controller
        @ControllerAdvice - use the exception in more than one controller
         */
    }
}
