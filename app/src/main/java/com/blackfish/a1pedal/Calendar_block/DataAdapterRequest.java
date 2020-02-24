package com.blackfish.a1pedal.Calendar_block;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blackfish.a1pedal.R;

import java.util.List;

public class DataAdapterRequest extends RecyclerView.Adapter<DataAdapterRequest.ViewHolder> {
    private Context activity;
    private LayoutInflater inflater;
    private List<RequesrList> requesrLists;

    public DataAdapterRequest(Context context, List<RequesrList> recLists) {
        activity=context;
        this.requesrLists = recLists;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public DataAdapterRequest.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.appointment_elem, parent, false);

        return new DataAdapterRequest.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterRequest.ViewHolder holder, int position) {
        RequesrList recomendList = requesrLists.get(position);

        holder.TextBodyView.setText("Запись на "+recomendList.getDate() + ", в " + recomendList.getTime());

       /* holder.NoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }});
            holder.OkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }});
            holder.ChangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }});
            */
    }

    @Override
    public int getItemCount() {
        return requesrLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView TextBodyView  ;
        TextView NoTextView;
        TextView OkTextView;
        TextView ChangeTextView;



        ViewHolder(View view){
            super(view);
            TextBodyView = (TextView) view.findViewById(R.id.TextBodyView);
            NoTextView = (TextView) view.findViewById(R.id.NoTextView);
            OkTextView = (TextView) view.findViewById(R.id.OkTextView);
            ChangeTextView = (TextView) view.findViewById(R.id.ChangeTextView);
        }
    }

}
