package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;

public class Application extends Controller {

    public static void initData(){
        Merchant John = new Merchant("John Leicestershire", "Crafts", "1243 Notreal Ave,\nVictoria, BC,\nV8P 2Y9","(250) 123-4567").save();
        Merchant Mary = new Merchant("Mary Lamb", "Produce","221 Baker St,\nVictoria, BC\nV8P 2J9","(250) 892-1892").save();
        Merchant Susan = new Merchant("Susan Brown", "Crafts","231 Christmas St.\nVictoria, BC\nV8P 9K8","(250) 182-3942").save();
        
        for (int i = 1; i <= 32; i++){
            Calendar last = Calendar.getInstance();
			last.set(2010,11-1,26);
            Calendar next = Calendar.getInstance();
			next.set(2011,12-1,3);
            if (i <= 4) {
                Stall stall1 = new Stall(i,"Crafts",last,next).save();
			} else {
                Stall stall1 = new Stall(i,"Produce",last,next).save();
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
        render();
    }

	public static void bookings(){
		render();
	}
	
	public static void finance(){
		render();
	}
	
	public static void vendors(){
		render();
	}

}
