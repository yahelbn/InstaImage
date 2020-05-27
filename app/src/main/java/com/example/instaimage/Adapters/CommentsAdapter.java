package com.example.instaimage.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.instaimage.Objects.Comment;
import com.example.instaimage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// Adapter Class
public class CommentsAdapter extends BaseAdapter{

    private ArrayList<Comment> mOriginalValues; // Original Values
    private ArrayList<Comment> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;
    Context context;

    public CommentsAdapter(Context context, ArrayList<Comment> mProductArrayList) {


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
        LinearLayout Container;
        TextView commentTextView;
        ImageView commentImageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.row_comment, null);
                holder.Container = (LinearLayout) convertView.findViewById(R.id.Container);
                holder.commentTextView = (TextView) convertView.findViewById(R.id.commentTextView);
                holder.commentImageView = (ImageView) convertView.findViewById(R.id.commentImageView);

                //

                //
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.commentTextView.setText(mDisplayedValues.get(position).getCommentText());
            Picasso.get().load(R.drawable.account).fit().into(holder.commentImageView);


//        holder.Container.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                Toast.makeText(context, mDisplayedValues.get(position).getCommentText(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, OnePictureInfo.class);
//                intent.putExtra("EXTRA_MESSAGE", mDisplayedValues.get(position).name);
//                context.startActivity(intent);
//            }
//        });

            return convertView;

    }

//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint,FilterResults results) {
//
//                mDisplayedValues = (ArrayList<Comment>) results.values; // has the filtered values
//                notifyDataSetChanged();  // notifies the data with new filtered values
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                ArrayList<Comment> FilteredArrList = new ArrayList<Comment>();
//
//                if (mOriginalValues == null) {
//                    mOriginalValues = new ArrayList<Comment>(mDisplayedValues); // saves the original data in mOriginalValues
//                }
//
//                /********
//                 *
//                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
//                 *  else does the Filtering and returns FilteredArrList(Filtered)
//                 *
//                 ********/
//                if (constraint == null || constraint.length() == 0) {
//
//                    // set the Original result to return
//                    results.count = mOriginalValues.size();
//                    results.values = mOriginalValues;
//                } else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i).getCommentText();
//                        if (data.toLowerCase().startsWith(constraint.toString())) {
//                            FilteredArrList.add(new Comment(mOriginalValues.get(i).getCommentText()));
//                        }
//                    }
//                    // set the Filtered result to return
//                    results.count = FilteredArrList.size();
//                    results.values = FilteredArrList;
//                }
//                return results;
//            }
//        };
//        return filter;
//    }
}