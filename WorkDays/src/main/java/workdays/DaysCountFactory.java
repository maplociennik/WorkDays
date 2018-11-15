package workdays;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.format.number.money.MonetaryAmountFormatter;

public class DaysCountFactory {

	private final static String days[]= {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
	
	public static ReportDays createReport(Date startPoint, Date endPoint) {
		ReportDays report= new ReportDays();
		
		report.setNumberOfDays(ChronoUnit.DAYS.between(startPoint.toInstant(), endPoint.toInstant()));
		report.setNumberOfWeeks(new StringBuilder()
				.append(report.getNumberOfDays()/7)
				.append("w")
				.append(report.getNumberOfDays()%7)
				.append("d")
				.toString());
		report.setNumberOfWorkingDays(calculateWorkdays(startPoint, endPoint));
		report.setNumberOfHollidays(report.getNumberOfDays()-report.getNumberOfWorkingDays());
		
		return report;
	}
	
	private static long calculateWorkdays(Date startPoint, Date endPoint) {
		long alldays = ChronoUnit.DAYS.between(startPoint.toInstant(), endPoint.toInstant());
		long workDays = basicWorkDays(startPoint, alldays);
		workDays = workDays - eventsHoliday(startPoint, endPoint);
		return workDays;
	}

	private static long basicWorkDays(Date startPoint, long alldays) {
		final long workDaysMatrix[][]= { 
				{0, 1, 2, 3, 4, 4, 4},
				{0, 1, 2, 3, 3, 3, 4},
				{0, 1, 2, 2, 2, 3, 4},
				{0, 1, 1, 1, 2, 3, 4},
				{0, 0, 0, 1, 2, 3, 4},
				{0, 0, 1, 2, 3, 4, 5},
				{0, 1, 2, 3, 4, 5, 5}};
		
		SimpleDateFormat dayFormat = new SimpleDateFormat("E");
		final String dayName = dayFormat.format(startPoint);
	    int i=0;
	    long workdays = 0;
	    workdays=(alldays/7)*5;
	    for(String day: days) {
	    	 if(day.equals(dayName)) {
	    		 workdays= workdays + workDaysMatrix[i][((int)alldays%7)];
	    	 }i++;
	    }
		return workdays;
	}
	
	private static long eventsHoliday(Date startPoint, Date endPoint) {
		long eventsCount = 0;
		final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		int startYear = Integer.parseInt(yearFormat.format(startPoint));
		int endYear = Integer.parseInt(yearFormat.format(endPoint));
		for(int i = startYear; i<endYear; i++) {
			if(checkHoliday(startPoint, endPoint, new Date()))eventsCount++;		
		}		
		return eventsCount;
	}
	
	private static boolean checkHoliday(Date startPoint, Date endPoint, Date event) {
		return true;
	}
}
