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
import repository.HistoricalExchangeRateData;
import utility.DateValidator;

public class ExchangeRateServiceImpl implements ExchangeRateService
{
    //private final static String REQUEST_FAILED = "False";

    // private static final String SUPPORTED_CURRENCIES_URL = "http://currencies.apps.grandtrunk.net/currencies";

    // private static final String CURRENT_RATE_URL = "http://currencies.apps.grandtrunk.net/getlatest/%s/%s";

    // private static final String HISTORICAL_RATE_URL = "http://currencies.apps.grandtrunk.net/getrate/%s/%s/%s";

    private Map<LocalDate, List<ExchangeRate>> exchangeRateData;
    private String[] availableCurrencies;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

    public ExchangeRateServiceImpl( String filePath ) throws Exception
    {
        HistoricalExchangeRateData historicalExchangeRateData = HistoricalExchangeRateData.getInstance();

        exchangeRateData = historicalExchangeRateData.getHistoricalExchangeRateData( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" );

        availableCurrencies = historicalExchangeRateData.getTableFields();

    }

    public List<String > getAvailableCurrencies()
    {
        return Arrays.asList(availableCurrencies);
    }

/*
    public List<String> getSupportedCurrencies() throws CurrencyExchangeRateException {
        try {
            String responseAsText = WebUtil.getResponseAsString(SUPPORTED_CURRENCIES_URL);
            return Arrays.asList( responseAsText.split( "\\r?\\n"));
        } catch ( IOException e) {
            throw new CurrencyExchangeRateException(e);
        }

    }

    @Override
    public double getExchangeRate(String fromCurrencyCode, String toCurrencyCode, LocalDate date) throws CurrencyExchangeRateException {
        String url = String.format(HISTORICAL_RATE_URL, DATE_FORMAT.format(date), fromCurrencyCode, toCurrencyCode);
        return getServiceResponse(url);
    }


    @Override
    public double getCurrentExchangeRate(String fromCurrencyCode, String toCurrencyCode) throws CurrencyExchangeRateException {
        String url = String.format(CURRENT_RATE_URL, fromCurrencyCode, toCurrencyCode);
        return getServiceResponse(url);
    }


    private double getServiceResponse(String url) throws CurrencyExchangeRateException {
        String responseAsText;
        try {
            responseAsText = WebUtil.getResponseAsString(url);
        } catch (IOException e) {
            throw new CurrencyExchangeRateException(e);
        }
        if (REQUEST_FAILED.equals(responseAsText)) {
            throw new CurrencyExchangeRateException("No exchange rate found");
        }
        return Double.parseDouble(responseAsText);
    }
    */

    private BigDecimal getExchangeRateValue(String currencyCode, LocalDate date)
    {
        return  exchangeRateData.get( date ).stream()
                                .filter( s -> s.getCurrencyCode().equals( currencyCode ) )
                                .map( s -> s.getRate() )
                                .findAny().get();
    }

    @Override
    public String getExchangeRateConversion( String sourceCurrencyCode, String targetCurrencyCode, String sourceAmount, String date ) throws CurrencyExchangeRateException
    {
        BigDecimal sourceCurrExchangeRate = getExchangeRateValue( sourceCurrencyCode, getDate( date ) );
        BigDecimal targetCurrExchangeRate = getExchangeRateValue( targetCurrencyCode, getDate( date ) );


        if ( sourceCurrExchangeRate == null && !StringUtils.equals( sourceCurrencyCode, "EUR" ) )
            throw new CurrencyExchangeRateException( "the exchange rate for the " + sourceCurrencyCode + " is not available on " + date );
        if ( targetCurrExchangeRate == null && !StringUtils.equals( targetCurrencyCode, "EUR" ) )
            throw new CurrencyExchangeRateException( "the exchange rate for the " + targetCurrencyCode + " is not available on " + date );

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

        if (!isCurrencyCodeExists( currencyCode ) )
            throw new CurrencyExchangeRateException( "Exchange rate for currency code " + currencyCode + " does not exist" );

        //LocalDate fromDttm = LocalDate.parse( fromDate );
        //LocalDate toDttm = LocalDate.parse( toDate );

        Comparator<ExchangeRate> comparator = Comparator.comparing( ExchangeRate::getRate );
        //  Employee maxObject = employees.stream().max(comparator).get()
        // Optional<BigDecimal> rate=
        ExchangeRate rate = exchangeRateData.entrySet().stream()
                                            .filter( s -> s.getKey().isEqual( fromDttm ) || s.getKey().isAfter( fromDttm ) )
                                            .filter( s -> s.getKey().isEqual( toDttm ) || s.getKey().isBefore( toDttm ) )
                                            .map( s -> s.getValue() )
                                            .flatMap( Collection::stream )
                                            .filter( s -> s.getCurrencyCode().equals( currencyCode ) )
                                            .filter( s -> s.getRate() != null )
                                            .max( comparator ).get();

        return Collections.singletonMap( rate.getDate().toString(), Collections.singletonMap( rate.getCurrencyCode(), rate.getRate().toString() ) );
    }

    @Override
    public String getAverageExchangeRate( String fromDate, String toDate, String currencyCode ) throws CurrencyExchangeRateException
    {
        LocalDate fromDttm = getDate( fromDate );
        LocalDate toDttm = getDate( toDate );

        /*
        exchangeData.entrySet().stream()
                    .filter( s->s.getKey().isEqual( fromDttm ) ||s.getKey().isAfter( fromDttm ) )
                    .filter( s->s.getKey().isEqual( toDttm )||s.getKey().isBefore( toDttm ) )
                    .map( s->s.getValue() )
                    .map( s->s );
        */
        /*
        List<ExchangeRate> lb = new ArrayList<>();
        for (Map.Entry<LocalDate,List<ExchangeRate>> exRate: exchangeData.entrySet())
        {
            if((exRate.getKey().isEqual( fromDttm )  || exRate.getKey().isBefore( fromDttm ))  && exRate.getKey().isEqual( toDttm ) || exRate.getKey().isBefore( toDttm ))
                lb.add( exRate.g);
        }

         */

        List<BigDecimal> values = exchangeRateData.entrySet().stream()
                                                  .filter( s -> s.getKey().isEqual( fromDttm ) || s.getKey().isAfter( fromDttm ) )
                                                  .filter( s -> s.getKey().isEqual( toDttm ) || s.getKey().isBefore( toDttm ) )
                                                  .map( s -> s.getValue() )
                                                  .flatMap( Collection::stream )
                                                  .filter( s -> s.getCurrencyCode().equals( currencyCode ) )
                                                  .filter( s -> s.getRate() != null )
                                                  .map( s -> s.getRate() ).collect( Collectors.toList() );
        int total = values.size();
        BigDecimal sum = values.stream().reduce( BigDecimal::add ).get();
        BigDecimal average = sum.divide( new BigDecimal( total ), 6 );

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
        return totalWithCount[ 0 ].divide( totalWithCount[ 1 ], 6 ).toString();

        // BigDecimal mean = totalWithCount[0].divide(totalWithCount[1], roundingMode);

        // return average.toString();
    }

    public static LocalDate getDate( String dateStr )
    {
        LocalDate date;

      if(DateValidator.isValid( dateStr , dateFormat))
            date= LocalDate.parse( dateStr, dateFormat );
      else
          throw new DateNotValidException( "The provided date " + dateStr + " is not in valid format" );

        return date;
    }

    private Boolean isCurrencyCodeExists( String currencyCode )
    {
        if ( currencyCode == null && StringUtils.equals( currencyCode, "" ) )
            return false;
        String currCode = Stream.of( availableCurrencies ).filter( s -> StringUtils.equals( s, currencyCode ) ).findAny().get();
        if ( currencyCode.equals( currCode ) )
            return true;
        return false;

    }
}
