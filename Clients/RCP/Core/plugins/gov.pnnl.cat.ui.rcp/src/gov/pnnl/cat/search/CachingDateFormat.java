/*******************************************************************************
 * .
 *                           Velo 1.0
 *  ----------------------------------------------------------
 * 
 *            Pacific Northwest National Laboratory
 *                      Richland, WA 99352
 * 
 *                      Copyright (c) 2013
 *            Pacific Northwest National Laboratory
 *                 Battelle Memorial Institute
 * 
 *   Velo is an open-source collaborative content management
 *                and job execution environment
 *              distributed under the terms of the
 *           Educational Community License (ECL) 2.0
 *   A copy of the license is included with this distribution
 *                   in the LICENSE.TXT file
 * 
 *                        ACKNOWLEDGMENT
 *                        --------------
 * 
 * This software and its documentation were developed at Pacific 
 * Northwest National Laboratory, a multiprogram national
 * laboratory, operated for the U.S. Department of Energy by 
 * Battelle under Contract Number DE-AC05-76RL01830.
 ******************************************************************************/
package gov.pnnl.cat.search;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Provides <b>thread safe</b> means of obtaining a cached date formatter.
 * <p>
 * The cached string-date mappings are stored in a <tt>WeakHashMap</tt>.
 * 
 * @see java.text.DateFormat#setLenient(boolean)
 * 
 * @author Derek Hulley
 * @version $Revision: 1.0 $
 */
public class CachingDateFormat extends SimpleDateFormat
{
    private static final long serialVersionUID = 3258415049197565235L;

    /** <pre> yyyy-MM-dd'T'HH:mm:ss </pre> */
    public static final String FORMAT_FULL_GENERIC = "yyyy-MM-dd'T'HH:mm:ss";

    /** <pre> yyyy-MM-dd </pre> */
    public static final String FORMAT_DATE_GENERIC = "yyyy-MM-dd";

    /** <pre> HH:mm:ss </pre> */
    public static final String FORMAT_TIME_GENERIC = "HH:mm:ss";

    private static ThreadLocal<SimpleDateFormat> s_localDateFormat = new ThreadLocal<SimpleDateFormat>();

    private static ThreadLocal<SimpleDateFormat> s_localDateOnlyFormat = new ThreadLocal<SimpleDateFormat>();

    private static ThreadLocal<SimpleDateFormat> s_localTimeOnlyFormat = new ThreadLocal<SimpleDateFormat>();

    transient private Map<String, Date> cacheDates = new WeakHashMap<String, Date>(89);

    /**
     * Constructor for CachingDateFormat.
     * @param format String
     */
    private CachingDateFormat(String format)
    {
        super(format);
    }

    /**
     * Method toString.
     * @return String
     */
    public String toString()
    {
        return this.toPattern();
    }

    /**
     * @param length
     *            the type of date format, e.g. {@link CachingDateFormat#LONG }
     * @param locale
     *            the <code>Locale</code> that will be used to determine the
     *            date pattern
     * 
    
    
    
    
    
     * @param lenient boolean
     * @return SimpleDateFormat
     * @see #getDateFormat(String, boolean) * @see CachingDateFormat#SHORT * @see CachingDateFormat#MEDIUM * @see CachingDateFormat#LONG * @see CachingDateFormat#FULL */
    public static SimpleDateFormat getDateFormat(int length, Locale locale, boolean lenient)
    {
        SimpleDateFormat dateFormat = (SimpleDateFormat) CachingDateFormat.getDateInstance(length, locale);
        // extract the format string
        String pattern = dateFormat.toPattern();
        // we have a pattern to use
        return getDateFormat(pattern, lenient);
    }

    /**
     * @param dateLength
     *            the type of date format, e.g. {@link CachingDateFormat#LONG }
     * @param timeLength
     *            the type of time format, e.g. {@link CachingDateFormat#LONG }
     * @param locale
     *            the <code>Locale</code> that will be used to determine the
     *            date pattern
     * 
    
    
    
    
    
     * @param lenient boolean
     * @return SimpleDateFormat
     * @see #getDateFormat(String, boolean) * @see CachingDateFormat#SHORT * @see CachingDateFormat#MEDIUM * @see CachingDateFormat#LONG * @see CachingDateFormat#FULL */
    public static SimpleDateFormat getDateTimeFormat(int dateLength, int timeLength, Locale locale, boolean lenient)
    {
        SimpleDateFormat dateFormat = (SimpleDateFormat) CachingDateFormat.getDateTimeInstance(dateLength, timeLength, locale);
        // extract the format string
        String pattern = dateFormat.toPattern();
        // we have a pattern to use
        return getDateFormat(pattern, lenient);
    }

    /**
     * @param pattern
     *            the conversion pattern to use
     * @param lenient
     *            true to allow the parser to extract the date in conceivable
     *            manner
    
     * @return Returns a conversion-cacheing formatter for the given pattern,
     *         but the instance itself is not cached */
    public static SimpleDateFormat getDateFormat(String pattern, boolean lenient)
    {
        // create an alfrescoDateFormat for cacheing purposes
        SimpleDateFormat dateFormat = new CachingDateFormat(pattern);
        // set leniency
        dateFormat.setLenient(lenient);
        // done
        return dateFormat;
    }

    /**
    
     * 
    
     * @return Returns a thread-safe formatter for the generic date/time format * @see #FORMAT_FULL_GENERIC */
    public static SimpleDateFormat getDateFormat()
    {
        if (s_localDateFormat.get() != null)
        {
            return s_localDateFormat.get();
        }

        CachingDateFormat formatter = new CachingDateFormat(FORMAT_FULL_GENERIC);
        // it must be strict
        formatter.setLenient(false);
        // put this into the threadlocal object
        s_localDateFormat.set(formatter);
        // done
        return s_localDateFormat.get();
    }

    /**
    
     * 
    
     * @return Returns a thread-safe formatter for the generic date format * @see #FORMAT_DATE_GENERIC */
    public static SimpleDateFormat getDateOnlyFormat()
    {
        if (s_localDateOnlyFormat.get() != null)
        {
            return s_localDateOnlyFormat.get();
        }

        CachingDateFormat formatter = new CachingDateFormat(FORMAT_DATE_GENERIC);
        // it must be strict
        formatter.setLenient(false);
        // put this into the threadlocal object
        s_localDateOnlyFormat.set(formatter);
        // done
        return s_localDateOnlyFormat.get();
    }

    /**
    
     * 
    
     * @return Returns a thread-safe formatter for the generic time format * @see #FORMAT_TIME_GENERIC */
    public static SimpleDateFormat getTimeOnlyFormat()
    {
        if (s_localTimeOnlyFormat.get() != null)
        {
            return s_localTimeOnlyFormat.get();
        }

        CachingDateFormat formatter = new CachingDateFormat(FORMAT_TIME_GENERIC);
        // it must be strict
        formatter.setLenient(false);
        // put this into the threadlocal object
        s_localTimeOnlyFormat.set(formatter);
        // done
        return s_localTimeOnlyFormat.get();
    }

    /**
     * Parses and caches date strings.
     * 
     * @see java.text.DateFormat#parse(java.lang.String,
     *      java.text.ParsePosition)
     */
    public Date parse(String text, ParsePosition pos)
    {
        Date cached = cacheDates.get(text);
        if (cached == null)
        {
            Date date = super.parse(text, pos);
            if ((date != null) && (pos.getIndex() == text.length()))
            {
                cacheDates.put(text, date);
                Date clonedDate = (Date) date.clone();
                return clonedDate;
            }
            else
            {
                return date;
            }
        }
        else
        {
            pos.setIndex(text.length());
            Date clonedDate = (Date) cached.clone();
            return clonedDate;
        }
    }
}
