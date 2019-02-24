package com.vu.tab.servy;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.vu.tab.servy.bean.CommonBean;
import com.vu.tab.servy.bean.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner, spinnerNBF;
    private static final String[] paths = {"item 1", "item 2", "item 3"};
    private static List<String> bankList=new ArrayList<>();
    private static List<String> nbfiList=new ArrayList<>();
    private LinearLayout bankSection,nbfiSection,customerSection,landSection,buildingSection,flatSection,machinarySection;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.bank_spiner);
        spinnerNBF = (Spinner)findViewById(R.id.nbfi_spiner);
        RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group_appType);
        RadioGroup propRg = (RadioGroup) findViewById(R.id.radio_group_propType);
        // liner layout for application type and ui section show hide
        bankSection=(LinearLayout)this.findViewById(R.id.bank_section);
        nbfiSection=(LinearLayout)this.findViewById(R.id.nbfi_section);
        customerSection=(LinearLayout)this.findViewById(R.id.customer_section);
        bankSection.setVisibility(LinearLayout.GONE);
        nbfiSection.setVisibility(LinearLayout.GONE);
        customerSection.setVisibility(LinearLayout.GONE);

        // liner layout for property type and application ui show hide
        landSection=(LinearLayout)this.findViewById(R.id.land_section);
        buildingSection=(LinearLayout)this.findViewById(R.id.building_section);
        flatSection=(LinearLayout)this.findViewById(R.id.flat_section);
        machinarySection=(LinearLayout)this.findViewById(R.id.machinary_section);
        landSection.setVisibility(LinearLayout.GONE);
        buildingSection.setVisibility(LinearLayout.GONE);
        flatSection.setVisibility(LinearLayout.GONE);
        machinarySection.setVisibility(LinearLayout.GONE);

        // bank and Nbfi list loading from API

        //new JsonTask().execute("http://10.44.22.212:8080/srvm/v1/getList?type=BRBANK");
       //new JsonTask().execute("http://10.44.22.212:8080/srvm/v1/getList?type=BRNBFI");

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_bank:
                        bankSection.setVisibility(LinearLayout.VISIBLE);
                        nbfiSection.setVisibility(LinearLayout.GONE);
                        customerSection.setVisibility(LinearLayout.GONE);
                      //  new JsonTask().execute("http://10.44.22.212:8080/srvm/v1/getList?type=BRBANK");
                        break;
                    case R.id.radio_nbfi:
                        bankSection.setVisibility(LinearLayout.GONE);
                        nbfiSection.setVisibility(LinearLayout.VISIBLE);
                        customerSection.setVisibility(LinearLayout.GONE);
                      //  new JsonTask().execute("http://10.44.22.212:8080/srvm/v1/getList?type=BRNBFI");
                        break;
                    case R.id.radio_customer:
                        bankSection.setVisibility(LinearLayout.GONE);
                        nbfiSection.setVisibility(LinearLayout.GONE);
                        customerSection.setVisibility(LinearLayout.VISIBLE);

                        break;
                }
            }
        });
        propRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_building:
                        landSection.setVisibility(LinearLayout.GONE);
                        buildingSection.setVisibility(LinearLayout.VISIBLE);
                        flatSection.setVisibility(LinearLayout.GONE);
                        machinarySection.setVisibility(LinearLayout.GONE);
                        break;
                    case R.id.radio_flat:
                        landSection.setVisibility(LinearLayout.GONE);
                        buildingSection.setVisibility(LinearLayout.GONE);
                        flatSection.setVisibility(LinearLayout.VISIBLE);
                        machinarySection.setVisibility(LinearLayout.GONE);
                        break;
                    case R.id.radio_land:
                        landSection.setVisibility(LinearLayout.VISIBLE);
                        buildingSection.setVisibility(LinearLayout.GONE);
                        flatSection.setVisibility(LinearLayout.GONE);
                        machinarySection.setVisibility(LinearLayout.GONE);

                        break;
                    case R.id.radio_machinary:

                        landSection.setVisibility(LinearLayout.GONE);
                        buildingSection.setVisibility(LinearLayout.GONE);
                        flatSection.setVisibility(LinearLayout.GONE);
                        machinarySection.setVisibility(LinearLayout.VISIBLE);
                        break;
                }
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,bankList);
        bankList.clear();

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapterNBF = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,nbfiList);

        adapterNBF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNBF.setAdapter(adapterNBF);
        spinnerNBF.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            com.google.gson.Gson gson = new com.google.gson.Gson();

//To convert json string to class use fromJson
            Converter converter = gson.fromJson(result, Converter.class);
            if(converter.type.trim().equals("branch_bank")){
                Map<Integer,Integer> bankMap=new HashMap<>();
                for (CommonBean commonBean:converter.list) {
                    if(!bankMap.containsKey(commonBean.id)){
                        bankList.add(commonBean.name);
                        bankMap.put(commonBean.id,commonBean.id);
                    }

                }
            }
            if(converter.type.trim().equals("branch_nbfi")){
                for (CommonBean commonBean:converter.list) {
                    nbfiList.add(commonBean.name);
                }
            }

            if (pd.isShowing()){
                pd.dismiss();
            }

            String test="";

        }
    }
}
