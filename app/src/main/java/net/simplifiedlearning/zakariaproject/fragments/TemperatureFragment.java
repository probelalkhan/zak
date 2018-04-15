package net.simplifiedlearning.zakariaproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;


import net.simplifiedlearning.zakariaproject.R;
import net.simplifiedlearning.zakariaproject.adapters.TemperatureAdapter;
import net.simplifiedlearning.zakariaproject.api.EndPoints;
import net.simplifiedlearning.zakariaproject.helper.Constants;
import net.simplifiedlearning.zakariaproject.helper.Helper;
import net.simplifiedlearning.zakariaproject.helper.MyNotificationManager;
import net.simplifiedlearning.zakariaproject.helper.VolleySingleton;
import net.simplifiedlearning.zakariaproject.models.Pressure;
import net.simplifiedlearning.zakariaproject.models.Temperature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Belal on 9/25/2017.
 */

public class TemperatureFragment extends Fragment {


    RecyclerView recyclerView;
    TemperatureAdapter adapter;
    List<Temperature> temperatureList;

    LineChart temperatureChart;

    SimpleDateFormat sdf;

    boolean keepRunning = true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temperature, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        keepRunning = true;

        view.findViewById(R.id.buttonPressure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewContainer, new PressureFragment());
                fragmentTransaction.commit();
                keepRunning = false;
            }
        });


        TextView textView = getActivity().findViewById(R.id.textViewTitle);
        textView.setText("Temperature");

        sdf = new SimpleDateFormat("mm:ss");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        temperatureList = new ArrayList<>();
        adapter = new TemperatureAdapter(getActivity(), temperatureList);
        recyclerView.setAdapter(adapter);
        temperatureChart = view.findViewById(R.id.temperatureChart);


        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (keepRunning) {
                    loadTemperatureValues();
                    handler.postDelayed(this, 3000);
                }
            }
        }, 3000);

    }


    private void loadTemperatureValues() {
        Log.d("temp1", "temp1");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_TEMPERATURE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        temperatureList.clear();
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray array = obj.getJSONArray("temperatures");


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject temp = array.getJSONObject(i);

                                temperatureList.add(new Temperature(
                                        temp.getInt("id"),
                                        temp.getDouble("temperature1"),
                                        temp.getDouble("temperature2"),
                                        temp.getString("date")
                                ));

                            }

                            Temperature p = temperatureList.get(temperatureList.size() - 1);

                            if(p.getTemp1() > Constants.T1_MAX || p.getTemp1() < Constants.T1_MIN || p.getTemp2() > Constants.T2_MAX || p.getTemp2() < Constants.T2_MIN){
                                new MyNotificationManager(getActivity()).addNotification();
                            }


                            loadGraph(temperatureList);

                            adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void loadGraph(List<Temperature> temperatureList) {
        ArrayList<Entry> temp1 = new ArrayList<>();
        ArrayList<Entry> temp2 = new ArrayList<>();

        for (Temperature t : temperatureList) {
            temp1.add(new Entry(Helper.getTime(t.getDate()) / 60000, (float) t.getTemp1()));
            temp2.add(new Entry(Helper.getTime(t.getDate()) / 60000, (float) t.getTemp2()));

        }

        Collections.sort(temp1, new EntryXComparator());
        Collections.sort(temp2, new EntryXComparator());


        LineDataSet dataSet1 = new LineDataSet(temp1, "Temperature 2");
        dataSet1.setColor(Color.RED);
        dataSet1.setLineWidth(3.0f);
        LineDataSet dataSet2 = new LineDataSet(temp2, "Temperature 1");
        dataSet2.setLineWidth(3.0f);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        LineData data = new LineData(dataSets);

        temperatureChart.setDrawGridBackground(false);
        temperatureChart.setData(data);

        XAxis xAxis = temperatureChart.getXAxis();

        YAxis leftAxis = temperatureChart.getAxisLeft();
        YAxis rightAxis = temperatureChart.getAxisRight();

        leftAxis.setAxisMaximum(50);
        leftAxis.setAxisMinimum(0);
        rightAxis.setAxisMaximum(50);
        rightAxis.setAxisMinimum(0);


        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return sdf.format(new Date((long) value));
            }
        });

        temperatureChart.invalidate();


    }
}
