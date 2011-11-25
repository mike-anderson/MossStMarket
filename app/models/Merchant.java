package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import models.Category;

@Entity
public class Merchant extends Model {
     
     public String      name;
     public Category      category;
     public String      address;
     public String      email;
     public String      phoneNumber;
    
     public Merchant(String _name, Category _category, String _address, String _phoneNumber){
        this.name = _name;
        this.category = _category;
        this.address = _address;
        this.phoneNumber = _phoneNumber;
     }
}
