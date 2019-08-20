package com.ilnur.cards.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.ilnur.cards.Category_Buttons.Cat_butt;
import com.ilnur.cards.Category_Buttons.Binders.Cat_butt_binder;
import com.ilnur.cards.Category_Buttons.Cat_butt_rev;
import com.ilnur.cards.Category_Buttons.Binders.Cat_butt_rev_binder;
import com.ilnur.cards.Category_Buttons.Cat_head;
import com.ilnur.cards.Category_Buttons.Binders.Cat_head_binder;
import com.ilnur.cards.Json.Category;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.FragmentState;
import com.ilnur.cards.forStateSaving.btn;
import com.ilnur.cards.forStateSaving.learn;
import com.ilnur.cards.forStateSaving.watch;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class ButListFragment extends Fragment {
    public btn btn;
    private List<TreeNode> nodes = new ArrayList<>();
    private MyDB db;


    public void setBtn(MyDB db,btn btn){
        this.btn = btn;
        this.db = db;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (Iterator<FragmentState> iterator = MainActivity.main.fragments.iterator(); iterator.hasNext(); ) {
            FragmentState s = iterator.next();
            if (s.name.equals("btn")) {
                iterator.remove();
            }
        }
        MainActivity.main.addFragment(new FragmentState("btn", new Gson().toJson(btn)));
        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.butlist_fragment, container, false);
        setRetainInstance(true);

        //restore saved instance
        /*if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            list = savedInstanceState.getParcelableArrayList("key");
        }*/
        //new loadHugePageBtn().execute();

        Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(btn.title);

        RaiflatButton learnBtn = col.findViewById(R.id.learn);
        RaiflatButton watchBtn = col.findViewById(R.id.watch);
        RaiflatImageButton rever = col.findViewById(R.id.rever);
        learnBtn.setVisibility(View.VISIBLE);
        watchBtn.setVisibility(View.VISIBLE);
        rever.setVisibility(View.VISIBLE);
        col.setExpandedTitleMarginBottom((int) getContext().getResources().getDimension(R.dimen.margin_title_exp));
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(true);


        if (btn.checkRevers){
            rever.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LearnFragment lf = new LearnFragment();
                    lf.setArguments(savedInstanceState);
                    learn learnState = new learn(btn.subj, btn.id, btn.title, true,  true);
                    lf.setLearnFragment(db, learnState);
                    //MainActivity.main.addFragment(new FragmentState("learn", new Gson().toJson(learnState)));
                    //db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, lf)
                            .addToBackStack("learn")
                            .commit();
                    /*MainActivity.main.addFragment(new FragmentState("learn", new Gson().toJson(learnState)));
                    db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));*/
                }
            });
        } else {
            rever.setVisibility(View.GONE);
        }
        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearnFragment lf = new LearnFragment();
                lf.setArguments(savedInstanceState);
                learn learnState = new learn(btn.subj, btn.id, btn.title, false,  true);
                lf.setLearnFragment(db, learnState);
                //MainActivity.main.addFragment(new FragmentState("learn", new Gson().toJson(learnState)));
                //db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                        .replace(R.id.parent, lf)
                        .addToBackStack("learn")
                        .commit();
                /*MainActivity.main.addFragment(new FragmentState("learn", new Gson().toJson(learnState)));
                db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));*/
            }
        });
        watchBtn.setOnClickListener(v -> {
            WatchFragment1 wf = new WatchFragment1();
            watch watch = new watch(btn.subj, btn.title, btn.id, true);
            wf.setWatchFragment(db, watch);
            wf.setArguments(savedInstanceState);
            getFragmentManager().beginTransaction()
                    //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_right_web, R.anim.to_left_web)
                    .replace(R.id.parent, wf)
                    .addToBackStack("watch")
                    .commit();
            MainActivity.main.addFragment(new FragmentState("watch", new Gson().toJson(watch)));
            db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
        });

        RecyclerView rv = rootview.findViewById(R.id.list_but);
        rv.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        //create treeview if node is empty
        if (nodes.isEmpty())
            init(rootview.getContext());

        //if there are no subcategories
        if (nodes.isEmpty()){
            TextView nothing = rootview.findViewById(R.id.nothing);
            nothing.setVisibility(View.VISIBLE);
        }
        //creating adapter and its listener
        TreeViewAdapter adapter = new TreeViewAdapter(nodes, Arrays.asList(new Cat_head_binder(),
                new Cat_butt_binder(savedInstanceState), new Cat_butt_rev_binder(savedInstanceState)));
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                Log.i("on click", "click");
                if (!node.isLeaf()) {
                    onToggle(!node.isExpand(), holder);
                    if (!node.isExpand())
                        adapter.collapseBrotherNode(node);
                } else {

                }
                return false;
            }
            @Override
            public void onToggle(boolean b, RecyclerView.ViewHolder viewHolder) {

            }
        });
        rv.setAdapter(adapter);


        Log.i("count  ",String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }

    //init treeview
    public void init(Context context){
        for (Category cat: btn.list){
            TreeNode<Cat_head> head = new TreeNode<>(new Cat_head(cat.getTitle()));
            nodes.add(head);
            if (cat.getReversible() == 1){
                TreeNode<Cat_butt_rev> but = new TreeNode<>(new Cat_butt_rev(btn.subj, cat.getTitle(),
                        cat.getId(), context));
                head.addChild(but);
            } else {
                TreeNode<Cat_butt> but = new TreeNode<>(new Cat_butt(btn.subj, cat.getTitle(),
                        cat.getId(), context));
                head.addChild(but);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //save state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    //also save state
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    //restoring saved state
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    // async to load huge pages to hashmap

}
