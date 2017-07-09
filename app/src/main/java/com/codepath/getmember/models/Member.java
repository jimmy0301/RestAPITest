package com.codepath.getmember.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by keyulun on 2017/7/8.
 */

public class Member {
    private Integer id;
    private String name;

    public Member(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ArrayList<Member> fromJsonArray(JSONArray data) throws JSONException {
        ArrayList <Member> list = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            Member member = new Member(data.getJSONObject(i).getInt("ID"), data.getJSONObject(i).getString("name"));
            list.add(member);
        }

        return list;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
