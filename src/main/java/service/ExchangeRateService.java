package service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.Map;
import exception.CurrencyExchangeRateException;

/**
 * This is the interface for service that provides currency conversion rates
 */
public interface ExchangeRateService
{
    /**
     * Given a Date, source Currency (eg. JPY), target Currency (eg. GBP), and an
     * Amount, it returns the Amount given converted from the first to the second Currency as
     * it would have been on that Date (assuming zero fees)
     *
     * @param sourceCurrencyCode 3-letter code for the source currency
     * @param targetCurrencyCode 3-letter code for the target currency
     * @param sourceAmount       the amount of source currency to convert to target currency
     * @param date               the target currency amount that would have been on this Date
     * @return the exchange rate for that day for the target currency
     * @throws CurrencyExchangeRateException if anything goes wrong
     */

    String getExchangeRate( String sourceCurrencyCode, String targetCurrencyCode, String sourceAmount, String date ) throws CurrencyExchangeRateException;

    /**
     * Get the reference rate data for a given Date for all available currencies
     *
     * @param date the date currencies exchange rate is available for
     * @return the exchange rate for all the available currencies
     * @throws CurrencyExchangeRateException if anything goes wrong
     */
    Map<String, String> getExchangeRate( String date ) throws CurrencyExchangeRateException;

    /**
     * Given a start Date, an end Date and a Currency, it returns the highest reference
     * exchange rate that the Currency achieved for the period.
     *
     * @param fromDate     start date in the period, a given currency exchange rate is available for
     * @param toDate       end date in the period, a given currency exchange rate is available for
     * @param currencyCode 3-letter code for the source currency
     * @return the highest reference exchange rate the given currency has achieved
     * @throws CurrencyExchangeRateException if anything goes wrong
     */

    Map<String, Map<String, String>> getHighestExchangeRate( String fromDate, String toDate, String currencyCode ) throws CurrencyExchangeRateException;

    /**
     * Given a start Date, an end Date and a Currency, it determines and returns the average
     * reference exchange rate of that Currency for the period.
     *
     * @param fromDate     start date in the period, a given currency exchange rate is available for
     * @param toDate       end date in the period, a given currency exchange rate is available for
     * @param currencyCode 3-letter code for the source currency
     * @return the average reference exchange rate  the given currency has achieved
     * @throws CurrencyExchangeRateException if anything goes wrong
     */

    String getAverageExchangeRate( String fromDate, String toDate, String currencyCode ) throws CurrencyExchangeRateException;
}
    /**
     * Get historical exchange rate
     * @param fromCurrency the source currency
     * @param toCurrency the target currency
     * @param date the date
     * @return the exchange rate
     * @throws ExchangeRateException if anything goes wrong
     */
    /*
    default double getExchangeRate( Currency fromCurrency, Currency toCurrency, LocalDate date) throws ExchangeRateException {
        return getExchangeRate(fromCurrency.getCurrencyCode(), toCurrency.getCurrencyCode(), date);
    }

    /**
     * Get current exchange rate
     * @param fromCurrency the source currency
     * @param toCurrency the target currency
     * @return the exchange rate
     * @throws ExchangeRateException if anything goes wrong
     */
    /*
    default double getCurrentExchangeRate(Currency fromCurrency, Currency toCurrency) throws ExchangeRateException {
        return getCurrentExchangeRate(fromCurrency.getCurrencyCode(), toCurrency.getCurrencyCode());
    }
    */
