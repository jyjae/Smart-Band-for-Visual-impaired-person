package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://172.20.10.5/Nodemcu_db_record_view/LoginRequest.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public LoginRequest(String pid, String pwd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("Pid", pid);
        map.put("Pwd", pwd);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
