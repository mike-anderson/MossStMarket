package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.*;
 
@Entity
public class Stall extends Model {
     
     public Integer     number;
     public String      category;
     public Calendar    lastMaintenanceDate;
     public Calendar    nextMaintenanceDate;
    
     public Stall(Integer _number, String _category, Calendar _lastMaintenanceDate, Calendar _nextMaintenanceDate){
        this.number = _number;
        this.category = _category;
        this.lastMaintenanceDate = _lastMaintenanceDate;
        this.nextMaintenanceDate = _nextMaintenanceDate;
     }
}

