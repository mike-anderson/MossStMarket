package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;

@Entity
public class Booking extends Model {
     
     public Integer	    stallnumber;
	 public Integer		price;
     public Date	 	startdate;
	 public Date		enddate;
     public long		merchantid;
    
     public Booking(Integer _location, Integer _price, Date _startDate, Date _endDate, long _merchant){
        this.stallnumber = _location;
        this.price = _price;
        this.startdate = _startDate;
		this.enddate = _endDate;
        this.merchantid = _merchant;
		// This is for a test commint, k thnx bye
     }

}
