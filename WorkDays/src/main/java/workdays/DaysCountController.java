package workdays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DaysCountController {
	
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	private final static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	
    @RequestMapping("/days")
    public ReportDays daysLeft(@RequestParam(value="date", required= false) String endDate) throws ParseException {
    	
    	Date startPoint =new Date();
    	Date endPoint =new Date();
    	
    	try {
			startPoint=dateFormat.parse(dateFormat.format(startPoint));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
    	
    	if(endDate==null) {
    		try {
				endPoint =	dateFormat.parse("3112"+yearFormat.format(startPoint));
			} catch (ParseException e) {
				e.printStackTrace();
			} 
    	}else {
    		try {
				endPoint= dateFormat.parse(endDate);
			} catch (ParseException e) {
				throw new IllegalArgumentException("Date: "+endDate+" do not match format ddMMyyyy");
			}
    	}
    	
    	if(startPoint.after(endPoint)) {
    		throw new IllegalArgumentException(new StringBuilder().
    												append("Date: ").
    												append(endDate).
    												append(" before ").
    												append(startPoint).
    												append(". Report for the past is not in the scope of this service.")
    												.toString());
    	}
    	
    	return DaysCountFactory.createReport(startPoint, endPoint);
    }
}
