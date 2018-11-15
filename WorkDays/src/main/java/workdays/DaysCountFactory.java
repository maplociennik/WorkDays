package workdays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaysCountFactory {
//TODO: change days[] to enum class
	private final static String days[]= {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
	private final static SimpleDateFormat dayFormat = new SimpleDateFormat("E");
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	
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
		for(int i = startYear; i<endYear+1; i++) {
			List<Date> events = createHolidayListForYear(startYear);
			for(Date event: events) {
				if(checkHoliday(startPoint, endPoint, event)) {
					eventsCount++;
					System.out.println(event);
				}
			}
		}		
		return eventsCount;
	}
	
	private static List<Date> createHolidayListForYear(int startYear) {
		
		final List<String> tab = new ArrayList<String>();
		tab.add("0101");
		tab.add("0601");
		
		String esterMonaday;
		int magicA = 24;
		int magicB = 6;
		int a=startYear%19;
		int b=startYear%4;
		int c=startYear%7;
		int d=(19*a + magicA)%30;
		int e=(2*b+4*c+6*d+magicB)%7;
		if(e+d<9) {
			esterMonaday=d+e+23+"03";
		}else {
			esterMonaday=d+e-8+"04";
		}
		tab.add(esterMonaday);
		tab.add("0105");
		tab.add("0305");
		tab.add("1508");
		tab.add("0111");
		tab.add("1111");
		tab.add("2512");
		tab.add("2612");
		//TODO: add Boze Cialo, 60dni po wielkanocy
		List<Date> holidayList = new ArrayList<Date>();
		for(String day: tab ) {
			try {
				holidayList.add(dateFormat.parse(day+startYear));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		return holidayList;
	}

	private static boolean checkHoliday(Date startPoint, Date endPoint, Date event) {
		if (startPoint.before(event) && 
			endPoint.after(event) && 
			!dayFormat.format(event).equals(days[5]) &&
			!dayFormat.format(event).equals(days[6])) {
			return true;
		}else {
			return false;	
		}
	}
}
