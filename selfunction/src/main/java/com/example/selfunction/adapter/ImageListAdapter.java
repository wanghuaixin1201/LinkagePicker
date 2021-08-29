package com.example.selfunction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.selfunction.ImageListActivity;
import com.example.selfunction.R;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ItemClickListViewHolder> {
    /*
    初始化布局
     */
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    List<String> content;
    String name;
    int count = 0;

    public ImageListAdapter(Context mContext, List<String> content, int number) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.content = content;
        count = number;
    }

    @Override
    public ItemClickListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemClickListViewHolder(mContext, mLayoutInflater.inflate(R.layout.item_image, null));
    }

    @Override
    public void onBindViewHolder(final ItemClickListViewHolder holder, final int position) {
        if (position > content.size() - 1) return;

        final String listBean = content.get(position);

        if(null != listBean) {
            Glide.with(mContext).load(listBean).error(mContext.getResources().getDrawable(R.mipmap.ic_launcher)).dontAnimate().into(holder.iv_img);

            holder.iv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageListActivity.class);
                    intent.putExtra("img", listBean);
                    mContext.startActivity(intent);

                }
            });
        }
    }

    class ItemClickListViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_img;

        public ItemClickListViewHolder(final Context mContext, View itemview) {
            super(itemview);

            iv_img = itemview.findViewById(R.id.iv_img);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }
}
