package repository;

import au.com.bytecode.opencsv.CSVReader;
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

public class HistoricalExchangeRateData
{
    private String[] tableFields;

    public Map<LocalDate, List<ExchangeRate>> getHistoricalExchangeRateData( String filePath )
    {
        return readCSVFile( filePath );
    }

    private Map<LocalDate, List<ExchangeRate>> readCSVFile( String filePath )
    {
        try
        {
            CSVReader reader = new CSVReader( new FileReader( filePath ) );//"C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" ), ',' );
            List<String[]> conversionRateData = reader.readAll();

            if ( ( tableFields = conversionRateData.get( 0 ) ) == null )
            {
                throw new Exception( "The File " + filePath + " is not valid" );
            }

            return conversionRateData.stream()
                                     .skip( 1 )
                                     .map( this::getExchangeRates )
                                     .flatMap( Collection::stream )
                                     .collect( Collectors.groupingBy( ExchangeRate::getDate ) );
        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return null;
    }

    private List<ExchangeRate> getExchangeRates( String[] row )
    {
        if ( row.length != tableFields.length )
            throw new CurrencyExchangeRateException( "exchange rate values in the following row is not equivalent to the provided currency codes: \n" + Arrays.asList( row ) );
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        //skip the Date filed
        for ( int i = 1; i < row.length; i++ )
        {
            if ( tableFields[ i ].equals( "" ) )
                continue;
            ExchangeRate exchangeRate = new ExchangeRate();

            exchangeRate.setCurrencyCode( tableFields[ i ] );
            exchangeRate.setDate( LocalDate.parse( row[ 0 ] ) );

            if ( NumberUtils.isCreatable( row[ i ] ) )
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
