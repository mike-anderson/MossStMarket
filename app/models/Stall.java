package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import models.Category;
 
@Entity
public class Stall extends Model {
     
     public Integer     number;
     public Category      category;
     public Calendar    lastMaintenanceDate;
     public Calendar    nextMaintenanceDate;
    
     public Stall(Integer _number, Category _category, Calendar _lastMaintenanceDate, Calendar _nextMaintenanceDate){
        this.number = _number;
        this.category = _category;
        this.lastMaintenanceDate = _lastMaintenanceDate;
        this.nextMaintenanceDate = _nextMaintenanceDate;
     }
}

