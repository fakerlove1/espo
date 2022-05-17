package com.stackfarm.esports.config;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author croton
 * @create 2021/3/31 20:39
 */
public class ESportsDateFormat extends DateFormat implements Serializable {

    private static final long serialVersionUID = 1L;

    private final DateFormat dateFormat;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    public ESportsDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return dateFormat.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        Date date = simpleDateFormat.parse(source, pos);
        return date;
    }

    @Override
    public Object clone() {
        super.clone();
        DateFormat dateFormat = (DateFormat) this.dateFormat.clone();
        return new ESportsDateFormat(dateFormat);
    }
}
