package com.codepath.getmember.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.getmember.models.Member;
import com.codepath.getmember.models.ViewHolderForMember;
import com.codepath.getmember.R;

import java.util.List;

/**
 * Created by keyulun on 2017/7/9.
 */

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Member> memberList;
    private Context mContext;

    public MemberAdapter(Context context, List<Member> lists) {
        memberList = lists;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View memberView = inflater.inflate(R.layout.item_member, parent, false);

        viewHolder = new ViewHolderForMember(memberView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderForMember vh = (ViewHolderForMember) holder;
        vh.getTvID().setText("" + memberList.get(position).getId());
        vh.getTvName().setText(memberList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }
}
