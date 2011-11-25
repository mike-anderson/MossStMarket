package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import models.Category;

@Entity
public class Category extends Model {
     
     public String      name;
     public String      colour;
    
     public Category(String _name, String _colour){
        this.name = _name;
        this.colour = _colour;
     }
}
