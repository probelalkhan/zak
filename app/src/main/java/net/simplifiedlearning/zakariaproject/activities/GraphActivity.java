package net.simplifiedlearning.zakariaproject.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.simplifiedlearning.zakariaproject.R;
import net.simplifiedlearning.zakariaproject.api.EndPoints;
import net.simplifiedlearning.zakariaproject.fragments.TemperatureFragment;
import net.simplifiedlearning.zakariaproject.helper.Constants;
import net.simplifiedlearning.zakariaproject.helper.SharedPrefManager;
import net.simplifiedlearning.zakariaproject.helper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, new TemperatureFragment());
        fragmentTransaction.commit();

        getMinMax();
    }

    private void getMinMax() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_MIN_MAX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray temp = obj.getJSONArray("temp");
                            JSONArray press = obj.getJSONArray("press");

                            Constants.T1_MIN = temp.getInt(0);
                            Constants.T1_MAX = temp.getInt(1);
                            Constants.T2_MIN = temp.getInt(2);
                            Constants.T2_MAX = temp.getInt(3);


                            Constants.P1_MIN = press.getInt(0);
                            Constants.P1_MAX = press.getInt(1);
                            Constants.P2_MIN = press.getInt(2);
                            Constants.P2_MAX = press.getInt(3);


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
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
