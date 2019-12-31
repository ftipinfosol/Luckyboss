package com.ftipinfosol.luckyboss.Helpers;

import org.json.JSONArray;
import org.json.JSONObject;

public interface HttpCallBackJsonArray {
    void onJSONResponse(boolean success, JSONArray response);
    void onEmptyResponse(boolean success, int statusCode, JSONObject response);
}
