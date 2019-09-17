package com.ilnur.cards.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.ilnur.cards.Fragments.ButListFragment;
import com.ilnur.cards.Fragments.ListFragment;
import com.ilnur.cards.MainActivity;
import com.ilnur.cards.MyDB;
import com.ilnur.cards.R;
import com.ilnur.cards.forStateSaving.ActivityState;
import com.ilnur.cards.forStateSaving.FragmentState;
import com.ilnur.cards.forStateSaving.btn;
import com.ilnur.cards.forStateSaving.list;

public class SubjAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] mas1;
    private final int[] imgs = {
            R.drawable.rus, R.drawable.phys,
            R.drawable.mathic, R.drawable.bio,
            R.drawable.hist
    };
    private MyDB db;
    private MainActivity main;
    private Bundle savedInstanceState;

    public SubjAdapter(Context mContext, String[] mas, MyDB db,
                       MainActivity main, Bundle savedInstanceState){
        this.mContext = mContext;
        this.mas1 = mas;
        this.db = db;
        this.main = main;
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItem = convertView;
        if (gridItem == null)
            gridItem = LayoutInflater.from(mContext).inflate(R.layout.subj_grid_lay, parent, false);

        TextView text = gridItem.findViewById(R.id.grid_text);
        ImageView image = gridItem.findViewById(R.id.grid_image);
        text.setText(mas1[position]);
        image.setImageResource(imgs[position]);

        CardView card = gridItem.findViewById(R.id.card_grid);
        card.setOnClickListener(v -> {
            MainActivity.main.exit = false;

            String[] mas = db.getCatNames(mas1[position]);
            if (mas.length == 0){
                Toast.makeText(mContext, "Этот предмет все еще добавляется", Toast.LENGTH_SHORT).show();
                //MyDB.addCurrent(subjects[position]);
            } else {
                main.enableBackBtn(true);
                if (mas.length == 1 && db.isSubjAdded(mas[0])){
                    //MainActivity.exit = false;
                    ButListFragment blf = new ButListFragment();
                    boolean checkRever = db.checkRevers(mas1[position], mas[0]);
                    int id_tittle = db.getParentId(mas1[position], mas[0]);
                    btn btn = new btn(mas1[position], id_tittle, mas[0], db.getSubCatNames(
                            mas1[position], mas[0]), checkRever);

                    blf.setBtn(db,btn);
                    blf.setArguments(savedInstanceState);

                    main.getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            //.setReorderingAllowed(true)
                            .replace(R.id.parent, blf)
                            .addToBackStack("btn")
                            .commit();

                    MainActivity.main.addFragment(new FragmentState("btn", new Gson().toJson(btn)));
                    db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
                    //MainActivity.appState.activities[0] = Ma
                } else {
                    ListFragment lf = new ListFragment();
                    list listState = new list(mas1[position], mas);
                    lf.setTitle(db, listState);
                    lf.setArguments(savedInstanceState);

                    main.getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                            //.setReorderingAllowed(true)
                            .replace(R.id.parent, lf)
                            .addToBackStack("list")
                            .commit();
                    MainActivity.main.addFragment(new FragmentState("list", new Gson().toJson(listState)));
                    db.updateActState(new ActivityState("main", new Gson().toJson(MainActivity.main)));
                }
            }
        });

        return gridItem;
    }


    @Override
    public int getCount() {
        return mas1.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
