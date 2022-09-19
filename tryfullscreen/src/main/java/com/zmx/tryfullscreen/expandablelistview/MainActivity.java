package com.zmx.tryfullscreen.expandablelistview;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import com.zmx.tryfullscreen.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Declare ExpandableListView
    ExpandableListView expandableListView;

    // Create ExpandableListAdapter
    ExpandableListAdapter expandableListAdapter;

    List<String> parents; // A list of parents (strings)
    Map<String, List<String>> childrenMap; // An object that maps keys to values.
            // A map cannot contain duplicate keys; each key can map to at most one value
            // Map Documentation: https://developer.android.com/reference/java/util/Map.html
            // HashMap Documentation: https://developer.android.com/reference/java/util/HashMap.html
            // Hashtable Documentation: https://developer.android.com/reference/java/util/Hashtable.html

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the expandablelistview from our XML layout file
//        expandableListView = (ExpandableListView) findViewById(R.id.list_view_expandable);
        expandableListView = binding.listViewExpandable;
        // Populate our data and provide associations
        fillData();

        /**
         * Instantiate our ExpandableListAdapter, pass through
         *  - Context (this)
         *  - List of Parents (parents)
         *  - Map populated with our values and their associations (childrenMap)
         *  (requires a constructor which accepts these values)
         */
        expandableListAdapter = new MyExpandableListAdapter(this, parents, childrenMap);

        // Set the value in the ExpandableListView
        expandableListView.setAdapter(expandableListAdapter);

        /** Set a listener on the child elements - show toast as an example */
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(MainActivity.this, parents.get(groupPosition) + " : " + childrenMap.get(parents.get(groupPosition)).get(childPosition), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    /**
     * Populate our parents and children with values, associate children to parents with HashMap
     */
    public void fillData() {
        parents = new ArrayList<>(); // List of Parent Items
        childrenMap = new HashMap<>(); // HashMap to map keys to values

        // Add parents to the parents list
        parents.add("Parent - Susan");
        parents.add("Parent - Jeff");
        parents.add("Parent - Simon");

        // Create lists to hold the data for the children of the parents
        List<String> susanChildren = new ArrayList<>();
        List<String> jeffChildren = new ArrayList<>();
        List<String> simonChildren = new ArrayList<>();

        // Add children to the children lists
        susanChildren.add("Child - Becky");
        susanChildren.add("Child - Guy");
        susanChildren.add("Child - Mark");

        jeffChildren.add("Child - Micky");
        jeffChildren.add("Child - Fry");
        jeffChildren.add("Child - Clark");

        simonChildren.add("Child - Rick");
        simonChildren.add("Child - Don");
        simonChildren.add("Child - Sam");

        // Associate children with parents using the childrenMap HashMap
        // Associate susanChildren with "Parent - Susan"
        childrenMap.put(parents.get(0),susanChildren);
        // Jeff
        childrenMap.put(parents.get(1),jeffChildren);
        // Simon
        childrenMap.put(parents.get(2),simonChildren);


    }
}
