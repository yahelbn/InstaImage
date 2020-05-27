package com.example.instaimage.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.instaimage.Objects.Image;
import com.example.instaimage.Activities.OnePictureInfo;
import com.example.instaimage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Adapter Class
public class MyAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Image> mOriginalValues; // Original Values
    private ArrayList<Image> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;
    Context context;

    public MyAdapter(Context context, ArrayList<Image> mProductArrayList) {
        this.mOriginalValues = mProductArrayList;
        this.mDisplayedValues = mProductArrayList;
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        LinearLayout llContainer;
        TextView tvName;
        ImageView ImageUploaded;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.image_info, null);
            holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.ImageUploaded=(ImageView)convertView.findViewById(R.id.imageViewElement);

            //

            //
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mDisplayedValues.get(position).name);
        Picasso.get().load(mDisplayedValues.get(position).ImageURL).fit().into(holder.ImageUploaded);



        holder.llContainer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(context, mDisplayedValues.get(position).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, OnePictureInfo.class);
                intent.putExtra("EXTRA_MESSAGE", mDisplayedValues.get(position).name);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<Image>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Image> FilteredArrList = new ArrayList<Image>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<Image>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i).name;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Image(mOriginalValues.get(i).name,mOriginalValues.get(i).ImageURL));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}