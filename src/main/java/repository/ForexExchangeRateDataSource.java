package repository;

import au.com.bytecode.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import model.ExchangeRate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import exception.CurrencyExchangeRateException;
import org.apache.commons.lang3.math.NumberUtils;

public final class ForexExchangeRateDataSource
{
    private String[] tableFields;
    private static ForexExchangeRateDataSource forexExchangeRateDataSource;

    private ForexExchangeRateDataSource()
    {

    }

    public static synchronized ForexExchangeRateDataSource getInstance()
    {
        if ( forexExchangeRateDataSource == null )
        {
            forexExchangeRateDataSource = new ForexExchangeRateDataSource();
        }

        return forexExchangeRateDataSource;
    }

    public Map<LocalDate, List<ExchangeRate>> getHistoricalExchangeRateData( String filePath )
    {
        return readCSVFile( filePath );
    }

    private Map<LocalDate, List<ExchangeRate>> readCSVFile( String filePath )
    {
        try
        {
            CSVReader reader = new CSVReader( new FileReader( filePath ), ',' );
            List<String[]> conversionRateData = reader.readAll();

            if ( conversionRateData == null )
            {
                throw new CsvValidationException( "The File " + filePath + " is not valid" );
            }
            tableFields = conversionRateData.get( 0 );

            return conversionRateData.stream()
                                     .skip( 1 )
                                     .map( this::getExchangeRates )
                                     .flatMap( Collection::stream )
                                     .collect( Collectors.groupingBy( ExchangeRate::getDate ) );
        }
        catch ( FileNotFoundException e )
        {
            throw new CurrencyExchangeRateException( e );
        }
        catch ( IOException e )
        {
            throw new CurrencyExchangeRateException( e );
        }
        catch ( Exception e )
        {
            throw new CurrencyExchangeRateException( e );
        }
    }

    private List<ExchangeRate> getExchangeRates( String[] row )
    {
        if ( row.length != tableFields.length )
            throw new CurrencyExchangeRateException( "Bad row in csv file: \n The no of exchange rate values in the following row is not equal the no of provided currency codes: \n" + Arrays.asList( row ) );

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        //skip the Date filed
        for ( int i = 1; i < row.length; i++ )
        {
            if ( tableFields[ i ].equals( "" ) )
                continue;
            ExchangeRate exchangeRate = new ExchangeRate();

            exchangeRate.setCurrencyCode( tableFields[ i ] );
            exchangeRate.setDate( LocalDate.parse( row[ 0 ] ) );

            if ( NumberUtils.isParsable( row[ i ] ) )
                exchangeRate.setRate( new BigDecimal( row[ i ] ) );
            else
                exchangeRate.setRate( null );

            exchangeRates.add( exchangeRate );
        }
        return exchangeRates;
    }

    public String[] getTableFields()
    {
        return tableFields;
    }
}
