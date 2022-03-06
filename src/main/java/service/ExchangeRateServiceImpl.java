package service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import exception.CurrencyExchangeRateException;
import exception.DateNotValidException;
import model.ExchangeRate;
import org.apache.commons.lang3.StringUtils;
import repository.ForexExchangeRateDataSource;
import utility.DateValidator;

public class ExchangeRateServiceImpl implements ExchangeRateService
{
    private Map<LocalDate, List<ExchangeRate>> exchangeRateData;
    private String[] availableCurrencies;
    private static final String dateIsoFormat = "yyyy-MM-dd";
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern( dateIsoFormat );

    public ExchangeRateServiceImpl( String filePath )
    {
        ForexExchangeRateDataSource forexExchangeRateDataSource = ForexExchangeRateDataSource.getInstance();

        exchangeRateData = forexExchangeRateDataSource.getHistoricalExchangeRateData( filePath );

        availableCurrencies = forexExchangeRateDataSource.getTableFields();

    }

    public List<String> getAvailableCurrencies()
    {
        return Arrays.asList( availableCurrencies );
    }

    private BigDecimal getExchangeRateValue( String currencyCode, LocalDate date )
    {
        if ( !isCurrencyCodeExists( currencyCode ) )
            throw new CurrencyExchangeRateException( String.format( "Exchange rate for currency code %s does not exist", currencyCode ) );
        return exchangeRateData.get( date ).stream()
                               .filter( s -> s.getCurrencyCode().equals( currencyCode ) )
                               .map( s -> s.getRate() )
                               .findAny().get();
    }

    @Override
    public String getExchangeRateConversion( String sourceCurrencyCode, String targetCurrencyCode, String sourceAmount, String date ) throws CurrencyExchangeRateException
    {
        BigDecimal sourceCurrExchangeRate = BigDecimal.ONE;
        BigDecimal targetCurrExchangeRate = BigDecimal.ONE;

        if ( !StringUtils.equals( "EUR", sourceCurrencyCode ) )
            sourceCurrExchangeRate = getExchangeRateValue( sourceCurrencyCode, getDate( date ) );

        if ( !StringUtils.equals( "EUR", targetCurrencyCode ) )
            targetCurrExchangeRate = getExchangeRateValue( targetCurrencyCode, getDate( date ) );

        if ( sourceCurrExchangeRate == null && !StringUtils.equals( sourceCurrencyCode, "EUR" ) )
            throw new CurrencyExchangeRateException( String.format( "the exchange rate for the %s is not available on %s", sourceCurrencyCode, date ) );
        if ( targetCurrExchangeRate == null && !StringUtils.equals( targetCurrencyCode, "EUR" ) )
            throw new CurrencyExchangeRateException( String.format( "the exchange rate for the %s is not available on %s", targetCurrencyCode, date ) );

        BigDecimal srcAmount = new BigDecimal( sourceAmount );
        BigDecimal targetAmount;

        if ( StringUtils.equals( targetCurrencyCode, "EUR" ) || StringUtils.equals( sourceCurrencyCode, "EUR" ) )
        {
            if ( StringUtils.equals( sourceCurrencyCode, "EUR" ) )
                targetAmount = srcAmount.multiply( targetCurrExchangeRate );
            else
                targetAmount = srcAmount.divide( targetCurrExchangeRate );
        }
        else
        {
            BigDecimal srcAmountInEuro = srcAmount.divide( sourceCurrExchangeRate, RoundingMode.HALF_EVEN );
            targetAmount = srcAmountInEuro.multiply( targetCurrExchangeRate, new MathContext( 6 ) );
        }

        return targetAmount.toString();
    }

    @Override
    public Map<String, String> getExchangeRate( String date ) throws CurrencyExchangeRateException
    {
        return Optional.ofNullable( exchangeRateData.get( getDate( date ) ) )
                       .orElseGet( Collections::emptyList )
                       .stream()
                       .collect( Collectors.toMap( s -> s.getCurrencyCode(), s -> s.getRate() != null ? s.getRate().toString() : "Rate not available" ) );

        /*  return exchangeRateData.get( dttm ).stream()
                           .collect( Collectors.toMap( s -> s.getCurrencyCode(), s -> s.getRate() != null ? s.getRate().toString() : "Rate not available" ) );

         */
    }

    @Override
    public Map<String, Map<String, String>> getHighestExchangeRate( String fromDate, String toDate, String currencyCode ) throws CurrencyExchangeRateException
    {
        LocalDate fromDttm = getDate( fromDate );
        LocalDate toDttm = getDate( toDate );

        if ( !isCurrencyCodeExists( currencyCode ) )
            throw new CurrencyExchangeRateException( "Exchange rate for currency code " + currencyCode + " does not exist" );

        Comparator<ExchangeRate> comparator = Comparator.comparing( ExchangeRate::getRate );

        ExchangeRate rate = exchangeRateData.entrySet().stream()
                                            .filter( s -> s.getKey().isEqual( fromDttm ) || s.getKey().isAfter( fromDttm ) )
                                            .filter( s -> s.getKey().isEqual( toDttm ) || s.getKey().isBefore( toDttm ) )
                                            .map( s -> s.getValue() )
                                            .flatMap( Collection::stream )
                                            .filter( s -> s.getCurrencyCode().equals( currencyCode ) )
                                            .filter( s -> s.getRate() != null )
                                            .min( comparator ).get();

        return Collections.singletonMap( rate.getDate().toString(), Collections.singletonMap( rate.getCurrencyCode(), rate.getRate().toString() ) );
    }

    @Override
    public String getAverageExchangeRate( String fromDate, String toDate, String currencyCode ) throws CurrencyExchangeRateException
    {
        LocalDate fromDttm = getDate( fromDate );
        LocalDate toDttm = getDate( toDate );

        BigDecimal[] totalWithCount = exchangeRateData.entrySet().stream()
                                                      .filter( s -> s.getKey().isEqual( fromDttm ) || s.getKey().isAfter( fromDttm ) )
                                                      .filter( s -> s.getKey().isEqual( toDttm ) || s.getKey().isBefore( toDttm ) )
                                                      .map( s -> s.getValue() )
                                                      .flatMap( Collection::stream )
                                                      .filter( s -> s.getRate() != null )
                                                      .filter( s -> s.getCurrencyCode().equals( currencyCode ) )
                                                      .map( s -> s.getRate() )
                                                      .map( bd -> new BigDecimal[]{ bd, BigDecimal.ONE } )
                                                      .reduce( ( a, b ) -> new BigDecimal[]{ a[ 0 ].add( b[ 0 ] ), a[ 1 ].add( BigDecimal.ONE ) } )
                                                      .get();
        return totalWithCount[ 0 ].divide( totalWithCount[ 1 ], new MathContext( 6 ) ).toString();
    }

    public static LocalDate getDate( String dateStr )
    {
        LocalDate date;

        if ( DateValidator.isValid( dateStr, dateFormat ) )
            date = LocalDate.parse( dateStr, dateFormat );
        else
            throw new DateNotValidException( String.format( "The provided date %s is not in valid format. It should be in %s format", dateStr, dateIsoFormat ) );

        return date;
    }

    private Boolean isCurrencyCodeExists( String currencyCode )
    {
        if ( currencyCode == null || StringUtils.equals( currencyCode, "" ) )
            return false;

        return List.of(availableCurrencies).contains( currencyCode );

    }
}
