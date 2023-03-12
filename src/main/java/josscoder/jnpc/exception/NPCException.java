package josscoder.jnpc.exception;

public class NPCException extends RuntimeException {

    public NPCException(String message) {
        super(message);
    }

    public NPCException(String message, Throwable cause) {
        super(message, cause);
    }
}
