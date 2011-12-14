package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import java.text.*;
import models.*;
import org.apache.commons.lang.time.DateUtils;
import play.data.validation.*;

public class Application extends Controller {
	@Before
	static void dropdownSetup() {
		Calendar startDate = Calendar.getInstance();
		int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DATE, daysUntilSaturday);
		
		Calendar tempDate = Calendar.getInstance();
		tempDate = startDate;
		
		SimpleDateFormat dateInWords = new SimpleDateFormat("MMMMM d, yyyy");
		String dateString = dateInWords.format(tempDate.getTime());
		SimpleDateFormat dateInNumbers = new SimpleDateFormat("MMddyy");
		String dateNumbers = dateInNumbers.format(tempDate.getTime());
		
		ArrayList<String[]> dateRange = new ArrayList<String[]>();

		int counter = 0;
		while (counter < 52) {
			dateRange.add(new String[] {dateNumbers, dateString});
			tempDate.add(Calendar.DATE,7);
			dateNumbers = dateInNumbers.format(tempDate.getTime());
			dateString = dateInWords.format(tempDate.getTime());
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
			
			Merchant jack = new Merchant("Jack Johnson", produce.id, "123 Fake st.<br />Victoria BC","jackj@uvic.ca","(250) 871-1902").save();
			Merchant john = new Merchant("John Jackson", produce.id, "124 Fake st.<br />Victoria BC","johnj@uvic.ca","(250) 871-1902").save();
			Calendar cal = Calendar.getInstance();
			cal.set(2011,12-1,3);
			Booking b1 = new Booking(1,230,processedDate(2011,11,5,true),processedDate(2011,12,10,false),jack.id).save();
			Booking b2 = new Booking(11,230,processedDate(2011,11,5,true),processedDate(2011,12,10,false),john.id).save();
		}
    }

	/*
	processedDate returns a date with the exact time 12:00:00AM or 11:59:59PM,
			depending on whether it is the start or end date
	*/
	public static Date processedDate(Integer year, Integer month, Integer day, boolean isStart) {
		Date temp;
		if (isStart)			
			temp = new Date(year - 1900, month - 1, day,12,00,00);
		else
			temp = new Date(year - 1900, month - 1, day,23,59,59);
		return temp;
	}
	
	public static Map<Integer,Booking> getBookingsByDate(Date currentDate)
	{
		List<Booking> allBookings = Booking.findAll();
		Map<Integer, Booking> bookings = new HashMap<Integer,Booking>();

		for (Booking i : allBookings)
		{
			if (currentDate.compareTo(i.startdate) >= 0 && currentDate.compareTo(i.enddate) <= 0)
			{
				
				bookings.put(i.stallnumber, i);
			}
		}
		return bookings;
	}

    public static void index(String dateString) {
		
		Date currentDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
		ParsePosition pos = new ParsePosition(0);
		try{
			currentDate = sdf.parse(dateString,pos);
		}catch(NullPointerException p){
			Calendar startDate = Calendar.getInstance();
			int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DATE, daysUntilSaturday);
			currentDate = startDate.getTime();
			dateString = sdf.format(currentDate.getTime());
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
		
		List<Booking> currentBookingList = Booking.findAll();
		
		Map<Integer, Booking> currentBookings = getBookingsByDate(currentDate);
		Map<Long,Merchant> merchants = new HashMap<Long,Merchant>();
		for(Booking booking : currentBookingList){
			Merchant merchant = Merchant.findById(booking.merchantid);
			merchants.put(booking.merchantid, merchant);
		}
		
        render(allStalls,allCategories,currentBookings,merchants,currentDate,dateString);
    }

	public static void add_booking(String stallNumber, String dateString, String endDateString, String merchantID) {
	
		Date currentDate = null;
		Date endDate = null;
		Calendar startDate = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
		ParsePosition pos = new ParsePosition(0);
		try{
			currentDate = sdf.parse(dateString,pos);
			startDate.setTime(currentDate);
		}catch(NullPointerException p){
			int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DATE, daysUntilSaturday);
			currentDate = startDate.getTime();
		}
		pos.setIndex(0);
		try{
			currentDate = sdf.parse(endDateString,pos);
		}
		catch(NullPointerException p){
			endDate = startDate.getTime();
		}
		
		Stall stall = Stall.find("number",Integer.parseInt(stallNumber)).first();
		Category cat = Category.findById(stall.categoryid);
		
		List<Booking> allBookings = Booking.find("byStallnumber",stall.number).fetch();
		List<Booking> pastBookings = new ArrayList();
		List<Booking> futureBookings = new ArrayList();
		Map<Long,Merchant> merchants = new HashMap<Long,Merchant>();
		
		List<Merchant> selectableMerchants = Merchant.find("categoryid",stall.categoryid).fetch();

		for(Booking booking : allBookings)
		{
			Merchant merchant = Merchant.findById(booking.merchantid);
			merchants.put(booking.merchantid, merchant);
			if (booking.startdate.getTime() < currentDate.getTime())
			{
				pastBookings.add(0,booking);
			}else{
				futureBookings.add(0,booking);
			}
		}
		
		render(stall,currentDate,endDate,merchants,cat,selectableMerchants,pastBookings,futureBookings);
	}
	
	public static void create_booking(String stallNumber, String startDateString, String endDateString, String merchantID){
		
		if(merchantID == null) {
		   	validation.addError("merchantID","A merchantID is required");
			validation.keep();
			//params.flash();
			add_booking(stallNumber,startDateString,endDateString,merchantID);
	   }
		
		Stall stall = Stall.find("number",Integer.parseInt(stallNumber)).first();
		Merchant merchant = Merchant.findById(Long.parseLong(merchantID));
		Category cat = Category.findById(stall.categoryid);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = sdf.parse(startDateString,pos);
		pos.setIndex(0);
		Date endDate = sdf.parse(endDateString,pos);
		
		if (startDate.getTime() > endDate.getTime()){
			validation.addError("date_range_conflict","The start and end date are not a valid range");
			validation.keep();
			//params.flash();
			add_booking(stallNumber,startDateString,endDateString,merchantID);
		}
		
		if (startDate.getTime() <= stall.nextmaintenancedate.getTime() && endDate.getTime() >= stall.nextmaintenancedate.getTime()){
			validation.addError("maintenance_date_conflict","The start and end date include the maintenance date");
			validation.keep();
			//params.flash();
			add_booking(stallNumber,startDateString,endDateString,merchantID);
		}
		
		int weekSpan = (int)((endDate.getTime() - startDate.getTime())/(1000*60*60*24*7)) + 1;
		int bookingTotal = cat.price * weekSpan;
		
		Booking b = new Booking(stall.number,bookingTotal,startDate,endDate,merchant.id).save();
		flash.success("Booking for %s Added!", merchant.name);
		
		index(startDateString);
	}
	
	public static void resolve_booking_conflict(String stallNumber,String startDateString,String endDateString,String merchantID){
		
		Stall stall = Stall.find("number",Integer.parseInt(stallNumber)).first();
		Merchant merchant = Merchant.findById(Integer.parseInt(merchantID));
		Category cat = Category.findById(stall.categoryid);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
		ParsePosition pos = new ParsePosition(0);
		Date startDate = sdf.parse(startDateString,pos);
		pos.setIndex(0);
		Date endDate = sdf.parse(endDateString,pos);
		
		
		//TODO:Split the dates
		
		String maintenanceDateString = sdf.format(stall.nextmaintenancedate.getTime());
		
		flash.success("Booking for %s Added! Please book an alternate stall for this week", merchant.name);
		index(maintenanceDateString);
	}

	public static void add_category(String category_name, String category_colour, String category_price)
	{
		validation.required(category_name);
		validation.required(category_colour);
		validation.required(category_price);
		validation.range(category_price, 1, 5000);

		if(validation.hasErrors()) {
		   params.flash(); // add http parameters to the flash scope
		   validation.keep(); // keep the errors for the next request
		   create_category();
	   }
		flash.success("Thanks for adding the '%s' Category", category_name);
		Category c = new Category(category_name, Integer.parseInt(category_colour,16), Integer.parseInt(category_price));
		c.save();
		index(current_date());
	}

	public static void create_category(){ 
		render();
	}
	
	public static void add_merchant(String merchant_name, String merchant_category, String merchant_addr1, String merchant_addr2, String merchant_city, String merchant_province, String merchant_postal, String merchant_email, String merchant_telephone)
	{
	
		validation.required(merchant_name);
		validation.required(merchant_category);
		validation.required(merchant_addr1);
		validation.required(merchant_city);
		validation.required(merchant_province);
		validation.required(merchant_postal);
		validation.required(merchant_telephone);
		validation.email(merchant_email);

		if(validation.hasErrors()) {
		   params.flash(); // add http parameters to the flash scope
		   validation.keep(); // keep the errors for the next request
		   create_merchant();
		}		
		String address = merchant_addr1 + "<br />";
		if (!merchant_addr2.isEmpty()) {
			address = address + merchant_addr2 + "<br />";
		}
		System.out.println("booop '" + merchant_addr2 + "'");
		address = address + merchant_city + ", " + merchant_province + "<br />" + merchant_postal;
		
		Merchant m = new Merchant(merchant_name, Long.parseLong(merchant_category), address, merchant_telephone, merchant_email);
		m.save();
		flash.success("Thanks for adding a new merchant, '%s'", merchant_name);
		merchants();
	}
	
	public static void create_merchant(){ 
		List<Category> categoryList = Category.findAll();
		Map<Long, Category> allCategories = new HashMap<Long,Category>();
		for(Category cat : categoryList)
		{
			allCategories.put(cat.id,cat);
		}

		render(allCategories);
	}
	
	public static void stalls(String dateString){
		Date currentDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
		ParsePosition pos = new ParsePosition(0);
		try{
			currentDate = sdf.parse(dateString,pos);
		}catch(NullPointerException p){
			Calendar startDate = Calendar.getInstance();
			int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DATE, daysUntilSaturday);
			currentDate = startDate.getTime();
			dateString = sdf.format(currentDate.getTime());
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
		
		List<Booking> currentBookingList = Booking.findAll();
		
		Map<Integer, Booking> currentBookings = getBookingsByDate(currentDate);
		Map<Long,Merchant> merchants = new HashMap<Long,Merchant>();
		for(Booking booking : currentBookingList){
			Merchant merchant = Merchant.findById(booking.merchantid);
			merchants.put(booking.merchantid, merchant);
		}
		
        render(allStalls,allCategories,currentBookings,merchants,currentDate,dateString);
	}
	public static void view_stall(Integer stallNumber) {
		Date currentDate = new Date();

		List<Category> categoryList = Category.findAll();
		Map<Long, Category> allCategories = new HashMap<Long,Category>();
		for(Category cat : categoryList){
			allCategories.put(cat.id,cat);
		}
		
		Stall currentStall = Stall.findById(Long.valueOf(stallNumber));
		
		List<Booking> currentBookingList = Booking.find("stallnumber",stallNumber).fetch();
		
		Map<Long, Booking> currentBookings = new HashMap<Long, Booking>();
		Map<Long,Merchant> merchants = new HashMap<Long,Merchant>();
		for(Booking booking : currentBookingList){
			Merchant merchant = Merchant.findById(booking.merchantid);
			merchants.put(booking.merchantid, merchant);
			currentBookings.put(booking.id,booking);
		}
		
        render(currentStall,allCategories,currentBookings,merchants,currentDate);
	}		
	public static void finance(String dateString){
		Date currentDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
		ParsePosition pos = new ParsePosition(0);
		try{
			currentDate = sdf.parse(dateString,pos);
		}catch(NullPointerException p){
			Calendar startDate = Calendar.getInstance();
			int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DATE, daysUntilSaturday);
			currentDate = startDate.getTime();
			dateString = sdf.format(currentDate.getTime());
		}
		
		List<Stall> stallList = Stall.findAll();
		Map<Integer, Stall> allStalls = new HashMap<Integer,Stall>();
		for(Stall stall : stallList){
			allStalls.put(stall.number, stall);
		}
		
		Map<Integer, Booking> currentBookings = getBookingsByDate(currentDate);
		List<Booking> currentBookingList = new ArrayList(currentBookings.values());
		Integer bookingTotal = 0;
		
		Map<Long,Merchant> merchants = new HashMap<Long,Merchant>();
		for(Booking booking : currentBookingList){
			Merchant merchant = Merchant.findById(booking.merchantid);
			//WOOO MAGIC NUMBERS!
			int weekSpan = (int)((booking.enddate.getTime() - booking.startdate.getTime())/(1000*60*60*24*7)) + 1;
			bookingTotal += (booking.price/weekSpan);
			booking.price = booking.price/weekSpan;
			
			merchants.put(booking.merchantid, merchant);
		}
		
        render(allStalls,currentBookings,merchants,currentDate,dateString,bookingTotal);
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
	public static String current_date(){
		
		Date currentDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
		ParsePosition pos = new ParsePosition(0);
		Calendar startDate = Calendar.getInstance();
		int daysUntilSaturday = 7 - startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DATE, daysUntilSaturday);
		currentDate = startDate.getTime();
		String dateString = sdf.format(currentDate.getTime());
		
		return dateString;	
	}
}
