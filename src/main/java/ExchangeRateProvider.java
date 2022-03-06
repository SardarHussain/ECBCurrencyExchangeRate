public class ExchangeRateProvider
{
    //Main Method here
/*
    public CustomExchangeRateProvider() {
        super(ProviderContext.of("GRAND"));
    }

    @Override
    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
        CurrencyUnit baseCurrency = conversionQuery.getBaseCurrency();
        CurrencyUnit currency = conversionQuery.getCurrency();
        CurrencyExchangeService currencyExchangeService = new GrandtrunkCurrencyExchangeService();
        double rate = currencyExchangeService.getCurrentExchangeRate(baseCurrency.getCurrencyCode(),
                                                                     currency.getCurrencyCode());
        return new ExchangeRateBuilder(getContext().getProviderName(), RateType.ANY)
                .setBase(baseCurrency)
                .setTerm(conversionQuery.getCurrency())
                .setFactor(DefaultNumberValue.of(rate))
                .build();

 */
}
