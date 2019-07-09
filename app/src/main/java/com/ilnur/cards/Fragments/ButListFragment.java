package com.ilnur.cards.Fragments;

import android.content.Context;
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
import com.ilnur.cards.Json.Category;
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
    private String parent;
    private String title;
    private int layout;
    private boolean checkRever;
    private ArrayList<Category> list;
    private int id;
    //private Context context;
    List<TreeNode> nodes = new ArrayList<>();


    public void setButListFragment(String subj, String parent, String title, ArrayList<Category> list,
                                   int layout, boolean checkRever, int id, Context context){
        this.subj = subj;
        this.parent = parent;
        this.title = title;
        this.list = list;
        this.layout = layout;
        this.checkRever = checkRever;
        this.id = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.butlist_fragment, container, false);
        //getActivity().setTitle(title);
        /*TextView tv = rootview.findViewById(R.id.text_title);
        tv.setText(title);*/
        if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
            list = savedInstanceState.getParcelableArrayList("key");
        }

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
                        .replace(R.id.parent, lf)
                        .addToBackStack("lf")
                        .commit();
            }
        });
        watch.setOnClickListener(v -> {
            WatchFragment wf = new WatchFragment();
            wf.setWatchFragment(subj, title, id, true);
            getFragmentManager().beginTransaction()
                    .replace(R.id.parent, wf)
                    .addToBackStack("watch")
                    .commit();
        });

        //GridView lv = rootview.findViewById(R.id.list_but);
        //lv.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        //Arrays.sort(list.toArray());
        //AdapterButList adapter = new AdapterButList(rootview.getContext(), list, layout, subj);
        RecyclerView rv = rootview.findViewById(R.id.list_but);
        rv.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        if (nodes.isEmpty())
            init(rootview.getContext());


        if (nodes.isEmpty()){
            TextView nothing = rootview.findViewById(R.id.nothing);
            nothing.setVisibility(View.VISIBLE);
        }

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
        //lv.setAdapter(adapter);

        rv.setAdapter(adapter);
        Log.i("count  ",String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("subj", subj);
        outState.putCharSequence("title", title);
        outState.putInt("id", id);
        outState.putParcelableArrayList("key", list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            subj = savedInstanceState.getString("subj");
            title = savedInstanceState.getString("title");
            id = savedInstanceState.getInt("id");
        }
    }
}
