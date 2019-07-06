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
import com.ilnur.cards.Adapters.AdapterList;
import com.ilnur.cards.Fragments.ButListFragment;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;


import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class ListFragment extends Fragment {
    private String title; // subject
    private String[] mas;

    public void setTitle(String title, String[] mas) {
        this.title = title;
        this.mas = mas;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.list_fragment, container, false);
        //getActivity().setTitle(title);

        Toolbar bar = Toolbar.class.cast(getActivity().findViewById(R.id.toolbar));
        CollapsingToolbarLayout col = CollapsingToolbarLayout.class.cast(getActivity().findViewById(R.id.collapsing_toolbar));
        col.setTitle(title);
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

        if (!MyDB.isSubjAdded(title))
            Toast.makeText(rootview.getContext(), "Некоторые темы все еще добавляются", Toast.LENGTH_SHORT).show();

        GridView grid = rootview.findViewById(R.id.list);
        //final String[] mas = MyDB.getCatNames(title);
        //Arrays.sort(mas);
        AdapterList adapter = new AdapterList(rootview.getContext(), mas);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(rootview.getContext(), android.R.layout.simple_list_item_1, mas);

        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.exit = false;
                ButListFragment blf = new ButListFragment();

                int layout;
                boolean checkRever = MyDB.checkRevers(title, mas[position]);
                if (checkRever) layout = R.layout.list_item_rever_lay;
                else layout = R.layout.list_item_lay;

                int id_tittle = MyDB.getParentId(title, mas[position]);

                blf.setButListFragment(title, title, mas[position], MyDB.getSubCatNames(
                        title, mas[position]), layout, checkRever, id_tittle, rootview.getContext());

                //Log.i("POs", mas[position]);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                        .replace(R.id.parent, blf)
                        .addToBackStack(null)
                        .commit();

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
}
