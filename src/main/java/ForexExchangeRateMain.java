import com.opencsv.exceptions.CsvValidationException;
import service.ExchangeRateService;
import service.ExchangeRateServiceImpl;

import java.io.IOException;


public class ForexExchangeRateMain
{

    public static void main( String[] args ) throws IOException, CsvValidationException
    {
        ForexExchangeRateMain forexExchangeData = new ForexExchangeRateMain();
        String filePath = forexExchangeData.getClass()
                                           .getClassLoader()
                                           .getResource( "eurofxref-hist.csv" ).getPath();

        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl( filePath);



        System.out.println( exchangeRateService.getExchangeRateConversion( "GBP", "USD", "100", "2022-02-18" ) );

        System.out.println( exchangeRateService.getExchangeRateConversion( "USD", "GBP", "100", "2022-02-18" ) );
        System.out.println( exchangeRateService.getAverageExchangeRate( "2022-01-01", "2022-03-04", "USD" ) );

        System.out.println( exchangeRateService.getHighestExchangeRate( "2022-01-01", "2022-02-18", "USD" ) );

        System.out.println( exchangeRateService.getExchangeRate( "2002-02-18" ) );

    }
}





