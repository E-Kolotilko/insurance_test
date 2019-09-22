package kolotilko.e.insurance_test.routines;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import kolotilko.e.insurance_test.models.PropertyType;

public class AwardCalculator {
    
    protected static final Map<String, Double> propertyCoefMap = new TreeMap<>(); 
    protected static final Map<String, Integer> propertyIndexMap = new TreeMap<>(); 
    static {        
        updatePropertyCoefMap();
    }
    
    protected static String errorMsg;
    public static String getErrorMsg(){
        return errorMsg;
    }
    
    public static synchronized List<String> getPropertyTypes() {
        List<String> result = new ArrayList<>();
        for (String s : propertyCoefMap.keySet()) {
            result.add(s);
        }
        return result;
    }
    
    public static synchronized Integer getPropertyIndex(String key) {
        if (key==null) return null;
        return propertyIndexMap.get(key);
    }
    
    public static synchronized void updatePropertyCoefMap (){
        propertyCoefMap.clear();
        propertyIndexMap.clear();
        //*
        EntityManager em = DbWatcher.EM_FACTORY.createEntityManager();
        TypedQuery<PropertyType> tq = em.createQuery("SELECT PT FROM PropertyType PT", PropertyType.class);
        List<PropertyType> propertyTypeList = null; 
        try {
            propertyTypeList = tq.getResultList();
        }
        catch (NoResultException e) {
            propertyTypeList = new ArrayList<PropertyType>();
        }
        catch (Exception e) {
            errorMsg = e.getMessage();
        }
        //*/
        if (propertyTypeList.isEmpty()) {
            //default
            propertyCoefMap.put("Квартира", 1.7);
            propertyCoefMap.put("Дом", 1.5);
            propertyCoefMap.put("Комната", 1.3);
        }
        else {
            for (PropertyType p : propertyTypeList) {
                propertyCoefMap.put(p.getType(), p.getCoefficient());
                propertyIndexMap.put(p.getType(), p.getId());
            }
        }        
    }
    
    public static synchronized double calculateAward(double insuranceAmount, int days, String propertyType, 
            int builtYear, double area) throws IllegalArgumentException {                
        double propTypeCoef;
        double builtYearCoef;
        double areaCoef;        

        if (days == 0) {
            throw new IllegalArgumentException("Количество дней не может равняться 0");
        }
        if (propertyType==null) {
            throw new IllegalArgumentException("Тип собственности не может быть null");
        }
        if (!propertyCoefMap.containsKey(propertyType)) {
            throw new IllegalArgumentException("Неизвестный тип собственности");
        }
        propTypeCoef = propertyCoefMap.get(propertyType);
        
        
        if (builtYear<2000) {
            builtYearCoef = 1.3;
        }
        else if (builtYear<=2014) {
            builtYearCoef = 1.6;
        }
        else if (builtYear==2015) {
            builtYearCoef = 2;
        }
        else {
            throw new IllegalArgumentException("Коэффициент для этого года постройки неизвестен");
        }
        
        if (area<0) 
            throw new IllegalArgumentException("Площадь не может быть отрицательной");
        if (area<50) {
            areaCoef = 1.2;
        }
        else if (area<=100) {
            areaCoef = 1.5;
        }
        else {
            areaCoef = 2;
        }
        
        return (insuranceAmount/days)*propTypeCoef*builtYearCoef*areaCoef; 
    }
    
    
}
