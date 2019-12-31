package com.ftipinfosol.luckyboss.Helpers;

public class apiURL {
    public static String BASE = "http://dc-luckyboss.sf3.in/api/";
    //public static String BASE = "http://192.168.1.20:8000/api/";

    private static String VERSION="v1";

    private static  String BASE_URL=BASE+VERSION+"/";

    public static  String LOGIN = BASE_URL+"login";

    public static String NEW_TICKET = BASE_URL+"save_ticket";

    public  static  String GET_RATE = BASE_URL+"get_rate";

    public static String GET_TICKETS = BASE_URL+"get_tickets";

    public static String GET_RESULTS_HISTORY = BASE_URL+"get_result_history";

    public static String GET_SIGN_IN_DETAIL = BASE_URL+"get_sign_in_detail";
    public static String UPDATE_SIGN_IN_DETAIL = BASE_URL+"update_sign_in_detail";

    public static String GET_TIME_RESTRICTION = BASE_URL+"get_time_restriction";

    public static String GET_TICKETS_BY_TRANSID = BASE_URL+"get_tickets_by_transID";
    public static String GET_TICKETS_BY_BILLNO = BASE_URL+"get_tickets_by_billNo";


}
