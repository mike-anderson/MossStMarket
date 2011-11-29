package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import models.Category;

@Entity
public class Merchant extends Model {
     
     public String      name;
     public Long	    categoryid;
     public String      address;
     public String      email;
     public String      phonenumber;
    
     public Merchant(String _name, Long _categoryID, String _address, String _phoneNumber, String _email){
        this.name = _name;
        this.categoryid = _categoryID;
        this.address = _address;
		this.email = _email;
        this.phonenumber = _phoneNumber;
     }
}
