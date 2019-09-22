package kolotilko.e.insurance_test.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

import kolotilko.e.insurance_test.routines.AwardCalculator;

@Entity
@Table(name="CONTRACTS")
public class Contract implements Serializable {    
    public static final String CONTRACT_NUMBER_SQL_KEY="CONTRACT_NUMBER";
    public static final String CONTRACT_DATE_SQL_KEY="CONTRACT_DATE";
    public static final String AWARD_SQL_KEY="AWARD";
    public static final String ACTIVE_FROM_SQL_KEY="ACTIVE_FROM";
    public static final String ACTIVE_TO_SQL_KEY="ACTIVE_TO";
    
    public static final String PROPERTY_TYPE_STR_JSON_KEY = "propertyTypeString";
    
    public static final String INSURANCE_AMOUNT_JSON_KEY="insuranceAmount";
    public static final String ACTIVE_FROM_JSON_KEY="activeFrom";
    public static final String ACTIVE_TO_JSON_KEY="activeTo";
    public static final String YEAR_JSON_KEY="year";
    public static final String AREA_JSON_KEY="area";
    public static final String COUNT_DATE_JSON_KEY="awardCountDate";
    public static final String AWARD_JSON_KEY="award";
    public static final String CONTRACT_NUMBER_JSON_KEY="contractNumber";
    public static final String CONTRACT_DATE_JSON_KEY="contractDate";
    public static final String CLIENT_ID_JSON_KEY="clientId";
    public static final String COUNTRY_JSON_KEY="country";
    public static final String INDEX_JSON_KEY="index";
    public static final String REGION_JSON_KEY="region";
    public static final String DISTRICT_JSON_KEY="district";
    public static final String LOCALITY_JSON_KEY="locality";
    public static final String STREET_JSON_KEY="street";
    public static final String HOUSE_JSON_KEY="houseNumber";
    public static final String BUILDING_JSON_KEY="building";
    public static final String STRUCTURE_JSON_KEY="structure";
    public static final String ROOM_JSON_KEY="room";
    public static final String AGENT_COMMENT_JSON_KEY="comment";
    
    //for persistence
    public Contract() {}
    
    public Contract(JSONObject info) throws IllegalArgumentException {
        if (info == null) throw new IllegalArgumentException("argument can't be null"); 
        try {
            insuranceAmount = info.getString(INSURANCE_AMOUNT_JSON_KEY);
            activeFrom = new Date(info.getLong(ACTIVE_FROM_JSON_KEY));
            activeTo = new Date(info.getLong(ACTIVE_FROM_JSON_KEY));
            
            String propertyTypeString = info.getString(PROPERTY_TYPE_STR_JSON_KEY);
            Integer propertyTypeStored = AwardCalculator.getPropertyIndex(propertyTypeString);
            if (propertyTypeStored == null) {
                throw new IllegalArgumentException("Неизвестный тип собственности"); 
            }
            propertyType = propertyTypeStored;
            
            year = info.getString(YEAR_JSON_KEY);
            area = info.getDouble(AREA_JSON_KEY);
            if (info.has(COUNT_DATE_JSON_KEY)) {
                awardCountDate = new Date(info.getLong(COUNT_DATE_JSON_KEY));
            }
            award = info.getString(AWARD_JSON_KEY);    
            contractNumber = info.getLong(CONTRACT_NUMBER_JSON_KEY);
            if (info.has(CONTRACT_DATE_JSON_KEY)) {
                contractDate = new Date(info.getLong(CONTRACT_DATE_JSON_KEY));   
            }
            if (info.has(CLIENT_ID_JSON_KEY)) {
                clientId = info.getLong(CLIENT_ID_JSON_KEY);
            }

            if (info.has(COUNTRY_JSON_KEY)) {
                country = info.getString(COUNTRY_JSON_KEY);
            }
            if (info.has(INDEX_JSON_KEY)) {
                index = info.getString(INDEX_JSON_KEY);
            }
            if (info.has(REGION_JSON_KEY)) {
                region = info.getString(REGION_JSON_KEY);
            }
            if (info.has(DISTRICT_JSON_KEY)) {
                district = info.getString(DISTRICT_JSON_KEY);
            }
            if (info.has(LOCALITY_JSON_KEY)) {
                locality = info.getString(LOCALITY_JSON_KEY);
            }
            if (info.has(STREET_JSON_KEY)) {
                street = info.getString(STREET_JSON_KEY);
            }
            if (info.has(HOUSE_JSON_KEY)) {
                houseNumber = info.getInt(HOUSE_JSON_KEY);
            }
            if (info.has(BUILDING_JSON_KEY)) {
                building = info.getString(BUILDING_JSON_KEY);
            }
            if (info.has(STRUCTURE_JSON_KEY)) {
                structure = info.getString(STRUCTURE_JSON_KEY);
            }
            if (info.has(ROOM_JSON_KEY)) {
                room = info.getInt(ROOM_JSON_KEY);
            }
            if (info.has(AGENT_COMMENT_JSON_KEY)) {
                comment = info.getString(AGENT_COMMENT_JSON_KEY);
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }        
    }
    
    @Column(name="INSURANCE_AMOUNT")
    String insuranceAmount;
    
    @Column(name=ACTIVE_FROM_SQL_KEY, nullable=false)
    Date activeFrom;
    
    @Column(name=ACTIVE_TO_SQL_KEY, nullable=false)
    Date activeTo;
    
    @Column(name="PROPERTY_TYPE_ID")
    int propertyType;
    
    @Column(name="YEAR", nullable=false)
    String year;
    
    @Column(name="AREA", nullable=false)
    double area;
    
    @Column(name="COUNT_DATE", nullable=false)
    Date awardCountDate;
    
    @Column(name=AWARD_SQL_KEY, nullable=false)
    String award;
    
    @Id
    @Column(name=CONTRACT_NUMBER_SQL_KEY, nullable=false)    
    long contractNumber;
    
    @Column(name=CONTRACT_DATE_SQL_KEY, nullable=true)
    Date contractDate;
    
    @Column(name="CLIENT_ID")
    long clientId;

    @Column(name="COUNTRY", nullable=true)
    String country;
    
    @Column(name="INDEX", nullable=true)
    String index;
    
    @Column(name="REGION", nullable=true)
    String region;
    
    @Column(name="DISTRICT", nullable=true)
    String district;

    @Column(name="LOCALITY", nullable=true)
    String locality;
    
    @Column(name="STREET", nullable=true)
    String street;
    
    @Column(name="HOUSE", nullable=true)
    int houseNumber;

    @Column(name="BUILDING", nullable=true)
    String building;

    @Column(name="STRUCTURE", nullable=true)
    String structure;

    @Column(name="ROOM", nullable=true)
    int room;

    @Column(name="AGENT_COMMENT", nullable=true)
    String comment;

    
    public String getInsuranceAmount() {
        return insuranceAmount;
    }
    public void setInsuranceAmount(String insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }
    public void setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
    }

    public Date getActiveTo() {
        return activeTo;
    }
    public void setActiveTo(Date activeTo) {
        this.activeTo = activeTo;
    }

    public int getPropertyType() {
        return propertyType;
    }
    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public double getArea() {
        return area;
    }
    public void setArea(double area) {
        this.area = area;
    }

    public Date getAwardCountDate() {
        return awardCountDate;
    }
    public void setAwardCountDate(Date awardCountDate) {
        this.awardCountDate = awardCountDate;
    }

    public String getAward() {
        return award;
    }
    public void setAward(String award) {
        this.award = award;
    }

    public long getContractNumber() {
        return contractNumber;
    }
    public void setContractNumber(long contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Date getContractDate() {
        return contractDate;
    }
    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public long getClientId() {
        return clientId;
    }
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }
    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getBuilding() {
        return building;
    }
    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStructure() {
        return structure;
    }
    public void setStructure(String structure) {
        this.structure = structure;
    }

    public int getRoom() {
        return room;
    }
    public void setRoom(int room) {
        this.room = room;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    private static final long serialVersionUID = 1L;
}
