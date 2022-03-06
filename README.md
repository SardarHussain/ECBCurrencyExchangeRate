# ECBCurrencyExchangeRate
This is a simpel program that reads the ECB historical forex exchange csv data in memory collection.
A java class (ExchangeRateServiceImpl.java) is implemented that provides the following functionalities using a functional style retrieval from the collection.

1. It allows to retrieve the reference rate data for a given Date for all
available Currencies.
2. Given a Date, source Currency (eg. JPY), target Currency (eg. GBP), and an
Amount, returns the Amount given converted from the first to the second Currency as
it would have been on that Date (assuming zero fees).
3. Given a start Date, an end Date and a Currency, return the highest reference
exchange rate that the Currency achieved for the period.
4. Given a start Date, an end Date and a Currency, it determines and returns the average
reference exchange rate of that Currency for the period.

There main driver class is ForexExchangeRateMain.java
The example csv file is added to the resources folder in the project. 
For Unit testing another csv file is added to the resources folder in the test folder.

