package routines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import routines.Utils;
/*
 * This class is supposed to provide convenient methods for extracting parameters 
 * from get and post request in Encoding-safe way.
 * Yes, there were precedents when request.getParameterMap() returned screwed up strings
 */
public class UtilsWeb {
	protected static final Logger aLogger = LogManager.getLogger();

    public Map<String,String> getStrMapFromGetJson(HttpServletRequest request) {    	
        Map<String, String> result = new TreeMap<String,String>();
        JSONObject parsed = getJsonFromGet(request);
        for (String aKey : parsed.keySet()) {
            //parsed.getString(aKey) will fall if not string
            result.put(aKey, String.valueOf(parsed.get(aKey)));
        }
        return result;
    }


    public Map<String,String> getStrMapFromPostJson(HttpServletRequest request) {
        TreeMap<String, String> result = new TreeMap<String,String>();
        JSONObject parsed = getJsonFromPost(request);
        for (String aKey : parsed.keySet()) {
            //result.put(aKey, parsed.getString(aKey)); //this will fall if not string
            result.put(aKey, String.valueOf(parsed.get(aKey)));
        }
        return result;
    }


    public JSONObject getJsonFromGet(HttpServletRequest request) {
        try {
            String queryString = request.getQueryString();
            if (queryString != null) {
                queryString = URLDecoder.decode(queryString, "UTF-8");
                return new JSONObject(queryString);
            }
        } catch (Exception decodeExc) {
            aLogger.error("Decoding exception! Message: "+decodeExc.getMessage());
        }
        return new JSONObject();
    }


    public JSONObject getJsonFromPost(HttpServletRequest request) {
        String queryString = null;
        try (InputStream inStream = request.getInputStream())  {
            queryString = Utils.getDecodedString(inStream,request.getCharacterEncoding());
        }
        catch (Exception ioExc) {
            aLogger.error("getJsonFromPost exception! Message: "+ioExc.getMessage());
        }
        if (queryString != null) {
            try {
            	return new JSONObject(queryString);
            }
            catch (Exception decodeExc) {
                aLogger.error("Decode exception! Message: "+decodeExc.getMessage());
            }
        }
        return new JSONObject();
    }


    public Map<String,String> getMapFromGet(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return Utils.getMapFromQuerryStr(queryString);
    }
    
    public Map<String, String> getMapFromPost(HttpServletRequest request) {
        String queryString = null;
        try (InputStream inStream = request.getInputStream())  {
            queryString = Utils.getDecodedString(inStream,request.getCharacterEncoding());
        }
        catch (Exception ioExc) {
            aLogger.error("getMapFromGet exception! Message: "+ioExc.getMessage());
        }
        return Utils.getMapFromQuerryStr(queryString);
    }

    public JSONArray getJsonArrFromGet(HttpServletRequest request) {
        try {
            String queryString = request.getQueryString();
            if (queryString != null) {
                queryString = URLDecoder.decode(queryString, "UTF-8");
                return new JSONArray(queryString);
            }
        } catch (Exception decodeExc) {
            aLogger.error("Decoding exception! Message: "+decodeExc.getMessage());
        }
        return new JSONArray();
    }


    public JSONArray getJsonArrFromPost(HttpServletRequest request) {
        String queryString = null;
        try (InputStream inStream = request.getInputStream()) {
            queryString = Utils.getDecodedString(inStream,request.getCharacterEncoding());
        }
        catch (Exception ioExc) {
            aLogger.error("getJsonArrFromPost exception! Message: "+ioExc.getMessage());
        }
        if (queryString != null) {
            try {
            	return new JSONArray(queryString);
            }
            catch (Exception decodeExc) {
                aLogger.error("Decode exception! Message: "+decodeExc.getMessage());
            }
        }
        return new JSONArray();
    }

}
