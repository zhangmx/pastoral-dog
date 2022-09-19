package com.zmx.tryfullscreen.expandablelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.zmx.tryfullscreen.R;
import com.zmx.tryfullscreen.databinding.ListChildBinding;
import com.zmx.tryfullscreen.databinding.ListParentBinding;

import java.util.List;
import java.util.Map;

/**
 * Created by mattr on 7/5/2017.
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    // Declare context object
    private Context mContext;
    private List<String> mParents;
    private Map<String, List<String>> mChildrenMap;

//    private ListParentBinding listParentBinding;
//    private ListChildBinding listChildBinding;

    /**
     * Constructor
     * @param context
     * @param parents - List of Parents (parents)
     * @param childrenMap - Map populated with our values and their associations (childrenMap)
     */
    public MyExpandableListAdapter(Context context, List<String> parents, Map<String, List<String>> childrenMap) {
        this.mContext = context;
        this.mParents = parents;
        this.mChildrenMap = childrenMap;

        // 这里不能用全局变量，因为这里的context是activity的context，而全局变量是application的context
//        listChildBinding = ListChildBinding.inflate(LayoutInflater.from(context));
//        listParentBinding = ListParentBinding.inflate(LayoutInflater.from(context));
    }

    /**
     * @return - the number of "parents" or groups
     */
    @Override
    public int getGroupCount() {
        return mParents.size();
    }

    /**
     * @param groupPosition - the position we are in the map, each map position may have a different count
     * @return - the number of children in each group (under each parent)
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildrenMap.get(mParents.get(groupPosition)).size();
    }

    /**
     * @param groupPosition
     * @return - for any specific position, return the group name
     */
    @Override
    public Object getGroup(int groupPosition) {
        return mParents.get(groupPosition);
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @return - the name of the child (cycles through each parent (or group), and each child of each parent
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildrenMap.get(mParents.get(groupPosition)).get(childPosition);
    }

    /**
     * @param groupPosition
     * @return the groupPosition as the ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * @param groupPosition
     * @param childPosition
     * @return the childPosition as the ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // not touched
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Get the value of the group item (parent), apply to the TextView using LayoutInflater
     * @param groupPosition
     * @param isExpanded
     * @param convertView // recycles old views in Adapters to increase performance
     * @param parent
     * @return the actual value
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        // Get the value of the parent item
        String parentValue = (String) getGroup(groupPosition);

        ListParentBinding binding = ListParentBinding.inflate(LayoutInflater.from(mContext), parent, false);

        // Create the convertView object if it is null
        if(convertView == null) {
            // Inflate the view in our list_parent.xml
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.list_parent, null);
//            convertView = listParentBinding.getRoot();
            convertView = binding.getRoot();
        }

        // Update the TextView in our convertView (based on the list_parent.xml)
//        TextView parentTextView = (TextView) convertView.findViewById(R.id.list_item_parent);
//        TextView parentTextView = listParentBinding.listItemParent;
        TextView parentTextView = binding.listItemParent;
        parentTextView.setText(parentValue);

        return convertView;
    }

    /**
     * Update and return the value of convertView
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView // recycles old views in Adapters to increase performance
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // get the value of the child item, in the parent (group)
        String childValue = (String) getChild(groupPosition, childPosition);

        ListChildBinding binding = ListChildBinding.inflate(LayoutInflater.from(mContext), parent, false);


        // Create the convertView object if it is null
        if(convertView == null) {
            // Inflate the view in our list_parent.xml
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.list_child, null);
//            convertView = listChildBinding.getRoot();

            convertView = binding.getRoot();

        }

        // Update the TextView in our convertView (based on the list_child.xml)
//        TextView childTextView = (TextView) convertView.findViewById(R.id.list_item_child);
//        childTextView.setText(childValue);

//        TextView childTextView = listChildBinding.listItemChild;
        TextView childTextView = binding.listItemChild;
        childTextView.setText(childValue);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true; // true - we want the child element to be selectable
    }
}
