package com.ilnur.cards.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.ilnur.cards.Adapters.SubjAdapter;
import com.ilnur.cards.Fragments.ListFragment;
import com.ilnur.cards.Json.Card;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.FragmentState;
import com.ilnur.cards.forStateSaving.btn;
import com.ilnur.cards.forStateSaving.list;
import com.ilnur.cards.forStateSaving.subj;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Iterator;



public class SubjFragment extends Fragment {
    private MyDB db;
    private subj subj;

    public SubjFragment(){ }

    public void setState(MyDB db, subj subj){
        this.db = db;
        this.subj = subj;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (MainActivity.main.fragments != null && MainActivity.main.fragments.size()>0) {
            for (Iterator<FragmentState> iterator = MainActivity.main.fragments.iterator(); iterator.hasNext(); ) {
                FragmentState s = iterator.next();
                if (s.name.equals("subj")) {
                    iterator.remove();
                }
            }
            /*for (FragmentState s : MainActivity.main.fragments) {
                if (s.name.equals("subj"))
                    MainActivity.main.deleteFragment();
            }*/
            MainActivity.main.addFragment(new FragmentState("subj", new Gson().toJson(subj)));
            db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.subj_layout, container, false);
        //getActivity().setTitle("Решу ЕГЭ. Карточки");

        setRetainInstance(true);
        //MainActivity.this.enableBackBtn();
        MainActivity main = (MainActivity)getActivity();
        //main.enableBackBtn(true);
        /*ActivityState mainState = new ActivityState("main", new Gson().toJson(MainActivity.main));
        appState.activities[0] = mainState;
        db.updateActState(mainState);*/

        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle("Решу ЕГЭ. Карточки");
        //col.set
        col.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        col.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        col.setExpandedTitleMarginBottom((int) getContext().getResources().getDimension(R.dimen.margin_title_col));
        RaiflatButton learn = col.findViewById(R.id.learn);
        RaiflatButton watch = col.findViewById(R.id.watch);
        RaiflatImageButton rever = col.findViewById(R.id.rever);
        learn.setVisibility(View.GONE);
        watch.setVisibility(View.GONE);
        rever.setVisibility(View.GONE);
        AppBarLayout apbar = AppBarLayout.class.cast(getActivity().findViewById(R.id.apbar));
        apbar.setExpanded(false);

        final GridView gridView = rootview.findViewById(R.id.subj_list);
        SubjAdapter adapter = new SubjAdapter(getContext(), subj.subjects);
        //ListView lv = rootview.findViewById(R.id.list_subj);
        //lv.setAdapter(adapter);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            MainActivity.main.exit = false;

            String[] mas = db.getCatNames(subj.subjects[position]);
            if (mas.length == 0){
                Toast.makeText(rootview.getContext(), "Этот предмет все еще добавляется", Toast.LENGTH_SHORT).show();
                //MyDB.addCurrent(subjects[position]);
            } else {
                main.enableBackBtn(true);
                if (mas.length == 1 && db.isSubjAdded(mas[0])){
                    //MainActivity.exit = false;
                    ButListFragment blf = new ButListFragment();
                    boolean checkRever = db.checkRevers(subj.subjects[position], mas[0]);
                    int id_tittle = db.getParentId(subj.subjects[position], mas[0]);
                    btn btn = new btn(subj.subjects[position], id_tittle, mas[0], db.getSubCatNames(
                            subj.subjects[position], mas[0]), checkRever);

                    blf.setBtn(db,btn);
                    blf.setArguments(savedInstanceState);
                    // load huge page to hashmap
                    //new loadHugePageSubj(subjects[position], id_tittle).execute();

                    //Log.i("POs", mas[position]);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, blf)
                            .addToBackStack("btn")
                            .commit();
                    /*ActivityState mainState = new ActivityState("main", new Gson().toJson(MainActivity.main));
                    appState.activities[0] = mainState;
                    db.updateActState(mainState);*/
                    MainActivity.main.addFragment(new FragmentState("btn", new Gson().toJson(btn)));
                    db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
                    //MainActivity.appState.activities[0] = Ma
                } else {
                    ListFragment lf = new ListFragment();
                    list listState = new list(subj.subjects[position], mas);
                    lf.setTitle(db, listState);
                    lf.setArguments(savedInstanceState);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, lf)
                            .addToBackStack("list")
                            .commit();
                    MainActivity.main.addFragment(new FragmentState("list", new Gson().toJson(listState)));
                    db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
                }
            }

        });
        Log.i("count  ",String.valueOf(getFragmentManager().getBackStackEntryCount()));
        return rootview;
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
        //outState.putCharSequenceArray("subj", subj.subjects);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if (savedInstanceState!=null){
            subj.subjects = savedInstanceState.getStringArray("subj");
        }*/
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        /*if (savedInstanceState!=null){
            subj.subjects = savedInstanceState.getStringArray("subj");
        }*/
    }
    // async to load huge pages to hashmap
    /*private class loadHugePageSubj extends AsyncTask<Void, Void, Void> {
        String subj;
        int id;

        public loadHugePageSubj(String subj, int id){
            this.subj = subj;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("loadSubj", subj+"//"+id);
            // key - subj+//+id
            if (!MyDB.hugePages.containsKey(subj+"//"+id)) {
                Log.i("loadSubj", subj+"//"+id + " not contains");
                //ArrayList<Card> temp = MyDB.getParentCardsWatch(subj, id);
                MyDB.hugePages.put(subj + "//" + id, MyDB.getParentCardsWatch(subj, id));
                Log.i("loadSubj", subj+"//"+id + " added");
            }
            return null;
        }
    }*/
}
