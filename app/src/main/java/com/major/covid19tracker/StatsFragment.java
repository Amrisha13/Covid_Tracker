package com.major.covid19tracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StatsFragment extends Fragment {

    private static final String STATS_URL = "https://api.covid19api.com/summary";
    //context
    Context context;
    //UI views
    private ProgressBar progressBar;
    private EditText searchEt;
    private ImageButton sortBtn;
    private RecyclerView statsRv;
    ArrayList<ModelStat> statArrayList;
    AdapterStat adapterStat;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        searchEt = view.findViewById(R.id.searchEt);
        sortBtn = view.findViewById(R.id.sortBtn);
        statsRv = view.findViewById(R.id.statsRv);
        progressBar.setVisibility(View.GONE);

        loadStatsData();
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //called as when user type in or remove something

                try {
                    adapterStat.getFilter().filter(s);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //popup msg to show sorting options
        final PopupMenu popupMenu=new PopupMenu(context,sortBtn);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Ascending");
        popupMenu.getMenu().add(Menu.NONE,1,1,"Descending");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==0){
                    Collections.sort(statArrayList,new SortStatCountryAsc());
                    adapterStat.notifyDataSetChanged();
                }
                else if(id==1){
                    Collections.sort(statArrayList,new SortStatCountryDesc());
                    adapterStat.notifyDataSetChanged();
                }
                return false;
            }
        });
        //sort
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             popupMenu.show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatsData();

    }

    private void loadStatsData() {
        progressBar.setVisibility(View.VISIBLE);

        //api call
        StringRequest stringRequest = new StringRequest(Request.Method.GET, STATS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //handle response
                handleResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //failed getting response
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        //add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void handleResponse(String response) {
        statArrayList = new ArrayList<>();
        statArrayList.clear();
        try {

            // we have json object as response
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("Countries");
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("dd/mm/yyyy hh:mm a");
            Gson gson = gsonBuilder.create();

            // start getting data

            for (int i = 0; i < jsonArray.length(); i++) {
                ModelStat modelStat = gson.fromJson(jsonArray.getJSONObject(i).toString(), ModelStat.class);
                statArrayList.add(modelStat);
            }
            //setup adapter
            adapterStat = new AdapterStat(context, statArrayList);
            statsRv.setAdapter(adapterStat);
            progressBar.setVisibility(View.GONE);


        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    public class SortStatCountryAsc implements Comparator<ModelStat> {


        @Override
        public int compare(ModelStat left, ModelStat right) {
            return left.getCountry().compareTo(right.getCountry());
        }
    }
    public class SortStatCountryDesc implements Comparator<ModelStat>{

        @Override
        public int compare(ModelStat left, ModelStat right) {
            return right.getCountry().compareTo(left.getCountry());
        }
    }
}

