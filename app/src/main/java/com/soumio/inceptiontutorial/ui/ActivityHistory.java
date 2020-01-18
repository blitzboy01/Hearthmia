package com.soumio.inceptiontutorial.ui;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.soumio.inceptiontutorial.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistory extends AppCompatActivity {

    ListView listViewHistory;
    DatabaseReference databaseLeads;

    List<Leads> leadlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        databaseLeads = FirebaseDatabase.getInstance().getReference("LEADS");
        listViewHistory = findViewById(R.id.listViewHistory);

        leadlist = new ArrayList<>();


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseLeads.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                leadlist.clear();

                for (DataSnapshot leadSnapshot : dataSnapshot.getChildren()) {
                    Leads leads = leadSnapshot.getValue(Leads.class);
                    leadlist.add(leads);
                }

                HistoryList adapter = new HistoryList(ActivityHistory.this, leadlist);
                listViewHistory.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }
}
