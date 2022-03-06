package model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRate
{
    private LocalDate date;
    private String currencyCode;
    private BigDecimal rate;


    public void setDate( LocalDate date )
    {
        this.date = date;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public BigDecimal getRate()
    {
        return rate;
    }

    public String getCurrencyCode()
    {
        return currencyCode;
    }

    public void setRate( BigDecimal rate )
    {
        this.rate = rate;
    }

    public void setCurrencyCode( String currencyCode )
    {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
            return true;

        if ( !( o instanceof ExchangeRate ) )
            return false;

        ExchangeRate that = (ExchangeRate)o;

        return new EqualsBuilder()
                .append( getDate(), that.getDate() )
                .append( getCurrencyCode(), that.getCurrencyCode() )
                .append( getRate(), that.getRate() )
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder( 17, 37 )
                .append( getDate() )
                .append( getCurrencyCode() )
                .append( getRate() )
                .toHashCode();
    }
}
