package exception;

public class DateNotValidException extends RuntimeException
        /**
         * Exception thrown during date conversion from string to LocalDate
         */
{
    public DateNotValidException( String message )
    {
        super( message );
    }

    public DateNotValidException( Throwable t )
    {
        super( t );
    }

}
