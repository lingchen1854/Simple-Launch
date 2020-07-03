package com.likego.backgrounddesktop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likego.backgrounddesktop.model.AppInfoModel;

import java.util.ArrayList;

/**
 * User: ap01854
 * Date: 2020/6/30
 * Time: 16:05:11
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<AppInfoModel> mAppInfoModels;

    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;
    int mItemLayoutID;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public RecyclerViewAdapter(Context context, ArrayList<AppInfoModel> appInfoModels,int itemLayoutID) {
        mInflater = LayoutInflater.from(context);
        mAppInfoModels = appInfoModels;
        mItemLayoutID = itemLayoutID;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(mItemLayoutID, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.appName.setText(mAppInfoModels.get(position).getAppName());
        holder.versionName.setText(mAppInfoModels.get(position).getVersionName());
        holder.versionCode.setText(mAppInfoModels.get(position).getVersionCode());
        holder.icon.setImageDrawable(mAppInfoModels.get(position).getIcon());
        if (mOnItemClickListener != null){
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
            holder.item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAppInfoModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout item;
        TextView appName;
        TextView versionName;
        TextView versionCode;
        ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.rc_item);
            appName = itemView.findViewById(R.id.app_name_id);
            versionName = itemView.findViewById(R.id.version_name_id);
            versionCode = itemView.findViewById(R.id.version_code_id);
            icon = itemView.findViewById(R.id.icon_id);
        }
    }

}
