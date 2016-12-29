package com.example.abs.login;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ab's on 26-11-2016.
 */

public class Select extends AsyncTask<String,Void,String> {
    Context C;
    private String fresult = "";

    public Select(Context C){
        this.C = C;
    }

    @Override
    protected String doInBackground(String... params) {
        String row_count = null;
        HashMap<String,String> hm = new HashMap<String, String>();
        hm.put("username",params[0]);
        hm.put("password",params[1]);
        try {
            URL url = new URL("http://gyaanify.com/login_nb.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream outstream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outstream,"UTF-8"));
            writer.write(getPostDataString(hm));
            writer.flush();
            writer.close();
            outstream.close();

            InputStream instream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "iso-8859-1"));
            StringBuilder result = new StringBuilder();
            String line = null;

            while((line = reader.readLine())!=null){
                result.append(line+"\n");
            }
            fresult = result.toString();
            reader.close();
            JSONObject root = new JSONObject(fresult);
            JSONArray response = root.getJSONArray("result");
            JSONObject finaljsonobject = response.getJSONObject(0);
            row_count = finaljsonobject.getString("rowcount");
            instream.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return row_count;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(C,"Inserting", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(C,s, Toast.LENGTH_SHORT).show();
    }

    public String getPostDataString(HashMap<String, String> hm) throws UnsupportedEncodingException {
        StringBuilder hashString = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> point : hm.entrySet()){
            if(first)
                first = false;
            else
                hashString.append("&");
            hashString.append(URLEncoder.encode(point.getKey(), "UTF-8"));
            hashString.append("=");
            hashString.append(URLEncoder.encode(point.getValue(), "UTF-8"));
        }
        return hashString.toString();
    }
}
