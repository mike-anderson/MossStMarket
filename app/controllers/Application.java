package controllers;

import play.*;
import play.mvc.*;
import java.util.*;
import models.*;

public class Application extends Controller {

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