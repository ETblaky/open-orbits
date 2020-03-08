package me.etblaky.rockets.Utils;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.TimeZone;

public class Date {

    public int year;
    public int day_of_year;
    public int hour;
    public int minute;
    public int second;

    public int day;
    public int month;

    public Date() {
        Calendar temp = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.year = temp.get(Calendar.YEAR);
        this.day_of_year = temp.get(Calendar.DAY_OF_YEAR);
        this.hour = temp.get(Calendar.HOUR_OF_DAY);
        this.minute = temp.get(Calendar.MINUTE);
        this.second = temp.get(Calendar.SECOND);

        this.day = temp.get(Calendar.DAY_OF_MONTH);
        this.month = temp.get(Calendar.MONTH) + 1;
    }

    public Date(int year, int month, int day) {
        this.year = year;

        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.MONTH, month - 1);
        temp.set(Calendar.DAY_OF_MONTH, day);
        this.day_of_year = temp.get(Calendar.DAY_OF_YEAR);
        this.hour = temp.get(Calendar.HOUR);
        this.minute = temp.get(Calendar.MINUTE);
        this.second = temp.get(Calendar.SECOND);

        this.day = day;
        this.month = month;
    }

    public Date(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.MONTH, month - 1);
        temp.set(Calendar.DAY_OF_MONTH, day);
        this.day_of_year = temp.get(Calendar.DAY_OF_YEAR);
        this.hour = hour;
        this.minute = minute;
        this.second = second;

        this.day = day;
        this.month = month;
    }

    public Date(int year, int day_of_year, int hour, int minute, int second) {
        this.year = year;
        this.day_of_year = day_of_year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;

        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_YEAR, day_of_year);
        this.day = temp.get(Calendar.DAY_OF_MONTH);
        this.month = temp.get(Calendar.MONTH) + 1;
    }

    public Date(int year, double day_of_year_decimal) {
        this.year = year;
        Date d = decimal2Date(day_of_year_decimal);
        this.day_of_year = d.day_of_year;
        this.hour = d.hour;
        this.minute = d.minute;
        this.second = d.second;

        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_YEAR, d.day_of_year);
        this.day = temp.get(Calendar.DAY_OF_MONTH);
        this.month = temp.get(Calendar.MONTH) + 1;
    }


    public static double offset_days(Date one, Date two) {
        double one_days = one.inDays();
        double two_days = two.inDays();

        return one_days - two_days;
    }

    public static Date decimal2Date(double day_of_year_decimal) {
        String[] day_decimal = String.valueOf(day_of_year_decimal).split("\\.");
        String[] hour_decimal = String.valueOf(Double.parseDouble("0." + day_decimal[1]) * 24).split("\\.");
        String[] minute_decimal = String.valueOf(Double.valueOf("0." + hour_decimal[1]) * 60).split("\\.");

        int years = 0;
        int hours = Integer.parseInt(hour_decimal[0]);
        int minutes = Integer.parseInt(minute_decimal[0]);
        int seconds = (int) Math.floor(Double.parseDouble("0." + minute_decimal[1]) * 60);

        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_YEAR, Integer.valueOf(day_decimal[0]));
        return new Date(years, temp.get(Calendar.MONTH) + 1, temp.get(Calendar.DAY_OF_MONTH), hours, minutes, seconds);
    }

    public Date zero() {
        year = 0;
        month = 0;
        day = 0;
        hour = 0;
        minute = 0;
        second = 0;

        return this;
    }


    public boolean after(Date d) {
        return inSeconds() > d.inSeconds();
    }

    public boolean before(Date d) {
        return inSeconds() < d.inSeconds();
    }


    public double inYears() {
        return year + day_of_year / 365.0 + hour / (365.0 * 24) + minute / (365.0 * 24 * 60) + second / (365.0 * 24 * 60 * 60);
    }

    public double inDays() {
        return (year * 365.0) + (day_of_year) + (hour / 24.0) + (minute / (24.0 * 60.0)) + (second / (24.0 * 60.0 * 60.0));
    }

    public double inHours() {
        return year * 365 * 24 + day_of_year * 24 + hour + minute / 60 + second / (60 * 60);
    }

    public double inMinutes() {
        return year * 365 * 24 * 60 + day_of_year * 24 * 60 + hour * 60 + minute + second / 60;
    }

    public double inSeconds() {
        return year * 365 * 24 * 60 * 60 + day_of_year * 24 * 60 * 60 + hour * 60 * 60 + minute * 60 + second;
    }


    public void addYear(int year) {
        this.year += year;
    }

    public void addMonth(int month) {
        this.month += month;

        if (this.month > 12) {
            this.month -= 12;
            addYear(1);
        }
    }

    public void addDay(int day) {
        this.day += day;

        YearMonth yearMonthObject = YearMonth.of(1999, 2);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        if (this.day > daysInMonth) {
            this.day -= daysInMonth;
            addMonth(1);
        }
    }

    public void addHour(int hour) {
        this.hour += hour;

        if (this.hour > 23) {
            this.hour -= 23;
            addDay(1);
        }
    }

    public void addMin(int min) {
        this.minute += min;

        if (this.minute > 59) {
            this.minute -= 59;
            addHour(1);
        }
    }

    public void addSec(int sec) {
        this.second += sec;

        if (this.second > 59) {
            this.second -= 59;
            addMin(1);
        }
    }


    @Override
    public String toString() {
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.DAY_OF_YEAR, day_of_year);
        return "Year: " + year + "\n"
                + "Month: " + (temp.get(Calendar.MONTH) + 1) + "\n"
                + "Day: " + temp.get(Calendar.DAY_OF_MONTH) + "\n"
                + "Day Of The Year: " + day_of_year + "\n"
                + "Hour: " + hour + "\n"
                + "Minute: " + minute + "\n"
                + "Second: " + second + "\n";
    }
}
