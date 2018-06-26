package com.examples.apps.atta.recipesengine.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.apps.atta.recipesengine.Model.TotalDaily;
import com.examples.apps.atta.recipesengine.Model.TotalNutrients;
import com.examples.apps.atta.recipesengine.R;

public class NutrientListAdapter extends RecyclerView.Adapter<NutrientListAdapter.ViewHolder>{

    private TotalNutrients totalNutrients;
    private TotalDaily totalDailies;
    private Context mContext;

    public NutrientListAdapter(TotalNutrients totalNutrients, TotalDaily totalDailies, Context mContext) {
        this.totalNutrients = totalNutrients;
        this.totalDailies = totalDailies;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NutrientListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nutrient_list_item
                ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NutrientListAdapter.ViewHolder holder, int position) {
        if (totalNutrients != null && totalDailies != null){
            switch (position){
                case 0 :
                    if (totalNutrients.getFAT()!=null) {
                        holder.title.setText(totalNutrients.getFAT().getLabel());
                        holder.nutrientTotal.setText(new StringBuilder().append
                                (String.format("%.2f", totalNutrients.getFAT().getQuantity())).append(" ")
                                .append(totalNutrients.getFAT().getUnit()).toString());
                        holder.nutrientDaily.setText(new StringBuilder().append
                                (String.format("%.2f", totalDailies.getFAT().getQuantity()))
                                .append(totalDailies.getFAT().getUnit()).toString());
                    }else {
                        holder.title.setText(R.string.no_info_available);
                    }
                    break;

                case 1:
                    if (totalNutrients.getCarbs()!=null) {
                        holder.title.setText(totalNutrients.getCarbs().getLabel());
                        holder.nutrientTotal.setText(new StringBuilder().append
                                (String.format("%.2f", totalNutrients.getCarbs().getQuantity()))
                                .append(" ").append(totalNutrients.getCarbs().getUnit()).toString());
                        holder.nutrientDaily.setText(new StringBuilder().append(String.format("%.2f"
                                , totalDailies.getCarbs().getQuantity())).append(totalDailies.getCarbs().getUnit()).toString());
                    }else {
                        holder.title.setText(R.string.no_info_available);
                    }
                    break;
                case 2:
                    if (totalNutrients.getCHOLE()!=null) {
                        holder.title.setText(totalNutrients.getCHOLE().getLabel());
                        holder.nutrientTotal.setText(new StringBuilder().append(String.format("%.2f"
                                , totalNutrients.getCHOLE().getQuantity()))
                                .append(" ").append(totalNutrients.getCHOLE().getUnit()).toString());
                        holder.nutrientDaily.setText(new StringBuilder().append(String.format("%.2f"
                                , totalDailies.getCholesterol().getQuantity()))
                                .append(totalDailies.getCholesterol().getUnit()).toString());
                    }else {
                        holder.title.setText(R.string.no_info_available);
                    }
                    break;
                case 3:
                    if (totalNutrients.getFiber()!=null) {
                        holder.title.setText(totalNutrients.getFiber().getLabel());
                        holder.nutrientTotal.setText(new StringBuilder().append(String.format("%.2f"
                                , totalNutrients.getFiber().getQuantity())).append(" ")
                                .append(totalNutrients.getFiber().getUnit()).toString());
                        holder.nutrientDaily.setText(new StringBuilder().append(String.format("%.2f"
                                , totalDailies.getFiber().getQuantity())).append(totalDailies.getFiber().getUnit()).toString());
                    }else {
                        holder.title.setText(R.string.no_info_available);
                    }
                    break;
                case 4:
                    if (totalNutrients.getProtein()!=null) {
                        holder.title.setText(totalNutrients.getProtein().getLabel());
                        holder.nutrientTotal.setText(new StringBuilder().append(String.format("%.2f"
                                , totalNutrients.getProtein().getQuantity())).append(" ")
                                .append(totalNutrients.getProtein().getUnit()).toString());
                        holder.nutrientDaily.setText(new StringBuilder().append(String.format("%.2f"
                                , totalDailies.getProtein().getQuantity()))
                                .append(totalDailies.getProtein().getUnit()).toString());
                    }else {
                        holder.title.setText(R.string.no_info_available);
                    }
                    break;
                case 5:
                    if (totalNutrients.getSUGAR()!=null) {
                        holder.title.setText(totalNutrients.getSUGAR().getLabel());
                        holder.nutrientTotal.setText(new StringBuilder().append(String.format("%.2f"
                                , totalNutrients.getSUGAR().getQuantity())).append(" ")
                                .append(totalNutrients.getSUGAR().getUnit()).toString());
                    }else {
                        holder.title.setText(R.string.no_info_available);
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (totalNutrients != null && totalDailies != null){
            return 6;
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView nutrientTotal;
        TextView nutrientDaily;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.nutrient_title);
            nutrientTotal = itemView.findViewById(R.id.total_nutrient);
            nutrientDaily = itemView.findViewById(R.id.daily_nutrient);

        }
    }
}
