package com.example.project_ewallet.ui.future;



import android.content.Intent;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.project_ewallet.DBManager;

import com.example.project_ewallet.DetailActivity;
import com.example.project_ewallet.ExpenseActivity;
import com.example.project_ewallet.IncomeActivity;

import com.example.project_ewallet.R;
import com.example.project_ewallet.databinding.FragmentFutureBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FutureFragment extends Fragment {


        private FragmentFutureBinding binding;
        private PieChart pieChart;

        private DBManager dbManager;
        private Map<String, Double> typeAmountMap = new HashMap<>();

        ArrayList<String[]> expenseArr = new ArrayList<>();
        ArrayList<String[]> incomeArr = new ArrayList<>();



        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {


            binding = FragmentFutureBinding.inflate(inflater, container, false);

            View root = binding.getRoot();
            pieChart = (PieChart) root.findViewById(R.id.chart);
            dbManager = new DBManager(this.getActivity());
            dbManager.open();
            initPieChart();
            showPieChart();

            Button btnDetails = (Button)root.findViewById(R.id.btnDetails);
            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), DetailActivity.class));
                }
            });
            Button btnExp = (Button)root.findViewById(R.id.btnExpense);
            btnExp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), ExpenseActivity.class));
                }
            });
            Button btnIn = (Button)root.findViewById(R.id.btnIncome);
            btnIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(), IncomeActivity.class));
                }
            });
            dbManager = new DBManager(getContext());
            dbManager.open();
            return root;
        }

    private void getData(){
            Cursor c = dbManager.fetchExpense();
            if (c.getCount() > 0){
                c.moveToFirst();
                while(!c.isLast()){
                    typeAmountMap.put(c.getString(3),c.getDouble(1));
                    c.moveToNext();

                }
            }
    }


    private void showPieChart(){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        //initializing data

        getData();

        //input data and fit data into pie chart entry
        for(String type: typeAmountMap.keySet()){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void initPieChart() {
        //using percentage as values instead of amount
        pieChart.setUsePercentValues(true);

        //remove the description label on the lower left corner, default true if not set
        pieChart.getDescription().setEnabled(false);

        //enabling the user to rotate the chart, default true
        pieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        pieChart.setRotationAngle(0);

        //highlight the entry when it is tapped, default true if not set
        pieChart.setHighlightPerTapEnabled(true);
        //adding animation so the entries pop up from 0 degree
        pieChart.animateY(1400, Easing.EaseInOutQuad);

    }
        public void onDestroyView(){
            super.onDestroyView();
            dbManager.close();
            binding = null;
        }

        //Cannot retrieve getData -> null pointer!!!!
    public void getData(View v) {
        expenseArr.clear();

        Cursor c = dbManager.fetchExpense();
        if (c != null) {
            c.moveToFirst();
            while(!c.isLast()) {
                String[] expense = new String[4];
                expense[0] = c.getString(0);
                expense[1] = c.getString(1); //--------> DATEPICKER?
                expense[2] = c.getString(2);
                expense[3] = c.getString(3);
                Log.d("",String.format("%5d %10s %7.2f\n %10s",
                        c.getInt(0),c.getString(1),c.getDouble(2),c.getString(3))
                );

                expenseArr.add(expense);
                c.moveToNext();
            }
        }
        Intent i = new Intent(getContext(), DetailActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("arraylist", (Serializable) expenseArr);
        i.putExtra("bundle", b);
        startActivity(i);

        incomeArr.clear();
        // move this to a RecyclerView
        Cursor cs = dbManager.fetchIncome();
        if (cs != null) {
            cs.moveToFirst();
            while(!cs.isLast()) {
                String[] expense = new String[4];
                expense[0] = cs.getString(0);
                expense[1] = cs.getString(1);
                expense[2] = cs.getString(2);
                expense[3] = cs.getString(3);
                Log.d("",String.format("%5d %10s %7.2f\n %10s",
                        cs.getInt(0),cs.getString(1),cs.getDouble(2),cs.getString(3))
                );
                //Remember to array database to array
                incomeArr.add(expense);
                cs.moveToNext();
            }
        }
        Intent ii = new Intent(getContext(), DetailActivity.class);
        Bundle bb = new Bundle();
        bb.putSerializable("arraylist", (Serializable) incomeArr);
        ii.putExtra("bundle", bb);
        startActivity(ii);
    }
}
