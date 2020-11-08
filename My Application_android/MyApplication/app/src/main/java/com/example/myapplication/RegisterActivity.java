package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_Num, et_Phone;

    private Button btn_register;
    private Button btn_validate;
    private AlertDialog dialog;
    private Button btn_check;

    private boolean validate = false;

    private boolean pidcheck = false;
    private boolean unumcheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //아이디값 찾아주기
        et_id = findViewById(R.id.et_id2);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_Num = findViewById(R.id.et_Num);
        et_Phone = findViewById(R.id.et_Phone);

        btn_validate = findViewById(R.id.btn_validate);
        btn_validate.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                validate = false;
                pidcheck = false;
                String pid = et_id.getText().toString();
                if (validate) {
                    return;
                }
                if (pid.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();

                                dialog.show();
                                validate = true;
                                pidcheck = true;
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(pid, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        //제품확인 버튼 클릭 시 수행
        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate = false;
                unumcheck = false;
                String unum = et_Num.getText().toString();
                if (validate) {
                    return;
                }
                if (unum.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("제품번호는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (!success) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("존재하는 제품번호입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();

                                dialog.show();
                                validate = true;
                                unumcheck = true;
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("존재하지 않는 제품번호입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateUnumRequest validateRequest = new ValidateUnumRequest(unum, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        //회원가입 버튼 클릭 시 수행
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pid = et_id.getText().toString();
                String pwd = et_pass.getText().toString();
                String pname = et_name.getText().toString();
                String phone = et_Phone.getText().toString();
                int unum = Integer.parseInt(et_Num.getText().toString());


                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");


                            //회원가입 성공시
                            if (success) {
                                Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                //회원가입 실패시
                            } else {
                                Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                //서버로 Volley를 이용해서 요청
                if (pidcheck && unumcheck) {
                    RegisterRequest registerRequest = new RegisterRequest(pid, pwd, pname, phone, unum, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                } else if (pidcheck == false && unumcheck == true) {
                    Toast.makeText(getApplicationContext(), "아이디를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (pidcheck == true && unumcheck == false) {
                    Toast.makeText(getApplicationContext(), "제품번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pidcheck && !unumcheck) {
                    Toast.makeText(getApplicationContext(), "아이디와 제품번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


    }
}
