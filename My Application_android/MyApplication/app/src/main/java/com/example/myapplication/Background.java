package com.example.myapplication;

import android.os.AsyncTask;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Background extends AsyncTask<String, Void, JSONArray>  {
    JSONArray b_jsonArray;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... strUrl) {

        String uri=strUrl[0];

        b_jsonArray=new JSONArray();

        try {

            URL url=new URL(uri);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            InputStream in=new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in,"UTF-8"));
            StringBuffer sb = new StringBuffer();


            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json);
            }

            String s = sb.toString();
            s=s.substring(10, s.length()-1);

            b_jsonArray=new JSONArray(s);

            conn.disconnect();
            bufferedReader.close();
            in.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return b_jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
