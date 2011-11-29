package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
import models.Category;
 
@Entity
public class Stall extends Model {
     
     public Integer     number;
     public Long	    categoryid;
     public Date	    lastmaintenancedate;
     public Date    	nextmaintenancedate;
    
     public Stall(Integer _number, Long _categoryID, Date _lastMaintenanceDate, Date _nextMaintenanceDate){
        this.number = _number;
        this.categoryid = _categoryID;
        this.lastmaintenancedate = _lastMaintenanceDate;
        this.nextmaintenancedate = _nextMaintenanceDate;
     }
}

