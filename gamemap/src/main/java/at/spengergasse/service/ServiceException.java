package at.spengergasse.service;

import java.util.function.Supplier;


public class ServiceException extends RuntimeException {

    public static Supplier<RuntimeException> ofMapNotFound = () -> {
      throw new ServiceException( "map does not exist" );
    };

    public static Supplier<RuntimeException> ofUserNotFound = () -> {
        throw new ServiceException( "user does not exist" );
    };

    public ServiceException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ServiceException( String message ) {
        super( message );
    }

    public ServiceException( Exception ex ) {
        super( ex );
    }
}
