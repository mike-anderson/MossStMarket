package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import models.Category;

@Entity
public class Category extends Model {

     public String      name;	 
     public Integer	    colour;
	 public Integer		price;
    
     public Category(String _name, Integer _colour, Integer _price){
        this.name = _name;
        this.colour = _colour;
		this.price = _price;
     }
}
