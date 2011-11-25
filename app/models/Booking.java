package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import models.Stall;
import models.Merchant;

@Entity
public class Booking extends Model {
     
     public Stall       location;
     public Date        date;
     public Merchant    holder;
    
     public Booking(Stall _location, Date _date, Merchant _holder){
        this.location = _location;
        this.date = _date;
        this.holder = _holder;
     }
}
