package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
 
@Entity
public class Merchant extends Model {
     
     public String      name;
     public String      category;
     public String      address;
     public String      phoneNumber;
    
     public Merchant(String _name, String _category, String _address, String _phoneNumber){
        this.name = _name;
        this.category = _category;
        this.address = _address;
        this.phoneNumber = _phoneNumber;
     }
}
