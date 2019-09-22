package kolotilko.e.insurance_test.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PROPERTY_TYPE")
public class PropertyType implements Serializable {
    
    
    @Id
    @Column(name="ID")
    int id; 
    
    @Column(name="TYPE")
    String type;
    
    @Column(name="COEFFICIENT")
    double coefficient;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public double getCoefficient() {
        return coefficient;
    }
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
      

    private static final long serialVersionUID = 1L;
}

