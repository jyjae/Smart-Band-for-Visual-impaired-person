package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UnumChangeRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://172.20.10.5/Nodemcu_db_record_view/UnumChangeRequest.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public UnumChangeRequest(String pid, String unum, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("Pid", pid);
        map.put("Unum", unum);



    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
