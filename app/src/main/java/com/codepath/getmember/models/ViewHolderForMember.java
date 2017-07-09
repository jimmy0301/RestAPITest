package com.codepath.getmember.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.getmember.R;

/**
 * Created by keyulun on 2017/7/9.
 */

public class ViewHolderForMember extends RecyclerView.ViewHolder {
    private TextView tvID;
    private TextView tvName;

    public ViewHolderForMember(View itemView) {
        super(itemView);
        tvID = (TextView) itemView.findViewById(R.id.tvMemberId);
        tvName = (TextView) itemView.findViewById(R.id.tvMemberName);
    }

    public TextView getTvID() {
        return tvID;
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvID(TextView tvID) {
        this.tvID = tvID;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }
}
