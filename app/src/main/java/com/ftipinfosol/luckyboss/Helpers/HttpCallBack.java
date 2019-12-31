package com.ftipinfosol.luckyboss.Helpers;

import org.json.JSONObject;

public interface HttpCallBack {
    void onJSONResponse(boolean success, JSONObject response);
    void onEmptyResponse(boolean success, int statusCode, JSONObject response);
}
