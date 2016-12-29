package com.example.abs.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Filter;

public class success_login extends AppCompatActivity {
    String myJSON;
    ListAdapter adapter;
    private static final String UL = "http://www.gyaanify.com/notes_detailsf.php";
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "Title";
    private static final String TAG_NAME = "Description";
    private static final String TAG_LINK = "Link";
    private static final String ap ="http://gyaanify.com/Notes/";
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        findViewById(R.id.cambtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(success_login.this,cam.class);
                startActivity(i);
            }
        });
        findViewById(R.id.upbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(success_login.this,pdfupload.class);
                startActivity(i);
            }
        });
        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String, String>>();
        getJSON(UL);
    }

    protected void showList(){
        try {
            personList.clear();
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String link = c.getString(TAG_LINK);
                link=ap+link;
                HashMap<String,String> persons = new HashMap<String,String>();
                persons.put(TAG_ID,id);
                persons.put(TAG_NAME,name);
                persons.put(TAG_LINK,link);
                personList.add(persons);
            }
            adapter = new SimpleAdapter(
                    success_login.this, personList, R.layout.activity_list_item,
                    new String[]{TAG_ID,TAG_NAME,TAG_LINK},
                    new int[]{R.id.id, R.id.name,R.id.dwnld});
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                myJSON=s;
                showList();
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getJSON(UL);
    }
}
