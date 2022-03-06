import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Service
{
    Model model = new Model();


    public String getExchangeRateForCurrency (String currency, LocalDate date)
    {
        int currencyIndex=-1;
        for (int i=1;i<model.getTableFields().length;i++)
        {
            if(model.getTableFields()[i].equals( currency ))
            {
                currencyIndex = i;
                break;
            }
        }

        if(currencyIndex!= -1)
        {
          return  model.getExchangeData().get( date )[currencyIndex];
        }

        return null;
       // Arrays.stream( model.getExchangeData().get( date ) ).filter( a->a )
    }

    public String[] getExchangeRateForDate (LocalDate date)
    {
        return model.getExchangeData().get( date );
    }

    public String[] getExchangeRateInDateRange(LocalDate frmDtt, LocalDate toDtt, String currency)
    {
        model.getExchangeData().entrySet().stream().
                filter( a->a.getKey().isEqual( frmDtt ) ||a.getKey().isAfter( frmDtt ) ).
                     filter( a->a.getKey().isEqual( toDtt ) ||a.getKey().isBefore( toDtt ) ).collect( Collectors.toList());
             return null;
    }
}
