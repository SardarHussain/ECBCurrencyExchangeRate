import au.com.bytecode.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Model
{
   // interface Stream<T> filter(Predicate<? super T> predicate);
  //Function Stream<T> filter( Predicate<? super T> predicate);

    private class Currency
    {
        String baseCurrency;
        String country;
        BigDecimal rate;

        public Currency (String country, String rate)
        {
           this. country = country;
           if(NumberUtils.isCreatable( rate ))
                this.rate = new BigDecimal( rate );
           else
               this.rate = null;
        }
    }
    private String[] tableFields;
    private Map<LocalDate, String[]> exchangeData;
    private static Map<LocalDate, BigDecimal[]> historicalData;
    private List<Map<LocalDate, BigDecimal>> kk;

    private Map<LocalDate, Currency[]> currList;

    public Model()
    {
        exchangeData = new TreeMap<>();

    }



    public Map<LocalDate, String[]> getExchangeData()
    {
       // currList.entrySet().stream().filter( a->a.getValue().equals( "" ) ).filter(  )
        return exchangeData;
    }

    public String[] getTableFields()
    {
        return tableFields;
    }
    //public static Map<LocalDate, BigDecimal[]> mapRows(String[] row)// Function<V, R> valueMapper) {
    public static  BigDecimal[] mapRows(String[] row)// Function<V, R> valueMapper) {

    {
        LocalDate date = LocalDate.parse( row[0]);
        BigDecimal[] exchangeRateRow = new BigDecimal[row.length];

        int fieldIndex=0;
        for ( String s:row)
        {
            if(NumberUtils.isCreatable( s ))
                exchangeRateRow[fieldIndex++]=( BigDecimal.valueOf( Double.valueOf( s ) ) );
            else
                exchangeRateRow[fieldIndex++] = null;

        }
        //historicalData.put( date, exchangeRateRow );
        return exchangeRateRow;
       /*
        return row.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey,
                                 e -> valueMapper.apply(e.getValue()),
                                 (a, b) -> { throw new AssertionError("Not expecting duplicate keys"); },
                                 TreeMap::new));

        */
    }
    public LocalDate getDate(String[] row)
    {
        return LocalDate.parse( row[0] );
    }
    public static <K, V, R> TreeMap<K, R> mapValues(Map<K, V> map, Function<V, R> valueMapper) {
        return map.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey,
                                 e -> valueMapper.apply(e.getValue()),
                                 (a, b) -> { throw new AssertionError("Not expecting duplicate keys"); },
                                 TreeMap::new));
    }

    public Map<LocalDate, BigDecimal[]> populateExchangeRateTable( ) throws IOException
    {
        CSVReader reader = new CSVReader( new FileReader( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" ), ',' );
        try
        {
            tableFields = reader.readNext();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return null;
    }

        //Set<String> set = people.stream().map(Person::getName).collect(Collectors.toCollection(TreeSet::new));
        /*
HashMap<LocalDate,String> map = new HashMap<>();
        Stream<String> lines = Files.lines( Paths.get("filePath""));
        Map<String, String> resultMap =
                lines.map(line -> line.split(","))
                     .collect(Collectors.toMap(line -> line[0], line -> line[1]));

        reader.readAll().s.map( s->s[0] ).collect( Collectors.toMap(line->lin))
           Map<LocalDate, BigDecimal[]> list= reader.readAll().stream().filter( a->a.length==tableFields.length )
                                                    .collect( Collectors.toMap( line->getDate( line ),line->mapRows( line ) ) );
                                                    //.map(Model::mapRows).sorted()
                                                    //.collect( Collectors.groupingBy(HashMap<LocalDate,BigDecimal[]>::keySet ));
                                                    .collect(Collectors.toMap(line -> line[0], line -> line[1]));

        .collect(  Collectors.groupingBy(  ))TreeMap::new);l-> historicalData.put( l ) ) ).collect( Collectors..filter( a-> a.entrySet() != null ).collect( Collectors.toMap(  ) )).collect( TreeMap::Map ).unordered().collect( Collectors.toMap(  )); ) ).reduce( a->a )

       // historicalData.get("").stream().filter( a->a[0].equals( "" ) ).

         */




    public void populateExchangeRateTable( String path )
    {
        try
        {
            CSVReader reader = new CSVReader( new FileReader( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" ), ',' );
            tableFields = reader.readNext();

            for ( String[] row : reader.readAll() )
            {
                if(row.length==tableFields.length)
                {
                    LocalDate date = LocalDate.parse( row[ 0 ] );
                    exchangeData.put( date, Arrays.copyOfRange( row, 1, row.length + 1 ) );
                }
                else
                {
                    System.out.println(" The data on   "+  row[0] +" is not valid");
                }

            }

        }
        catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
    /*

    public Map<String, String[]> getExchangeRate() throws FileNotFoundException, IOException, CsvValidationException
    {
        //CSVReader reader = new CSVReader(new FileReader("C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv"), ',' , '"' , 0);

        CSVReader reader = new CSVReader( new FileReader( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" ), ',' );

        Map<String, String> values = new TreeMap<>();
        CSVReaderHeaderAware readerHeaderAware = new CSVReaderHeaderAware( new FileReader( "C:\\Users\\sardarh\\Desktop\\Data\\sardar\\Formedix\\eurofxref-hist\\eurofxref-hist.csv" ) );
        //CSVReaderHeaderAware reader1 = new CSVReaderHeaderAware( new FileReader( "stats.csv" ) );
        System.out.println( readerHeaderAware.getRecordsRead() );
        System.out.println( readerHeaderAware.getLinesRead() );
        System.out.println( readerHeaderAware.iterator().hasNext() );

        for ( String[] k : readerHeaderAware )
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

    public String[] getExchangeRate( List<String[]> data, LocalDate frmDttm, LocalDate toDttm )
    {
        Map<String, String> map = new TreeMap<>();
        for ( Map.Entry<String, String> entry : map.entrySet() )
        {
            System.out.println( entry.getKey() + "/" + entry.getValue() );
        }
        String curr = "USD";
        Map<LocalDate, BigDecimal[]> currData = new TreeMap<>();
        BigDecimal[] kk = new BigDecimal[ 9 ];
        currData.put( LocalDate.parse( "2022-02-02" ), new BigDecimal[]{ null, null, new BigDecimal( 0 ) } );
        BigDecimal n = currData.get( "" )[ 9 ];

        BigDecimal[] ls = currData.get( "" );

        for ( Map.Entry<LocalDate, BigDecimal[]> dt : currData.entrySet() )
        {
            if (
                    ( dt.getKey().isEqual( frmDttm ) || dt.getKey().isAfter( frmDttm ) ) &&
                    ( dt.getKey().isEqual( toDttm ) || dt.getKey().isBefore( toDttm ) ) )
            {
                BigDecimal d = dt.getValue()[ 8 ];

            }
        }

        List<List<String>> llstring = new ArrayList<>();
        llstring.stream().filter( d -> LocalDate.parse( d.get( 0 ) ).isEqual( frmDttm ) );
        List<LocalDate> datesList = frmDttm.datesUntil( toDttm ).collect( Collectors.toList() );
        List<String[]> dataToReturn = new ArrayList<>();

        for ( String[] record : data )
        {
            LocalDate d = LocalDate.parse( record[ 0 ] );
            if ( ( d.isEqual( frmDttm ) || d.isAfter( frmDttm ) ) &&
                 ( d.isEqual( toDttm ) || d.isBefore( toDttm ) ) )

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
        //return null;
    //}


}
