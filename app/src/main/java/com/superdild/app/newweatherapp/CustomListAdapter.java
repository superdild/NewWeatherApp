package com.superdild.app.newweatherapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by gino on 24/03/18.
 */

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder>{

    Cursor c;
    Context context;
    static  ListItemClickListener clickListener;

    public CustomListAdapter(Cursor c, Context context, ListItemClickListener clickListener) {
        this.c = c;
        this.context = context;
        this.clickListener=clickListener;

    }

    /**
     * Interfaccia
     */
    public interface ListItemClickListener {
        void onListItemCLick(int postition);
    }

    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!c.moveToPosition(position)) {
            c.close();
            return;}
        holder.data.setText(c.getString(c.getColumnIndex(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_DATE_TIME)));
        holder.temp_min.setText(c.getString(c.getColumnIndex(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_TEMP_MIN)));
        holder.description.setText(c.getString(c.getColumnIndex(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_DESCRIPTION)));
        holder.temp_max.setText(c.getString(c.getColumnIndex(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_TEMP_MAX)));
        int id = context.getResources().getIdentifier("ic_" + c.getString(c.getColumnIndex(WeatherDataBaseContract.WeatherDataBaseEntry.COLUMN_ICON_ID)), "drawable", context.getPackageName());
        holder.iconID.setImageResource(id);

    }

    @Override
    public int getItemCount() {
        return c.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView data;
        TextView temp_min, temp_max;
        TextView description;
        ImageView iconID;

        public ViewHolder(View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.single_item_data);
            temp_min = itemView.findViewById(R.id.single_item_temp_min);
            description = itemView.findViewById(R.id.single_item_description);
            iconID = itemView.findViewById(R.id.single_item_icon);
            temp_max = itemView.findViewById(R.id.single_item_temp_max);
            
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                   clickListener.onListItemCLick(pos);
                }
            });
        }

    }
}
