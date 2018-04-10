package net.simplifiedlearning.zakariaproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.simplifiedlearning.zakariaproject.R;
import net.simplifiedlearning.zakariaproject.models.Pressure;
import net.simplifiedlearning.zakariaproject.models.Temperature;

import java.util.List;

/**
 * Created by Belal on 9/25/2017.
 */

public class PressureAdapter extends RecyclerView.Adapter<PressureAdapter.ViewHolder> {

    private Context mCtx;
    private List<Pressure> pressureList;

    public PressureAdapter(Context mCtx, List<Pressure> pressureList) {
        this.mCtx = mCtx;
        this.pressureList= pressureList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_pressure, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pressure p = pressureList.get(position);

        holder.textViewDate.setText(p.getDate());
        holder.textViewPressure1.setText(String.valueOf(p.getPressure1()));
        holder.textViewPressure2.setText(String.valueOf(p.getPressure2()));
        holder.textViewDiff.setText(String.valueOf(p.getDifference()));
    }

    @Override
    public int getItemCount() {
        return pressureList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate, textViewPressure1, textViewPressure2, textViewDiff;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewPressure1 = itemView.findViewById(R.id.textViewPress1);
            textViewPressure2 = itemView.findViewById(R.id.textViewPress2);
            textViewDiff = itemView.findViewById(R.id.textViewDiff);
        }
    }
}
