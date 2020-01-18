package com.soumio.inceptiontutorial.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.soumio.inceptiontutorial.R;

import java.util.List;

public class HistoryList extends ArrayAdapter<Leads> {
    private Activity context;
    private List<Leads> historylist;

    public HistoryList(Activity context, List<Leads> historylist) {
        super(context, R.layout.list_layout, historylist);
        this.context = context;
        this.historylist = historylist;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewCategory = listViewItem.findViewById(R.id.textViewCategory);
        TextView textViewLead1 = listViewItem.findViewById(R.id.textViewLead1);
        TextView textViewLead2 = listViewItem.findViewById(R.id.textViewLead2);
        TextView textViewLead3 = listViewItem.findViewById(R.id.textViewLead3);
        TextView textViewLead4 = listViewItem.findViewById(R.id.textViewLead4);
        TextView textViewLead5 = listViewItem.findViewById(R.id.textViewLead5);
        TextView textViewLead6 = listViewItem.findViewById(R.id.textViewLead6);
        TextView textViewLead7 = listViewItem.findViewById(R.id.textViewLead7);
        TextView textViewLead8 = listViewItem.findViewById(R.id.textViewLead8);
        TextView textViewLead9 = listViewItem.findViewById(R.id.textViewLead9);
        TextView textViewLead10 = listViewItem.findViewById(R.id.textViewLead10);
        TextView textViewLead11 = listViewItem.findViewById(R.id.textViewLead11);
        TextView textViewLead12 = listViewItem.findViewById(R.id.textViewLead12);

        Leads leads = historylist.get(position);

        textViewCategory.setText(leads.getpCategory());
        textViewLead1.setText("Lead 1: " + leads.getPlabel1());
        textViewLead2.setText("Lead 2: " + leads.getPlabel2());
        textViewLead3.setText("Lead 3: " + leads.getPlabel3());
        textViewLead4.setText("Lead 4: " + leads.getPlabel4());
        textViewLead5.setText("Lead 5: " + leads.getPlabel5());
        textViewLead6.setText("Lead 6: " + leads.getPlabel6());
        textViewLead7.setText("Lead 7: " + leads.getPlabel7());
        textViewLead8.setText("Lead 8: " + leads.getPlabel8());
        textViewLead9.setText("Lead 9: " + leads.getPlabel9());
        textViewLead10.setText("Lead 10: " + leads.getPlabel10());
        textViewLead11.setText("Lead 11: " + leads.getPlabel11());
        textViewLead12.setText("Lead 12: " + leads.getPlabel12());


        return listViewItem;

    }
}
