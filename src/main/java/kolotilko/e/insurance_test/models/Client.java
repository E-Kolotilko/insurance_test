package kolotilko.e.insurance_test.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@Table(name="CLIENTS")
public class Client implements Serializable {
    public static final String ID_JSON_KEY="id";
    public static final String SURNAME_JSON_KEY="surname";
    public static final String NAME_JSON_KEY="name";
    public static final String PATRONYMIC_JSON_KEY="patr";
    public static final String DOB_JSON_KEY="DoB";
    public static final String PASS_SERIES_JSON_KEY="passSeries";
    public static final String PASS_NUMBER_JSON_KEY="passNum";
    
    public static final String SURNAME_SQL_KEY="SURNAME";
    public static final String NAME_SQL_KEY="NAME";
    public static final String PATRONYMIC_SQL_KEY="PATR";
    public static final String DOB_SQL_KEY="DOB";
    public static final String PASS_SERIES_SQL_KEY="PASS_SERIAL";
    public static final String PASS_NUMBER_SQL_KEY="PASP_NUM";
    
    //for persistence
    public Client() {
    }
    
    //{ "surname":"...", "name":"...", "patr":"...", "DoB":long, "passSeries":"...", "passNum":"..."}
    public Client(JSONObject clientInfo) throws Exception {
        surname = clientInfo.getString(SURNAME_JSON_KEY);
        name = clientInfo.getString(NAME_JSON_KEY);
        if (clientInfo.has(PATRONYMIC_JSON_KEY)) {
            patronymic = clientInfo.getString(PATRONYMIC_JSON_KEY);
        }
        long dob = clientInfo.getLong(DOB_JSON_KEY);
        dateOfBirth = new Date(dob);
        if (clientInfo.has(PASS_SERIES_JSON_KEY)) {
          passportSeries = clientInfo.getString(PASS_SERIES_JSON_KEY);
        }
        if (clientInfo.has(PASS_NUMBER_JSON_KEY)) {
          passportNumber = clientInfo.getString(PASS_NUMBER_JSON_KEY);
        }
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLIENTS_SEQ")
    @SequenceGenerator(name="CLIENTS_SEQ", allocationSize=1)
    @Column(name="ID", nullable=false)
    long id; 
    
    @Column(name=SURNAME_SQL_KEY, unique=false, nullable=false)
    String surname;

    @Column(name=NAME_SQL_KEY, unique=false, nullable=false)
    String name;

    @Column(name=PATRONYMIC_SQL_KEY, unique=false, nullable=true)
    String patronymic;    

    @Column(name=DOB_SQL_KEY, unique=false, nullable=false)
    @Temporal(TemporalType.DATE)
    Date dateOfBirth;
    
    @Column(name=PASS_SERIES_SQL_KEY, unique=false, nullable=true)
    String passportSeries;
    
    @Column(name=PASS_NUMBER_SQL_KEY, unique=false, nullable=true)
    String passportNumber;
    
        
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getPassport_series() {
        return passportSeries;
    }
    public void setPassport_series(String passport_series) {
        this.passportSeries = passport_series;
    }
    public String getPassport_number() {
        return passportNumber;
    }
    public void setPassport_number(String passport_number) {
        this.passportNumber = passport_number;
    }

    
    public JSONObject toJsonObject() {
        JSONObject me = new JSONObject();
        me.put(ID_JSON_KEY, id);
        me.put(SURNAME_JSON_KEY, id);
        if (surname != null) {
            me.put(SURNAME_JSON_KEY, surname);
        }
        if (name != null) {
            me.put(NAME_JSON_KEY, name);
        }
        if (patronymic != null) {
            me.put(PATRONYMIC_JSON_KEY, patronymic);
        }
        if (dateOfBirth != null) {
            me.put(DOB_JSON_KEY, dateOfBirth.getTime());
        }
        if (passportSeries != null) {
            me.put(PASS_SERIES_JSON_KEY, passportSeries);
        }
        if (passportNumber != null) {
            me.put(PASS_NUMBER_JSON_KEY, passportNumber);
        }
        return me;
    }
    
    public boolean isDifferent(Client other) {
        if (this.surname!= null) {
            if (!this.surname.equals(other.surname)) {
                return false;
            }
        } else if (other.surname != null) {
            if (!other.surname.equals(this.surname)) {
                return false;
            }
        }
        

        if (this.name!= null) {
            if (!this.name.equals(other.name)) {
                return false;
            }
        } else if (other.name != null) {
            if (!other.name.equals(this.name)) {
                return false;
            }
        }
        
        if (this.patronymic!= null) {
            if (!this.patronymic.equals(other.patronymic)) {
                return false;
            }
        } else if (other.patronymic != null) {
            if (!other.patronymic.equals(this.patronymic)) {
                return false;
            }
        }
        
        
        if (this.dateOfBirth!= null) {
            if (!this.dateOfBirth.equals(other.dateOfBirth)) {
                return false;
            }
        } else if (other.dateOfBirth != null) {
            if (!other.dateOfBirth.equals(this.dateOfBirth)) {
                return false;
            }
        }
        
        
        if (this.passportSeries!= null) {
            if (!this.passportSeries.equals(other.passportSeries)) {
                return false;
            }
        } else if (other.passportSeries != null) {
            if (!other.passportSeries.equals(this.passportSeries)) {
                return false;
            }
        }
        
        
        if (this.passportNumber!= null) {
            if (!this.passportNumber.equals(other.passportNumber)) {
                return false;
            }
        } else if (other.passportNumber != null) {
            if (!other.passportNumber.equals(this.passportNumber)) {
                return false;
            }
        }
        
        return true;
    }
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
