package com.jdk.jdk.Date;

/**
 * Created by tbu on 7/17/2014.
 */

import org.apache.commons.validator.routines.DateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtils {

    // Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);


    /**
     *  Creates an instance of  <code>java.util.Date</code>  with the given
     *  year / month / date information in the default time zone with the
     *  default locale.
     *
     *  <p>
     *  This method uses  <code>java.util.GregorianCalendar</code>  to
     *  convert the calendar information to the  <code>Date</code>  object.
     *  </p>
     *
     * @param   year    the value used to set the YEAR time field in the
     *                  calendar.
     * @param   month   the value used to set the MONTH time field in the
     *                  calendar. Month value is 0-based. e.g., 0 for
     *                  January.
     * @param   date    the value used to set the DATE time field in the
     *                  calendar.
     * @return  the instance of  <code>java.util.Date</code>  with the given
     *          year / month / date information in the default time zone
     *          with the default locale.
     */
    public static Date createDate(int year, int month, int date) {

        // No argument validation for now
        return  (new GregorianCalendar(year, month, date)).getTime();
    }


    /**
     *  Creates an instance of  <code>java.util.Date</code>  with the given
     *  year / month / date information in the default time zone with the
     *  default locale.
     *
     *  <p>
     *  This method is a utility method for parsing String arguments before
     *  using  {@link #createDate(int, int, int)}  to create the actual
     *  <code>Date</code>  object.  If any one of the String argument is
     *  <code>null</code>  or blank, a null pointer will be returned
     *  without throwing exceptions.
     *  </p>
     *
     *  <p>
     *  This method uses 1-based month so that it work conveniently with
     *  user interface.  e.g., 1 for January.
     *  </p>
     *
     * @param   yearString  the String representation of the integer value
     *                      used to set the YEAR time field in the calendar.
     * @param   monthString the String representation of the integer value
     *                      used to set the MONTH time field in the
     *                      calendar.  <b>Month value is 1-based.</b>
     *                      e.g., 1 for January.
     * @param   dateString  the String representation of the integer value
     *                      used to set the DATE time field in the calendar.
     * @return  the instance of  <code>java.util.Date</code>  with the given
     *          year / month / date information in the default time zone
     *          with the default locale, or  <code>null</code>  if any
     *          one of the method argument is  <code>null</code>  or blank.
     */
    public static Date createDate(String yearString, String monthString,
                                  String dateString) {

        if ((yearString == null) || (yearString.length() < 1)
                || (monthString == null) || (monthString.length() < 1)
                || (dateString == null) || (dateString.length() < 1)) {

            return  null;

        } else {

            return  createDate(Integer.parseInt(yearString),
                    Integer.parseInt(monthString) - 1,
                    Integer.parseInt(dateString));
        }
    }


    /**
     *  Creates an instance of  <code>java.util.Date</code>  with the given
     *  year / month / date information in the default time zone with the
     *  default locale.
     *
     * @param   yyyyMMddString  the String representation of the date in
     *                          yyyyMMdd format.
     * @return  the instance of  <code>java.util.Date</code>  with the given
     *          year / month / date information in the default time zone
     *          with the default locale, or  <code>null</code>  if there are
     *          errors parsing the input String.
     */
    public static Date createDate(String yyyyMMddString) {

        try {

            return  (new SimpleDateFormat("yyyyMMdd")).parse(yyyyMMddString);

        } catch (ParseException pex) {

            LOGGER.error("Unable to parse input date String  "
                    + yyyyMMddString + "  as  yyyyMMdd", pex);
            return  null;
        }
    }


    /**
     * Get Current dateTime in any format
     *  Letter  Date or Time Component  Presentation		Examples
     *  G	Era designator		Text			AD
     *  y	Year			Year			1996; 96
     *  M	Month in year		Month			July; Jul; 07
     *  w	Week in year		Number			27
     *  W	Week in month		Number			2
     *  D    Day in year		Number			189
     *  d	Day in month		Number			10
     *  F	Day of week in month	Number			2
     *  E	Day in week		Text			Tuesday; Tue
     *  a	Am/pm marker		Text			PM
     *  H	Hour in day (0-23)	Number			0
     *  k	Hour in day (1-24)	Number			24
     *  K	Hour in am/pm (0-11)	Number			0
     *  h	Hour in am/pm (1-12)	Number			12
     *  m	Minute in hour		Number			30
     *  s	Second in minute	Number			55
     *  S	Millisecond		Number			978
     *  z	Time zone		General time zone	Pacific Standard Time; PST; GMT-08:00
     *  Z	Time zone		RFC 822 time zone	-0800
     **/

    public static String dateTime(String outputFormat)
    {
        Date now = new Date();  //current Date/Time
        SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);
        return formatter.format(now);
    }

    public static String dateTime(Calendar cal, String outputFormat)
    {
        return dateTime(cal.getTime(), outputFormat);
    }

    public static String dateTime(Date dt, String outputFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);
        return formatter.format(dt);
    }

    public static String dateTime(String inputDateTime, String inputFormat, String outputFormat)
    {

        if(inputDateTime == null) return null;
        if(inputDateTime.length() == 0) return "";
        SimpleDateFormat formatter = new SimpleDateFormat(outputFormat);
        String outputDateTime=inputDateTime;
        try
        {
            DateFormat dateFormatter = new SimpleDateFormat(inputFormat);
            Date givenDateTime = (Date) dateFormatter.parse(inputDateTime.trim());
            outputDateTime= formatter.format(givenDateTime);
        }
        catch (ParseException ex)
        {
            LOGGER.error("Parse Errors! Wrong inputformat is a possible cause.", ex);
        }
        return outputDateTime;
    }

    // all handy methods
    // convert yyyy-mm-dd hh:mm:ss to mmddyyyy
    public static String MysqlDateToMMDDYYYY(String dt)
    {
        return dateTime(dt, "yyyy-MM-dd HH:mm:ss", "MMddyyyy");
    }
    // convert from YYYYMMDD to YYYY-MM-DD + start/endinf time
    public static String MysqlYYYYMMDDandTime(String s, boolean begin)
    {
        if(begin)
            return dateTime(s, "yyyyMMdd", "yyyy-MM-dd") + " 00:00:00";
        else
            return dateTime(s, "yyyyMMdd", "yyyy-MM-dd") + " 23:59:59";
    }

    public static String MysqlYYYY_MM_DDandTime(String s, boolean begin)
    {
        if(begin)
            return s + " 00:00:00";
        else
            return s + " 23:59:59";
    }

    public static String MysqlDateToYYYYMMDD(String dt)
    {
        return dateTime(dt, "yyyy-MM-dd HH:mm:ss", "yyyyMMdd");
    }
    public static String MysqlDateToMM_DD_YY(String dt)
    {
        return dateTime(dt, "yyyy-MM-dd", "MM-dd-yy");
    }

    public static String todayInYYYY_MM_DD()
    {
        return dateTime("yyyy-MM-dd");
    }

    public static String todayInMM_DD_YYYY()
    {
        return dateTime("MM-dd-yyyy");
    }

    public static String todayInYYYYMMDDHHMMSS()
    {
        return dateTime("yyyyMMddHHmmss");
    }

    public static String todayInYYMMDDHHMMSSS()
    {
        return dateTime("yyMMddHHmmssS");
    }

    public static String todayInYYYY_MM_DD_HH_MM_SS()
    {
        return dateTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String todayInYYYYMMDD()
    {
        return dateTime("yyyyMMdd");
    }

    public static String calendarToYYYYMMDDHHMMSS(Calendar cal)
    {
        if(cal == null) return "";
        return dateTime(cal, "yyyyMMddHHmmss");
    }

    public static String calendarToYYYYMMDD(Calendar cal)
    {
        if(cal == null) return "";
        return dateTime(cal, "yyyyMMdd");
    }

    public static Calendar YYYYMMDDHHMMSStoCalendar(String s)
    {
        Calendar cal = Calendar.getInstance();

        try
        {
            DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date givenDateTime = (Date) dateFormatter.parse(s);
            cal.setTime(givenDateTime);
        }
        catch (ParseException ex)
        {
            System.out.println("Parse Errors! Wrong inputformat is a possible cause.");
        }
        return cal;
    }
    /**
     *
     * @param inpStr
     * @return Object[]
     **/
    //TODO need to put all the data formats here. If you encounter a new one please put it in here.
    //"yyyy-MM-dd HH:mm:ss.s", is a date format added to support 4.75 with vendorportal 1.0
    public static Date parseDate( String inpStr )
    {
        String patterns []  = { "MM/dd/yy",
                "MM-dd-yy",
                "MM.dd.yy",
                "MMddyy",
                "MM/dd/yyyy",
                "MM-dd-yyyy",
                "MM.dd.yyyy",
                "yyyy-MM-dd",
                "MMddyyyy",
                "yyyy/MM/dd",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss.s",
                "yyyyMMddHHmmss"
        };

        Date retVal    = null;

        DateValidator dateValidator = DateValidator.getInstance();
        Date dt					  = null;

        for( int patternsCnt = 0; patternsCnt < patterns.length ; patternsCnt++ ){
            dt = dateValidator.validate( inpStr, patterns[ patternsCnt ]);

            if( dt != null ){
                retVal = dt;
                break;
            }
        }//end of for loop

        return retVal;
    }

    public static String MysqlDateToYYYY_MM_DD(String dt)
    {
        return dateTime(dt, "yyyyMMdd","yyyy-MM-dd" );
    }

    public static void main(String args[])
    {
          System.out.println(dateTime("yyyyMMdd"));
//        System.out.println(dateTime("dd MMMMM yyyy"));
//        System.out.println(dateTime("yyyyMMddHHmmss"));
//        System.out.println(dateTime("dd.MM.yy"));
//        System.out.println(dateTime("MM/dd/yyyy"));
//        System.out.println(dateTime("MM-dd-yyyy"));
//        System.out.println(dateTime("yyyy.MM.dd G 'at' hh:mm:ss z"));
//        System.out.println(dateTime("EEE, MMM d, ''yy"));
//        System.out.println(dateTime("h:mm a"));
//        System.out.println(dateTime("H:mm:ss:SSS"));
//        System.out.println(dateTime("H"));
//        System.out.println(dateTime("K:mm a,z"));
//        System.out.println(dateTime("yyyy.MMMMM.dd GGG hh:mm aaa"));
//
//        System.out.println(dateTime("2004-11-10", "yyyy-MM-dd", "MM/dd/yyyy"));
//        System.out.println(dateTime("0000-00-00", "yyyy-MM-dd", "MM/dd/yyyy"));
//        System.out.println(todayInYYYYMMDD());
//        System.out.println(todayInMM_DD_YYYY());
//        System.out.println(todayInYYYYMMDDHHMMSS());
//        System.out.println(MysqlDateToMMDDYYYY("2004-04-10 12:20:30"));
//        System.out.println(MysqlDateToYYYYMMDD("2008-10-16 10:54:34.0"));
//        System.out.println(calendarToYYYYMMDDHHMMSS(Calendar.getInstance()));
//
//        System.out.println(calendarToYYYYMMDD(Calendar.getInstance()));
//        System.out.println((YYYYMMDDHHMMSStoCalendar("20041209110423").getTime()));
//        System.out.println(MysqlYYYYMMDDandTime("20041204",  false));
//
//        System.out.println( DateUtils.parseDate("2008-04-16") );
//        System.out.println( DateUtils.parseDate("12/04/2008") );
//
//        System.out.println(DateUtils.MysqlDateToYYYY_MM_DD("20080714"));

    }


    /** from Gemini **/
    public static Date getExpiredDate(Date date ,int days){
        Date dateresult=new Date();
        try{
            DateFormat dateFormat =  DateFormat.getDateInstance(DateFormat.FULL);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.add(GregorianCalendar.DAY_OF_MONTH,days);
            dateresult = cal.getTime() ;
        }catch(Exception e){
            System.out.println("exception"+e.toString());
        }
        return dateresult;
    }
    /**
     *   return days between begin Date and end Date
     *   @param   startDate
     *   @param   endDate
     *   @return
     */
    public  static long getPeriodDayCount(String startDate, String endDate){
        String t1 = startDate.replace('-','/');
        String t2 = endDate.replace('-','/');
        long l = 0;
        try{
            Date dt1= new Date(t1);
            Date dt2= new Date(t2);
            l = dt1.getTime() - dt2.getTime();
        }catch(Exception e){
            System.out.println("AdminUtils --> getPeriodDayCount exception"+e.toString());
        }
        return   l/60/60/1000/24;
    }
}

