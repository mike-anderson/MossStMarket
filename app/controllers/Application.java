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
	
	@Before	
	public static void initData(){
        if(Category.count() == 0) {
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
			Booking b2 = new Booking(11,230,cal.getTime(),cal.getTime(),john.id).save();
			SimpleDateFormat sdf = new SimpleDateFormat("MMMMM d, yyyy");
			String date = sdf.format(b1.startdate);
		}
    }

	//dan's interpretation of the getBookings() render
	public static void getBookings(Integer year, Integer month, Integer day)
	{
		List<Booking> allBookings = Booking.findAll();
		Map<Long, Booking> bookings = new HashMap<Long,Booking>();
		Date specifiedDate = new Date(year.intValue() - 1990, month.intValue(), day.intValue());

		for (Booking i : allBookings)
		{
			if (specifiedDate.compareTo(i.startdate) >= 0 || specifiedDate.compareTo(i.enddate) <= 0)
			{
				bookings.put(i.id, i);
			}
		}

		render(bookings);
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

	public static void add_booking() {
		
		render();
	}

	public static boolean add_merchant(String merchant_name, String merchant_category, String merchant_addr1, String merchant_addr2, String merchant_city, String merchant_province, String merchant_postal, String merchant_email, String merchant_telephone)
	{
		return false;
	}

	/* this is dan's attempt at things; might not actually work*/
	public static void create_merchant()
	{
		List<Category> categoryList = Category.findAll();
		Map<Long, Category> allCategories = new HashMap<Long,Category>();
		for(Category cat : categoryList)
		{
			allCategories.put(cat.id,cat);
		}
		//Merchant m = new Merchant(newName, newCatID, newAddress, newPhone, newEmail);

		//m.create();
		
		//System.out.println("CALLED");

		render(allCategories);
	}
	
	public static void stalls(String date){
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
	
	public static void finance(){
		
		Calendar startDate = Calendar.getInstance();
		int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DATE, daysUntilSaturday);
		Date currentDate = DateUtils.round(startDate.getTime(), Calendar.DATE);
		
		//List<Booking> currentBookingList = Booking.find("byStartdate",currentDate).fetch();
		List<Booking> currentBookingList = Booking.findAll();
		
		Map<Long,Merchant> merchants = new HashMap<Long,Merchant>();
		for(Booking booking : currentBookingList){
			Merchant merchant = Merchant.findById(booking.merchantid);
			merchants.put(booking.merchantid, merchant);
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
		
		render(currentBookingList,merchants,allCategories,allStalls);
	}
	
	public static void merchants(){

		Calendar startDate = Calendar.getInstance();
		int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DATE, daysUntilSaturday);
		Date currentDate = DateUtils.round(startDate.getTime(), Calendar.DATE);
		
		//List<Booking> currentBookingList = Booking.find("byStartdate",currentDate).fetch();
		List<Booking> currentBookingList = Booking.findAll();
		
		List<Merchant> allMerchants = Merchant.findAll();
		
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
		
		render(currentBookingList,allMerchants,allCategories,allStalls);
	}

}
