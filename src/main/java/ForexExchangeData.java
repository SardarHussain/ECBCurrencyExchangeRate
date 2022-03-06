import au.com.bytecode.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.math.NumberUtils;
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



//import java.util.stream.Stream;

public class ForexExchangeData
{
    List<String[]> data;
    class DateCountry
    {
        Date date;
        BigDecimal USD;
        BigDecimal JPY;

    }
/*
    CSVReader reader =
    String [] CountryCurrency = new String{Ope
    "USD",	"JPY","BGN",	"CYP",	"CZK",	"DKK",	"EEK",	"GBP",	"HUF",	"LTL",	"LVL",	"MTL"
    ,"PLN", "ROL",	"RON",	"SEK",	"SIT",	"SKK",	"CHF",	"ISK",	"NOK",	"HRK",	"RUB",	"TRL",
            "TRY",	"AUD",	"BRL",	"CAD"	CNY	HKD	IDR	ILS	INR	KRW	MXN	MYR	NZD	PHP	SGD	THB	"ZAR"}

 */
    public static void main (String [] args) throws IOException, CsvValidationException
    {
        ForexExchangeData fed = new ForexExchangeData();
       // fed.getExchangeRate();

        ////////////////////////////////


        ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" );

        System.out.println( exchangeRateService.getExchangeRate(  "1900-02-18" ));

        System.out.println(exchangeRateService.getExchangeRate("USD","GBP","100","2022-02-18"));
      // System.out.println( exchangeRateService.getExchangeRate(  "2022-02-18" ));
       System.out.println(exchangeRateService.getAverageExchangeRate( "2022-01-01","2022-02-18","USD" ));
      // System.out.println(exchangeRateService.getHighestExchangeRate( "01-01-2022", "2022-02-18", "USD" ));
        System.out.println(exchangeRateService.getHighestExchangeRate( "2022-01-01", "2022-02-18", "USD" ));


        System.out.println( exchangeRateService.getExchangeRate(  "2002-02-18" ));
        System.out.println( exchangeRateService.getExchangeRate(  "2009-02-18" ));
        System.out.println( exchangeRateService.getExchangeRate(  "2000-02-18" ));
        System.out.println( exchangeRateService.getExchangeRate(  "1900-02-18" ));



        //////////////////////////////

        Model model = new Model();
        model.populateExchangeRateTable( "" );
        System.out.println(Arrays.asList( model.getTableFields()));
        for (Map.Entry<LocalDate, String[]> entry: model.getExchangeData().entrySet())
        {
            System.out.println( entry.getKey() +" : "+ Arrays.asList( entry.getValue() ));
        }

    }

    public Map<String, String[]> getExchangeRate() throws FileNotFoundException, IOException, CsvValidationException
    {
        //CSVReader reader = new CSVReader(new FileReader("C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv"), ',' , '"' , 0);

        CSVReader reader = new CSVReader( new FileReader( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" ), ',' );

        Map<String, String> values = new TreeMap<>();
        CSVReaderHeaderAware readerHeaderAware = new CSVReaderHeaderAware( new FileReader( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" ) );
        //CSVReaderHeaderAware reader1 = new CSVReaderHeaderAware( new FileReader( "stats.csv" ) );
System.out.println(readerHeaderAware.getRecordsRead());
System.out.println(readerHeaderAware.getLinesRead());
        System.out.println(readerHeaderAware.iterator().hasNext());

        for (String[] k:readerHeaderAware)
        {
            System.out.println( Arrays.asList( k ) );
        }


        for ( Map.Entry<String, String> entry : readerHeaderAware.readMap().entrySet() )
        {
            values.put( entry.getKey(), entry.getValue() );
        }

        for ( int i = 0; i < 100; i++ )
        {
            Map<String, String> values2 = readerHeaderAware.readMap();
            for ( Map.Entry entry : values2.entrySet() )
            {
                System.out.println( entry.getKey() + "  :" + entry.getValue() );
            }

            data = reader.readAll();
            String a = "N/A";
            BigDecimal p;
            if ( isNumeric( a ) )
                p = new BigDecimal( a );
            else
                p = null;
            System.out.println( p );

            System.out.println( Arrays.asList( data.get( data.size() - 1 ) ) );
            System.out.print( Arrays.asList( data.subList( data.size() - 1, data.size() ).get( 0 ) ) );
            BigDecimal b = NumberUtils.isParsable( a ) ? new BigDecimal( a ) : null;
            System.out.print( b );

            String dateInString = "7-Jun-2013";
            String dateInString2 = "2022-02-17";

            // LocalDate date = LocalDate.parse(dateInString2, DateTimeFormatter.BASIC_ISO_DATE);
            // String s2=date.toString();

            LocalDate localDate = LocalDate.parse( "2021-12-02" );

            System.out.print( Arrays.asList( getAllResults( data.subList( 1, data.size() ), localDate ) ) );
            String[] record = data.subList( 1, data.size() ).stream().
                    filter( d -> LocalDate.parse( d[ 0 ] ).
                            isEqual( localDate ) ).findAny().get();
            // date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.print( record );

            //data.get( 0 ).toString().split(  )
            for ( String[] d : data.subList( 1, data.size() ) )
            {
                System.out.println( Arrays.toString( d ) );
            }
            //Read CSV line by line and use the string array as you want
            String[] nextLine;
            Date dt = new Date();

            ///////////////////////data.stream().allMatch( d->>"" )
            while ( ( nextLine = reader.readNext() ) != null )
            {
                if ( nextLine != null )
                {
                    //Verifying the read data here
                    System.out.println( Arrays.toString( nextLine ) );
                }
            }
            return null;
        }
        return null;
    }

    public String[] getExchangeRate (List<String[]> data, LocalDate frmDttm, LocalDate toDttm)
    {
        Map<String, String> map = new TreeMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
        }
        String curr = "USD";
        Map <LocalDate, BigDecimal[]> currData= new TreeMap<>();
        BigDecimal [] kk = new BigDecimal[9];
currData.put( LocalDate.parse( "2022-02-02" ),new BigDecimal[] {null, null, new BigDecimal( 0 )});
BigDecimal n = currData.get( "" )[9];

BigDecimal[] ls = currData.get( "" );

        for (Map.Entry<LocalDate, BigDecimal[]>  dt:currData.entrySet())
        {
            if (
            (dt.getKey().isEqual( frmDttm ) || dt.getKey().isAfter( frmDttm )) &&
                                              (dt.getKey().isEqual( toDttm ) || dt.getKey().isBefore( toDttm ) ) )
            {
                BigDecimal d= dt.getValue()[8];

            }
        }

        List<List<String>> llstring = new ArrayList<>();
        llstring.stream().filter( d->LocalDate.parse(d.get( 0 )).isEqual( frmDttm ));
        List<LocalDate> datesList = frmDttm.datesUntil( toDttm ).collect( Collectors.toList());
        List<String []> dataToReturn= new ArrayList<>();


        for(String [] record: data)
        {
            LocalDate d =LocalDate.parse( record[0]);
            if((d.isEqual( frmDttm  ) || d.isAfter( frmDttm )) &&
               (d.isEqual( toDttm )|| d.isBefore( toDttm )))

                dataToReturn.add( record );
        }
        /*
         data.stream().filter( d->LocalDate.parse( d[0]).isEqual( frmDttm ) || LocalDate.parse( d[0] ).isAfter( frmDttm )  )
            .filter( d->LocalDate.parse( d[0]).isEqual( toDttm )||LocalDate.parse( d[0] ).isBefore( toDttm ) )
             .forEach(  ); )
                 .collect( Collectors.toList())
                .stream().filter( d );


        data.stream().filter( d->data.get(  )LocalDate.parse( d[0]) )
    LocalDate.parse( d[0] ).datesUntil( toDttm ).isEqual( frmDttm )|| )
        if(data.stream().findFirst().
        List <LocalDate> listofDates= data.stream().filter( d->LocalDate.parse( d[0]).isEqual( frmDttm ) ).flatMapToInt( d->d )
        data.stream().filter( d->LocalDate.parse(d[0]).isEqual(frmDttm  )).
        for (String[] d: data){
            LocalDate date = LocalDate.parse( d[0] );frmDttm
            if(date.datesUntil( toDttm ).filter(  ))
        }
        data.stream().filter( d->)
            */
        return null;
    }

    public String[] getAllResults (List<String[]> data, LocalDate date)
    {
        // using a custom pattern (01/04/2014)
        //String asCustomPattern = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // using a custom pattern (01/04/2014)
       // String asCustomPattern = date.format( DateTimeFormatter.ofPattern( "dd/MM/yyyy"));
       // DateFormat dateFormat = DateFormat
       // date.format(  )
        for(String[] record : data)
        {
            LocalDate key = LocalDate.parse( record[0]);
            if(key.isEqual( date ))
                return record;
        }
        return null;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

    /*
    public Map<Date, String[]> getDateCurrencyValues()
    {
        Date d = new Date("");
        Map<Date, String[]> dateCurrencyValues = new HashMap<Date, String[]>();
        dateCurrencyValues.put(d, new String[10]);
        String[] array = {"a", "b", "c", "d", "e"};
BigDecimal bg = new BigDecimal( "" );
BigDecimal.
        String [] p =dateCurrencyValues.get( "" );
Stream <String> k =Stream.of( p );
        final Stream<String> stringStream = k.filter( a -> BigDecimal.valueOf( a ) );
        Stream<String> stream1 = Arrays.stream( p);
stream1.filter( a-> a)
        return dateCurrencyValues;
    }

     */



