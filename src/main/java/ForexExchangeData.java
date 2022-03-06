import com.opencsv.exceptions.CsvValidationException;
import service.ExchangeRateService;
import service.ExchangeRateServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ForexExchangeData
{

    public static void main( String[] args ) throws IOException, CsvValidationException, Exception
    {

        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" );

        System.out.println( exchangeRateService.getExchangeRate( "1900-02-18" ) );

        System.out.println( exchangeRateService.getExchangeRateConversion( "USD", "GBP", "100", "2022-02-18" ) );
        // System.out.println( exchangeRateService.getExchangeRate(  "2022-02-18" ));
        System.out.println( exchangeRateService.getAverageExchangeRate( "2022-01-01", "2022-02-18", "USD" ) );
        // System.out.println(exchangeRateService.getHighestExchangeRate( "01-01-2022", "2022-02-18", "USD" ));
        System.out.println( exchangeRateService.getHighestExchangeRate( "2022-01-01", "2022-02-18", "USD" ) );

        System.out.println( exchangeRateService.getExchangeRate( "2002-02-18" ) );
        System.out.println( exchangeRateService.getExchangeRate( "2009-02-18" ) );
        System.out.println( exchangeRateService.getExchangeRate( "2000-02-18" ) );
        System.out.println( exchangeRateService.getExchangeRate( "1900-02-18" ) );

    }
}





