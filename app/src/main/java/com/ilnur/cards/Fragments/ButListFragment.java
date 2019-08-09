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
import com.ilnur.cards.Category_Buttons.Cat_butt;
import com.ilnur.cards.Category_Buttons.Binders.Cat_butt_binder;
import com.ilnur.cards.Category_Buttons.Cat_butt_rev;
import com.ilnur.cards.Category_Buttons.Binders.Cat_butt_rev_binder;
import com.ilnur.cards.Category_Buttons.Cat_head;
import com.ilnur.cards.Category_Buttons.Binders.Cat_head_binder;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.Json.Category;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String subj;
    private String title;
    private boolean checkRever;
    private ArrayList<Category> list;
    private int id;
    private List<TreeNode> nodes = new ArrayList<>();
    //private MyDB db;


    public void setButListFragment( String subj, String title, ArrayList<Category> list,
                                   boolean checkRever, int id){
        this.subj = subj;
        this.title = title;
        this.list = list;
        this.checkRever = checkRever;
        this.id = id;
        //this.db = db;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.butlist_fragment, container, false);

        MainActivity.current_tag = "but";
        setRetainInstance(true);

        //restore saved instance
        if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            list = savedInstanceState.getParcelableArrayList("key");
        }
        //new loadHugePageBtn().execute();

        Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(title);

        RaiflatButton learn = col.findViewById(R.id.learn);
        RaiflatButton watch = col.findViewById(R.id.watch);
        RaiflatImageButton rever = col.findViewById(R.id.rever);
        learn.setVisibility(View.VISIBLE);
        watch.setVisibility(View.VISIBLE);
        rever.setVisibility(View.VISIBLE);
        col.setExpandedTitleMarginBottom((int) getContext().getResources().getDimension(R.dimen.margin_title_exp));
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(true);


        if (checkRever){
            rever.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LearnFragment lf = new LearnFragment();
                    lf.setLearnFragment(subj, title, id, true, true);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, lf)
                            .addToBackStack(null)
                            .commit();
                }
            });
        } else {
            rever.setVisibility(View.GONE);
        }
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearnFragment lf = new LearnFragment();
                lf.setLearnFragment(subj, title, id, false, true);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                        .replace(R.id.parent, lf)
                        .addToBackStack("lf")
                        .commit();
            }
        });
        watch.setOnClickListener(v -> {
            WatchFragment1 wf = new WatchFragment1();
            wf.setWatchFragment(subj, title, id, true);
            getFragmentManager().beginTransaction()
                    //.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_right_web, R.anim.to_left_web)
                    .replace(R.id.parent, wf)
                    .addToBackStack("watch")
                    .commit();
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
                new Cat_butt_binder(), new Cat_butt_rev_binder()));
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
        for (Category cat: list){
            TreeNode<Cat_head> head = new TreeNode<>(new Cat_head(cat.getTitle()));
            nodes.add(head);
            if (cat.getReversible() == 1){
                TreeNode<Cat_butt_rev> but = new TreeNode<>(new Cat_butt_rev(subj, cat.getTitle(),
                        cat.getId(), context));
                head.addChild(but);
            } else {
                TreeNode<Cat_butt> but = new TreeNode<>(new Cat_butt(subj, cat.getTitle(),
                        cat.getId(), context));
                head.addChild(but);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(false);
    }


    //save state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("subj", subj);
        outState.putCharSequence("title", title);
        outState.putInt("id", id);
        outState.putParcelableArrayList("key", list);
    }
    //also save state
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
        }
    }


    //restoring saved state
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
        }
    }
    // async to load huge pages to hashmap
    /*private class loadHugePageBtn extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("loadBtn", subj+"//"+id);
            // key - subj+//+id
            if (!MyDB.hugePages.containsKey(subj+"//"+id)) {
                Log.i("loadBtn", subj+"//"+id + " not contains");
                ArrayList<Card> temp = MyDB.getParentCardsWatch(subj, id);
                MyDB.hugePages.put(subj + "//" + id, temp);
                Log.i("loadBtn", subj+"//"+id + " added");
            }
            return null;
        }
    }*/
}
