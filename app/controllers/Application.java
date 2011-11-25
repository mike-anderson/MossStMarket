package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import java.text.SimpleDateFormat;
import models.*;

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
		Category Crafts = new Category("Crafts", "#C0DCDD").save();
		Category Produce = new Category("Produce", "#CAA3C2").save();

        Merchant John = new Merchant("John Leicestershire", Crafts, "1243 Notreal Ave,\nVictoria, BC,\nV8P 2Y9","(250) 123-4567").save();
        Merchant Mary = new Merchant("Mary Lamb", Produce,"221 Baker St,\nVictoria, BC\nV8P 2J9","(250) 892-1892").save();
        Merchant Susan = new Merchant("Susan Brown", Crafts,"231 Christmas St.\nVictoria, BC\nV8P 9K8","(250) 182-3942").save();
        
        for (int i = 1; i <= 32; i++){
            Calendar last = Calendar.getInstance();
			last.set(2010,11-1,26);
            Calendar next = Calendar.getInstance();
			next.set(2011,12-1,3);
            if (i <= 4) {
                Stall stall1 = new Stall(i,Crafts,last,next).save();
			} else {
                Stall stall1 = new Stall(i,Produce,last,next).save();
			}
            last.add(Calendar.DAY_OF_YEAR,7);
            next.add(Calendar.DAY_OF_YEAR,7);
        }
		Calendar temp = Calendar.getInstance();
		temp.set(2011,11-1,26);
        Booking b1 = new Booking((Stall)Stall.find("byNumber",1).first(),temp,John).save();
        Booking b2 = new Booking((Stall)Stall.find("byNumber",5).first(),temp,Mary).save();
		temp.set(2011,12-1,03);
        Booking b3 = new Booking((Stall)Stall.find("byNumber",11).first(),temp,Susan).save();
        System.out.println("success! Database populated with dummy data");  
    }
    public static void index() {
		List<Stall> allStalls = Stall.findAll();
        render(allStalls);
    }
	public static void add_booking(){
		render();
	}
	public static void bookings(){
		List<Booking> allBookings = Booking.findAll();
		render();
	}
	
	public static void finance(){
		render();
	}
	
	public static void vendors(){
		List<Merchant> allVendors = Merchant.findAll();
		render(allVendors);
	}

}
