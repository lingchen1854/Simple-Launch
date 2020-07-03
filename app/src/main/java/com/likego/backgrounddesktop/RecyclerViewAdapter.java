package com.likego.backgrounddesktop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * User: ap01854
 * Date: 2020/6/30
 * Time: 16:05:11
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private String[] mButtonList;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(TextView textView, int position);
    }

    public RecyclerViewAdapter(Context context, String[] list) {
        mInflater = LayoutInflater.from(context);
        mButtonList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.rc_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textView.setText(mButtonList[position]);
        if (mOnItemClickListener != null){
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.textView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mButtonList.length;
    }

    public void addData(int position ,String string) {
//        mButtonList.add(position, string);
        notifyItemInserted(position);
    }

    public void addData(String string) {
//        mButtonList.add(string);
//        notifyItemInserted(mButtonList.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_id);
        }
    }

}
