package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceImplTest
{
    ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp()
    {
        String filePath = getClass()
                .getClassLoader()
                .getResource( "testEurofxref.csv" ).getPath();

        exchangeRateService = new ExchangeRateServiceImpl( filePath );

    }

    @Test
    void getExchangeRateTest()
    {
        //Date,USD,JPY,BGN,CYP,CZK,DKK

        // 18/02/2022,1.1354,130.59,1.9558,N/A,24.337,7.4382

        Map<String, String> exchangeRateExpected = new HashMap<String, String>();
        exchangeRateExpected.put( "USD", "1.1354" );
        exchangeRateExpected.put( "JPY", "130.59" );
        exchangeRateExpected.put( "BGN", "1.9558" );
        exchangeRateExpected.put( "CYP", "Rate not available" );
        exchangeRateExpected.put( "CZK", "24.337" );
        exchangeRateExpected.put( "DKK", "7.4382" );

        Map<String, String> exchangeRateActual = exchangeRateService.getExchangeRate( "2022-02-18" );
        assertEquals( exchangeRateExpected, exchangeRateActual );

    }

    @Test
    void testGetExchangeRateConversionTest()
    {
        String targetAmountActual = exchangeRateService.getExchangeRateConversion( "USD", "DKK", "100", "2021-12-15" );

        assertEquals( "661.822", targetAmountActual );
    }

    @Test
    void getHighestExchangeRateTest()
    {
        Map<String, Map<String, String>> highestExchangeRateExpected = new HashMap<>();
        Map<String, String> currencyVal = new HashMap<>();
        currencyVal.put( "USD", "1.126" );
        highestExchangeRateExpected.put( "2022-02-01", currencyVal );

        Map<String, Map<String, String>> highestExchangeRateActual = exchangeRateService.getHighestExchangeRate( "2022-02-01", "2022-02-18", "USD" );

        assertEquals( highestExchangeRateExpected, highestExchangeRateActual );
    }

    @Test
    void getAverageExchangeRateTest()
    {
        String averageExchangeRateActual = exchangeRateService.getAverageExchangeRate( "2022-02-01", "2022-02-10", "DKK" );
        String averageExchangeRateExpected = "7.44164";
        assertEquals( averageExchangeRateExpected, averageExchangeRateActual );
    }
}