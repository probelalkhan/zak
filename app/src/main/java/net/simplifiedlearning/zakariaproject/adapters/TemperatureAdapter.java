package net.simplifiedlearning.zakariaproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simplifiedlearning.zakariaproject.R;
import net.simplifiedlearning.zakariaproject.models.Temperature;

import java.util.List;

/**
 * Created by Belal on 9/25/2017.
 */

public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.ViewHolder> {

    private Context mCtx;
    private List<Temperature> temperatureList;

    public TemperatureAdapter(Context mCtx, List<Temperature> temperatureList) {
        this.mCtx = mCtx;
        this.temperatureList = temperatureList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_temperature, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Temperature t = temperatureList.get(position);

        holder.textViewDate.setText(t.getDate());
        holder.textViewTemp1.setText(String.valueOf(t.getTemp1()));
        holder.textViewTemp2.setText(String.valueOf(t.getTemp2()));
        holder.textViewDiff.setText(String.valueOf(t.getDifference()));
    }

    @Override
    public int getItemCount() {
        return temperatureList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewTemp1, textViewTemp2, textViewDiff;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTemp1 = itemView.findViewById(R.id.textViewTemp1);
            textViewTemp2 = itemView.findViewById(R.id.textViewTemp2);
            textViewDiff = itemView.findViewById(R.id.textViewDiff);
        }
    }
}
