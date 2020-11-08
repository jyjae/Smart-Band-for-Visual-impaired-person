package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private Button btn_login;
    private TextView txt_register;
    Background task;
    JSONArray jsonArray;
    Button btnRenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id2);
        et_pass = findViewById(R.id.et_pass);

        txt_register = findViewById(R.id.txt_register);
        btn_login = findViewById(R.id.btn_login);

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pid = et_id.getText().toString();
                final String pwd = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            int i = Integer.parseInt(setValue().getStrChecked());


                            if (success) {//로그인 성공시

//                                String userID = jsonObject.getString( "Pid" );
//                                String userPass = jsonObject.getString( "Pwd" );


                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                intent = new Intent(LoginActivity.this, MainActivity.class);

                                intent.putExtra("Pid", pid);
                                intent.putExtra("Pwd", pwd);

                                startActivity(intent);

                            } else {//로그인 실패시
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(pid, pwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

            }
        });
    }


    public Location setValue() {
        double[] dbVal = {0.0, 0.0};
        String strVal;

        Location location = new Location();

        final String url = "http://172.20.10.5/Nodemcu_db_record_view/MainActivity.php";   //서버주소 넣기
        task = new Background();

        try {
            jsonArray = task.execute(url).get();


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dbVal[0] = Double.parseDouble(jsonObject.getString("Lati").toString());
                dbVal[1] = Double.parseDouble(jsonObject.getString("Longi").toString());
                strVal = jsonObject.getString("Checked").toString();

                location.setDbLati(dbVal[0]);
                location.setDblongi(dbVal[1]);
                location.setStrChecked(strVal);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return location;


    }

}


