package RizpaEngine;

public class FileException extends Exception {

    public FileException(String message,Throwable cause)  {
        super(message, cause);
    }

    @Override
    public String toString() {
            return getLocalizedMessage();
    }
}
