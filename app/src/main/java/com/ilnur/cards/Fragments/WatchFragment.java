package com.ilnur.cards.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.ilnur.cards.Adapters.AdapterWatch;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class WatchFragment extends Fragment {
    private String subj;
    private String title;
    private int id;
    private boolean parent;
    private AdapterWatch adapter;
    ArrayList<Card> list;


    public void setWatchFragment(String subj, String title, int id, boolean parent) {
        this.subj = subj;
        this.title = title;
        this.id = id;
        this.parent = parent;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("Watch", "saved");
        outState.putCharSequence("subj", subj);
        outState.putCharSequence("title", title);
        outState.putInt("id", id);
        outState.putBoolean("parent", parent);
        outState.putParcelableArrayList("watch", list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Watch", "created");
        if (savedInstanceState!= null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            list = savedInstanceState.getParcelableArrayList("watch");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("Watch", "restored");
        if (savedInstanceState!= null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            list = savedInstanceState.getParcelableArrayList("watch");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.watch_lay, container, false);
        /*if (savedInstanceState !=null){
            onViewStateRestored(savedInstanceState);
        }*/
        //getActivity().setTitle(title);
        if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            parent = savedInstanceState.getBoolean("parent");
            list = savedInstanceState.getParcelableArrayList("watch");
        }
        Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(title);
        col.setExpandedTitleMarginBottom((int) getContext().getResources().getDimension(R.dimen.margin_title_col));
        RaiflatButton learn = col.findViewById(R.id.learn);
        RaiflatButton watch = col.findViewById(R.id.watch);
        RaiflatImageButton rever = col.findViewById(R.id.rever);
        learn.setVisibility(View.GONE);
        watch.setVisibility(View.GONE);
        rever.setVisibility(View.GONE);
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(false);
        Log.i("subj",subj);
        Log.i("title", title);
        Log.i("id", "" +id);
        if (savedInstanceState == null || !savedInstanceState.containsKey("watch")) {
            if (parent)
                list = MyDB.getParentCardsWatch(subj, title, id);
            else
                list = MyDB.getChildCardsWatch(subj, title, id);
        }

        ListView lw = rootview.findViewById(R.id.watch_list);
        //AdapterWatchList adapter = new AdapterWatchList(rootview.getContext(), list, subj);
        adapter = new AdapterWatch(rootview.getContext(), subj, list);
        lw.setAdapter(adapter);
        Log.i("count  ",String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("onDestroy", " 2");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView", " 2");
    }
}
