package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ValidateUnumRequest extends StringRequest {
    //서버 url 설정(php파일 연동)
    final static  private String URL="http://172.20.10.5/Nodemcu_db_record_view/ValidateUnumRequest.php";
    private Map<String,String> map;

    public ValidateUnumRequest(String unum, Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("Unum",unum);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}