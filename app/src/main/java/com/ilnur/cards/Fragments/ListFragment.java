package com.ilnur.cards.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.ilnur.cards.Adapters.AdapterList;
import com.ilnur.cards.Category_Buttons.Binders.Cat_butt_binder;
import com.ilnur.cards.Category_Buttons.Binders.Cat_butt_rev_binder;
import com.ilnur.cards.Category_Buttons.Binders.Cat_head_binder;
import com.ilnur.cards.Category_Buttons.Cat_butt;
import com.ilnur.cards.Category_Buttons.Cat_butt_rev;
import com.ilnur.cards.Category_Buttons.Cat_head;
import com.ilnur.cards.Fragments.ButListFragment;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.Json.Category;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.FragmentState;
import com.ilnur.cards.forStateSaving.btn;
import com.ilnur.cards.forStateSaving.list;


import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class ListFragment extends Fragment {
    private ArrayList<Category> list1;
    private List<TreeNode> nodes = new ArrayList<>();
    private MyDB db;
    private list list;

    public void setTitle(MyDB db, list list) {
        this.db = db;
        this.list = list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MainActivity.main.addFragment(new FragmentState("list", new Gson().toJson(list)));
        /*MainActivity.main.fragments.remove(MainActivity.main.fragments.size()-1);
        MainActivity.main.addFragment(new FragmentState("list", new Gson().toJson(list)));
        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));*/
        for (Iterator<FragmentState> iterator = MainActivity.main.fragments.iterator(); iterator.hasNext(); ) {
            FragmentState s = iterator.next();
            if (s.name.equals("list")) {
                iterator.remove();
            }
        }
        MainActivity.main.addFragment(new FragmentState("list", new Gson().toJson(list)));
        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.list_fragment, container, false);
        //getActivity().setTitle(title);
        setRetainInstance(true);
        /*if (savedInstanceState != null) {
            list.mas = savedInstanceState.getStringArray("mas");
            list.title = savedInstanceState.getString("title");
        }*/

        //Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(list.title);
        col.setExpandedTitleMarginBottom((int) getContext().getResources().getDimension(R.dimen.margin_title_col));
        col.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        col.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        RaiflatButton learn = col.findViewById(R.id.learn);
        RaiflatButton watch = col.findViewById(R.id.watch);
        RaiflatImageButton rever = col.findViewById(R.id.rever);
        learn.setVisibility(View.GONE);
        watch.setVisibility(View.GONE);
        rever.setVisibility(View.GONE);
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(false);
        apbar.setClickable(false);


            if (!db.isSubjAdded(list.title)) {
                Toast.makeText(rootview.getContext(), "Некоторые темы все еще добавляются", Toast.LENGTH_SHORT).show();
                //MyDB.addCurrent(title);
            }

        //Arrays.sort(mas);
        //mas = sort(mas);

        RecyclerView rv = rootview.findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(rootview.getContext()));
        //create treeview if node is empty
        if (nodes.isEmpty())
            init(rootview.getContext());

        //Log.i("mas", "" + mas.length);

        //creating adapter and its listener
        TreeViewAdapter adapter = new TreeViewAdapter(nodes, Arrays.asList(new Cat_head_binder(),
                new Cat_butt_binder(savedInstanceState), new Cat_butt_rev_binder(savedInstanceState)));
        adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                //Log.i("on click", "click");
                Cat_head head = (Cat_head) node.getContent();
                ArrayList<Category> cats = db.getSubCatNames(list.title, head.getTitle());
                if (!node.isLeaf()) {
                    if (!cats.isEmpty()) {
                        MainActivity.main.exit = false;
                        ButListFragment blf = new ButListFragment();

                        boolean checkRever = db.checkRevers(list.title, head.getTitle());

                        int id_tittle = db.getParentId(list.title, head.getTitle());
                        btn btn = new btn(list.title, id_tittle, head.getTitle(), db.getSubCatNames(
                                list.title, head.getTitle()), checkRever);
                        blf.setBtn(db, btn);
                        blf.setArguments(savedInstanceState);
                        //new loadHugePageList(title, id_tittle).execute();

                        //Log.i("POs", mas[position]);
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                                .replace(R.id.parent, blf)
                                .addToBackStack("btn")
                                .commit();
                        /*MainActivity.main.addFragment(new FragmentState("btn", new Gson().toJson(btn)));
                        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));*/
                    }
                } else {
                    if (!cats.isEmpty()) {
                        MainActivity.main.exit = false;
                        ButListFragment blf = new ButListFragment();

                        boolean checkRever = db.checkRevers(list.title, head.getTitle());

                        int id_tittle = db.getParentId(list.title, head.getTitle());

                        btn btn = new btn(list.title, id_tittle, head.getTitle(), db.getSubCatNames(
                                list.title, head.getTitle()), checkRever);
                        blf.setBtn(db, btn);
                        blf.setArguments(savedInstanceState);
                        //new loadHugePageList(title, id_tittle).execute();

                        //Log.i("POs", mas[position]);
                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                                .replace(R.id.parent, blf)
                                .addToBackStack("btn")
                                .commit();
                        /*MainActivity.main.addFragment(new FragmentState("btn", new Gson().toJson(btn)));
                        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));*/
                    }
                }
                return false;
            }

            @Override
            public void onToggle(boolean b, RecyclerView.ViewHolder viewHolder) {

            }
        });
        rv.setAdapter(adapter);

        //Log.i("count  ", String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
    }

    private void init(Context context) {
        for (String s : list.mas) {
            TreeNode<Cat_head> head = new TreeNode<>(new Cat_head(s));
            nodes.add(head);
            if (db.getSubCatNames(list.title, s).isEmpty() || db.getSubCatNames(list.title, s) == null) {
                //Log.i("break", s);
                if (db.checkRevers(list.title, s)) {
                    TreeNode<Cat_butt_rev> tmp = new TreeNode<>(new Cat_butt_rev(list.title, s,
                            db.getParentId(list.title, s), context));
                    head.addChild(tmp);
                } else {
                    TreeNode<Cat_butt> tmp = new TreeNode<>(new Cat_butt(list.title, s, db.getParentId(list.title, s), context));
                    head.addChild(tmp);
                }
            }
        }
    }

    private String[] sort(String[] mas){
        String tmp;
        for (int i = 0; i < mas.length; i++) {
            for (int j = i + 1; j < mas.length; j++) {
                if (mas[i].compareToIgnoreCase(mas[j]) > 0) {
                    tmp = mas[i];
                    mas[i] = mas[j];
                    mas[j] = tmp;
                }
            }
        }
        return mas;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      /*  MainActivity.main.deleteFragment();
        db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

       /* outState.putCharSequenceArray("mas", mas);
        outState.putCharSequence("title", title);*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (savedInstanceState != null) {
            mas = savedInstanceState.getStringArray("mas");
            title = savedInstanceState.getString("title");
        }*/
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        /*if (savedInstanceState != null) {
            mas = savedInstanceState.getStringArray("mas");
            title = savedInstanceState.getString("title");
        }*/
    }
    // async to load huge pages to hashmap
    /*private class loadHugePageList extends AsyncTask<Void, Void, Void> {
        String subj;
        int id;

        public loadHugePageList(String subj, int id){
            this.subj = subj;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("loadList", subj+"//"+id);
            // key - subj+//+id
            if (!MyDB.hugePages.containsKey(subj+"//"+id)) {
                Log.i("loadList", subj+"//"+id + " not contains");
                ArrayList<Card> temp = MyDB.getParentCardsWatch(subj, id);
                MyDB.hugePages.put(subj + "//" + id, temp);
                Log.i("loadList", subj+"//"+id + " added");
            }

            return null;
        }
    }*/
}
