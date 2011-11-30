package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import java.text.*;
import models.*;
import org.apache.commons.lang.time.DateUtils;

public class Application extends Controller {
	@Before
	static void addDefaults() {
		Calendar startDate = Calendar.getInstance();
		int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DATE, daysUntilSaturday);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM d, yyyy");
		String currentDate = sdf.format(startDate.getTime());
		
		List<String> dateRange = new ArrayList();
				
		int counter = 0;
		
		while (counter < 52) {
			dateRange.add(currentDate);
			startDate.add(Calendar.DATE,7);
			currentDate = sdf.format(startDate.getTime());
			counter++;
		}
		
		renderArgs.put("dateRange",dateRange);
	}
	
	public static void initData(){
		Category produce = new Category("Produce",0xCAA3C2,250).save();
		Category crafts = new Category("Crafts", 0xC0DCDD,300).save();
		Category jewelry = new Category("Jewelry",0xFFFF9E,90).save();
		
		Stall stall;
		
		for (int i = 1; i <= 32; i++){
            Calendar last = Calendar.getInstance();
			last.set(2010,11-1,26);
            Calendar next = Calendar.getInstance();
			next.set(2011,12-1,3);
            if (i <= 4) {
                stall = new Stall(i,crafts.id,last.getTime(),next.getTime()).save();
			} else {
                stall = new Stall(i,produce.id,last.getTime(),next.getTime()).save();
			}
            last.add(Calendar.DAY_OF_YEAR,7);
            next.add(Calendar.DAY_OF_YEAR,7);
        }
		
		Merchant jack = new Merchant("Jack Johnson", produce.id, "123 Fake st.Victoria BC","jackj@uvic.ca","(250) 871-1902").save();
		Merchant john = new Merchant("John Jackson", produce.id, "124 Fake st.Victoria BC","johnj@uvic.ca","(250) 871-1902").save();
		Calendar cal = Calendar.getInstance();
		cal.set(2011,12-1,3);
		Booking b1 = new Booking(1,230,cal.getTime(),cal.getTime(),jack.id).save();
		SimpleDateFormat sdf = new SimpleDateFormat("MMMMM d, yyyy");
		String date = sdf.format(b1.startdate);
		renderText("Success: added %s to the database\n and booked stall %d for %s",jack.name,b1.stallnumber,date);
    }

	public static void getBookings(Integer year, Integer month, Integer day){
		
	}

    public static void index(String date) {
		Date currentDate = null;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
			ParsePosition pos = new ParsePosition(0);
			currentDate = sdf.parse(date,pos);
		}catch(NullPointerException p){
			//just dont want this error popping up
		}
		if (currentDate == null){
			Calendar startDate = Calendar.getInstance();
			int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DATE, daysUntilSaturday);
			currentDate = DateUtils.round(startDate.getTime(), Calendar.DATE);
		}
		
		List<Category> categoryList = Category.findAll();
		Map<Long, Category> allCategories = new HashMap<Long,Category>();
		for(Category cat : categoryList){
			allCategories.put(cat.id,cat);
		}
		
		List<Stall> stallList = Stall.findAll();
		Map<Integer, Stall> allStalls = new HashMap<Integer,Stall>();
		for(Stall stall : stallList){
			allStalls.put(stall.number, stall);
		}
		
		//List<Booking> currentBookingList = Booking.find("byStartdateGreaterThanEqualsAndEnddateLessThanEquals",currentDate, currentDate).fetch();
		List<Booking> currentBookingList = Booking.findAll();
		Map<Integer, Booking> currentBookings = new HashMap<Integer,Booking>();
		for(Booking booking : currentBookingList){
			currentBookings.put(booking.stallnumber, booking);
		}
		
		Map<Long,Merchant> merchants = new HashMap<Long,Merchant>();
		for(Booking booking : currentBookingList){
			Merchant merchant = Merchant.findById(booking.merchantid);
			merchants.put(booking.merchantid, merchant);
		}
		
        render(allStalls,allCategories,currentBookings,merchants,currentDate);
    }

	public static void add_booking(){
		render();
	}
	
	public static void stalls(){
		List<Booking> allBookings = Booking.findAll();
		render();
	}
	
	public static void finance(){
		render();
	}
	
	public static void merchants(){
		List<Merchant> allMerchants = Merchant.findAll();
		render(allMerchants);
	}

}
