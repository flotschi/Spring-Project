package at.spengergasse.foundation;

public class Ensurer {

    public static void ensureNotNull( Object obj ) {
        if ( obj == null ) {
            throw new IllegalArgumentException( "object is null" );
        }
    }
}
