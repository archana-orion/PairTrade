package orion.utility;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static Date addWorkingDays(Date openTime, int numberOfDays) {
		// Create Calendar class instance
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(openTime);

		for (int i = 1; i <= numberOfDays; i++) {
			// Adding 1 day to calendar.
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			/*
			 * calendar.get(Calendar.DAY_OF_WEEK) = 1 (Sunday)
			 * calendar.get(Calendar.DAY_OF_WEEK) = 7 (Saturday)
			 * 
			 * If day is either Sunday or Saturday then it is non working day so Increasing
			 * the limit to compensate working days.
			 */
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
				numberOfDays++;
			}
		}
	//	calendar.set(Calendar.HOUR_OF_DAY, 16);

		return calendar.getTime();
	}
	
	public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

	public static Date setMarketClosingTime(Date time) {
		 Calendar cal = Calendar.getInstance();
	        cal.setTime(time);
	        cal.set(Calendar.HOUR_OF_DAY, 16);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        return cal.getTime();
	}
}
