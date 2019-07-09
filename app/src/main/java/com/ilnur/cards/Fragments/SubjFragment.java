package com.ilnur.cards.Fragments;

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
import com.ilnur.cards.Adapters.SubjAdapter;
import com.ilnur.cards.Fragments.ListFragment;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class SubjFragment extends Fragment {
    private static String[] subjects = {"Русский язык", "Физика", "Математика", "Английский язык", "История"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.subj_layout, container, false);
        //getActivity().setTitle("Решу ЕГЭ. Карточки");

        if (savedInstanceState!=null){
            subjects = savedInstanceState.getStringArray("subj");
        }
        Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
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
        SubjAdapter adapter = new SubjAdapter(getContext(), subjects);
        //ListView lv = rootview.findViewById(R.id.list_subj);
        //lv.setAdapter(adapter);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            MainActivity.exit = false;

            String[] mas = MyDB.getCatNames(subjects[position]);
            if (mas.length == 0){
                Toast.makeText(rootview.getContext(), "Этот предмет все еще добавляется", Toast.LENGTH_SHORT).show();
            } else {
                if (mas.length == 1){
                    //MainActivity.exit = false;
                    ButListFragment blf = new ButListFragment();

                    int layout;
                    boolean checkRever = MyDB.checkRevers(subjects[position], mas[0]);
                    if (checkRever) layout = R.layout.list_item_rever_lay;
                    else layout = R.layout.list_item_lay;

                    int id_tittle = MyDB.getParentId(subjects[position], mas[0]);

                    blf.setButListFragment(subjects[position], subjects[position], mas[0], MyDB.getSubCatNames(
                            subjects[position], mas[0]), layout, checkRever, id_tittle, rootview.getContext());

                    //Log.i("POs", mas[position]);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, blf)
                            .addToBackStack("btl")
                            .commit();
                } else {
                    ListFragment lf = new ListFragment();
                    lf.setTitle(subjects[position], mas);
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            .replace(R.id.parent, lf)
                            .addToBackStack("lf")
                            .commit();
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
        outState.putCharSequenceArray("subj", subjects);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState!=null){
            subjects = savedInstanceState.getStringArray("subj");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            subjects = savedInstanceState.getStringArray("subj");
        }
    }
}
