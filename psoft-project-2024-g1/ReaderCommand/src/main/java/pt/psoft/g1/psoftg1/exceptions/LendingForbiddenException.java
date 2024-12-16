package pt.psoft.g1.psoftg1.exceptions;


public class LendingForbiddenException extends RuntimeException {

    public LendingForbiddenException(final String string) {
        super(string);
    }

    public LendingForbiddenException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
