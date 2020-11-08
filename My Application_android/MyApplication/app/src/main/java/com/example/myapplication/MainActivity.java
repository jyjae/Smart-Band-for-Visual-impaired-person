package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    Background task;
    JSONArray jsonArray;
    static Button btnRenew;
    TabHost tabHost;

    private EditText et_Unum;

    private Button btn_change;
    private AlertDialog dialog;
    private Button btn_check;

    private boolean validate = false;
    private boolean unumcheck = false;
    public static String savePid;
    ViewGroup mapViewContainer;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String pid = intent.getStringExtra("Pid");
        savePid = pid;

        Toast.makeText(getApplicationContext(), "Service 시작", Toast.LENGTH_SHORT).show();
        intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);


        tabHost = findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpecMap = tabHost.newTabSpec("").setContent(R.id.tabMap).setIndicator("지도");
        TabHost.TabSpec tabSpecSet = tabHost.newTabSpec("").setContent(R.id.tabSetting).setIndicator("정보");
        tabHost.addTab(tabSpecMap);
        tabHost.addTab(tabSpecSet);


        btnRenew = findViewById(R.id.btnRenew);
        et_Unum = findViewById(R.id.et_Unum);
        btn_change = findViewById(R.id.btn_change);


        mapViewContainer = findViewById(R.id.MapView);


        setView(setValue(), mapViewContainer);


        btnRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mapViewContainer.removeAllViews();
                setView(setValue(), mapViewContainer);

            }
        });


        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate = false;
                unumcheck = false;
                String unum = et_Unum.getText().toString();
                if (validate) {
                    return;
                }
                if (unum.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                dialog = builder.setMessage("존재하는 제품번호입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();

                                dialog.show();
                                //et_id.setEnabled(false);
                                validate = true;
                                unumcheck = true;
                                //btn_validate.setText("확인");


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(validateRequest);
            }
        });


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Unum = et_Unum.getText().toString();
                String pid = savePid;

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");


                            //회원가입 성공시
                            if (success) {
                                Toast.makeText(getApplicationContext(), "변경 완료", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(UnumChangeActivity.this, MainActivity.class);
                                //startActivity(intent);
                                //회원가입 실패시
                            } else {
                                Toast.makeText(getApplicationContext(), "변경 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if (unumcheck) {
                    UnumChangeRequest unumChangeRequest = new UnumChangeRequest(pid, Unum, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(unumChangeRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "제품번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
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


    public void setView(Location location, ViewGroup mapViewContainer) {

        mapViewContainer.removeAllViews();

        mapView = new MapView(MainActivity.this);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(location.getDbLati(), location.getDblongi());


        mapView.setMapCenterPoint(mapPoint, true);
        mapViewContainer.addView(mapView);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);


    }


}

