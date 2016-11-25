package example.com.projectone.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhinav Ravi on 18/10/16.
 */
public class JSONUtil {
    private  final String LOG_TAG = JSONUtil.class.getSimpleName();
    /**
     * This method returns the list of JSONEntry objects from the JSONString.
     * @param JSONString
     * @param JSONEntry
     * @return
     * @throws JSONException
     */
    public List<JSONObject> getEntryFromJson(String JSONString, String JSONEntry) throws JSONException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(JSONString);
        JSONArray jsonArray = jsonObject.getJSONArray(JSONEntry);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject entryJSONObject = jsonArray.getJSONObject(i);
            jsonObjects.add(entryJSONObject);
        }
        if(!jsonObjects.isEmpty()){
            //Log.d(LOG_TAG,jsonObjects.toString());
            return jsonObjects;
        }
        else{
            Log.e(LOG_TAG, "JSONEntry is empty!");
            throw new JSONException("Not able to get entry " + JSONEntry + " from " + JSONString);
        }
    }
}
