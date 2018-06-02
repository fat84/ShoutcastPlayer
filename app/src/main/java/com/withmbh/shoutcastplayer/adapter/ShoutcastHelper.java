package com.withmbh.shoutcastplayer.adapter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.withmbh.shoutcastplayer.R;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class ShoutcastHelper {

    public static List<Shoutcast> retrieveShoutcasts(Context context){

        Reader reader = new InputStreamReader(context.getResources().openRawResource(R.raw.shoutcasts));

        return (new Gson()).fromJson(reader, new TypeToken<List<Shoutcast>>() {}.getType());
    }

}
