package restapi.webapp;

public class UserNotFoundExeption extends RuntimeException{
    public UserNotFoundExeption(Long id){
        super("There is not user corresponding to id = " + id);

        /*
        This custom exception is useless without invocation from one or more controllers
        @ExceptionHandler - use the exception in a specific controller
        @ControllerAdvice - use the exception in more than one controller
         */
    }
    public UserNotFoundExeption(String id){
        super("There is not user corresponding to email = " + id);

        /*
        This custom exception is useless without invocation from one or more controllers
        @ExceptionHandler - use the exception in a specific controller
        @ControllerAdvice - use the exception in more than one controller
         */
    }
}
