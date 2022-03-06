package exception;

public class CurrencyExchangeRateException extends RuntimeException
/**
 * Exception thrown during currency conversion error
 */
{
    public CurrencyExchangeRateException( String message) {
        super(message);
    }

    public CurrencyExchangeRateException( Throwable t) {
        super(t);
    }
}
