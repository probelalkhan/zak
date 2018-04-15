package net.simplifiedlearning.zakariaproject.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import net.simplifiedlearning.zakariaproject.R;
import net.simplifiedlearning.zakariaproject.helper.MyNotificationManager;
import net.simplifiedlearning.zakariaproject.adapters.PressureAdapter;
import net.simplifiedlearning.zakariaproject.api.EndPoints;
import net.simplifiedlearning.zakariaproject.helper.Constants;
import net.simplifiedlearning.zakariaproject.helper.Helper;
import net.simplifiedlearning.zakariaproject.helper.VolleySingleton;
import net.simplifiedlearning.zakariaproject.models.Pressure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Belal on 9/25/2017.
 */

public class PressureFragment extends Fragment {


    MyNotificationManager mManager;
    RecyclerView recyclerView;
    PressureAdapter adapter;
    List<Pressure> pressureList;

    LineChart pressureChart;

    SimpleDateFormat sdf;

    boolean keepRunning = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pressure, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mManager = new MyNotificationManager(getActivity());

        keepRunning = true;
        view.findViewById(R.id.buttonTemperature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.viewContainer, new TemperatureFragment());
                fragmentTransaction.commit();
                keepRunning = false;
            }
        });

        TextView textView = getActivity().findViewById(R.id.textViewTitle);
        textView.setText("Pressure");

        sdf = new SimpleDateFormat("mm:ss");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        pressureList = new ArrayList<>();
        adapter = new PressureAdapter(getActivity(), pressureList);
        recyclerView.setAdapter(adapter);

        pressureChart = view.findViewById(R.id.pressureChart);



        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (keepRunning) {
                    loadPressureValues();
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);

    }


    private void loadPressureValues() {
        Log.d("press1", "press1");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_PRESSURE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pressureList.clear();
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray array = obj.getJSONArray("pressures");


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject temp = array.getJSONObject(i);

                                pressureList.add(new Pressure(
                                        temp.getInt("id"),
                                        temp.getDouble("pressure1"),
                                        temp.getDouble("pressure2"),
                                        temp.getString("date")
                                ));


                            }

                            Pressure p = pressureList.get(pressureList.size() - 1);


                            loadGraph(pressureList);

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

    private void loadGraph(List<Pressure> temperatureList) {
        ArrayList<Entry> press1 = new ArrayList<>();
        ArrayList<Entry> press2 = new ArrayList<>();

        for (Pressure p : temperatureList) {
            press1.add(new Entry(Helper.getTime(p.getDate()) / 60000, (float) p.getPressure1()));
            press2.add(new Entry(Helper.getTime(p.getDate()) / 60000, (float) p.getPressure2()));

        }

        Collections.sort(press1, new EntryXComparator());
        Collections.sort(press2, new EntryXComparator());


        LineDataSet dataSet1 = new LineDataSet(press1, "Presssure 1");
        dataSet1.setColor(Color.RED);
        dataSet1.setLineWidth(3.0f);
        LineDataSet dataSet2 = new LineDataSet(press2, "Pressure 2");
        dataSet2.setLineWidth(3.0f);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);

        LineData data = new LineData(dataSets);

        pressureChart.setDrawGridBackground(false);
        pressureChart.setData(data);

        XAxis xAxis = pressureChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return sdf.format(new Date((long) value));
            }
        });

        pressureChart.invalidate();


    }
}
