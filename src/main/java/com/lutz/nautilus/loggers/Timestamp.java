package main.java.com.lutz.nautilus.loggers;

public class Timestamp {

	private int hour, minute, second, day, month, year;

	public Timestamp(int hour, int minute, int second, int day, int month,
			int year) {

		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public int getHour() {

		return hour;
	}

	public int getMinute() {

		return minute;
	}

	public int getSecond() {

		return second;
	}

	public int getDay() {

		return day;
	}

	public int getCalendarMonthValue() {

		return month;
	}
	
	public int getReadableMonth(){
		
		return month+1;
	}

	public int getYear() {

		return year;
	}

	public static String addZeros(int value, int prefLength) {

		String valueStr = Integer.toString(value);

		while (valueStr.length() < prefLength) {

			valueStr = "0" + valueStr;
		}

		return valueStr;
	}

	@Override
	public String toString() {

		return getYear() + "-" + addZeros(getReadableMonth(), 2) + "-"
				+ addZeros(getDay(), 2) + " " + getHour() + ":"
				+ addZeros(getMinute(), 2) + ":" + addZeros(getSecond(), 2);
	}
}
