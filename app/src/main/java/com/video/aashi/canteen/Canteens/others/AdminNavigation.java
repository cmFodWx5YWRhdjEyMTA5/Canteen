package com.video.aashi.canteen.Canteens.others;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.video.aashi.canteen.R;

import java.util.ArrayList;
import java.util.Objects;

public class AdminNavigation  extends RecyclerView.Adapter<AdminNavigation.ViewHolder> {
    ArrayList<String> arrayList = new ArrayList<>();
    TypedArray typedArray;
    Context context;
    boolean selected_item = true;

    public AdminNavigation(ArrayList<String> arrayList, Context context) {

        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public AdminNavigation.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from (viewGroup.getContext()).inflate(R.layout.admin, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i)
    {
           //   DrawableCompat.setTint(Objects.requireNonNull(typedArray.getDrawable(i)), ContextCompat.getColor
        //        (context, R.color.black1));
        viewHolder.textView.setText(arrayList.get(i));
   //     viewHolder.imageView.setImageResource(typedArray.getResourceId(i,-1));

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
    //    ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.navigation_texts);
          //  imageView = (ImageView) itemView.findViewById(R.id.navigation_image);


        }
    }


}
