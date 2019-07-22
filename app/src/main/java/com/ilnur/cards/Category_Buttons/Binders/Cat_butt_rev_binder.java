package com.ilnur.cards.Category_Buttons.Binders;

import android.view.View;

import com.github.rubensousa.raiflatbutton.RaiflatButton;
import com.github.rubensousa.raiflatbutton.RaiflatImageButton;
import com.ilnur.cards.Category_Buttons.Cat_butt_rev;
import com.ilnur.cards.Fragments.LearnFragment;
import com.ilnur.cards.Fragments.WatchFragment;
import com.ilnur.cards.Fragments.WatchFragment1;
import com.ilnur.cards.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

public class Cat_butt_rev_binder extends TreeViewBinder<Cat_butt_rev_binder.ViewHolder> {

    @Override
    public Cat_butt_rev_binder.ViewHolder provideViewHolder(View itemView) {
        return new Cat_butt_rev_binder.ViewHolder(itemView);
    }

    @Override
    public void bindView(Cat_butt_rev_binder.ViewHolder holder, int i, TreeNode treeNode) {
        Cat_butt_rev cat = (Cat_butt_rev) treeNode.getContent();
        FragmentManager manager = ((AppCompatActivity) cat.getContext()).getSupportFragmentManager();
        holder.learn.setOnClickListener(v -> {
            LearnFragment lf = new LearnFragment();
            lf.setLearnFragment(cat.getSubj(), cat.getTitle(), cat.getId(), false, false);
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_left, R.anim.to_right)
                    .replace(R.id.parent, lf)
                    .addToBackStack(null)
                    .commit();
        });

        holder.watch.setOnClickListener(v -> {
            int id = cat.getId();
            WatchFragment1 wf = new WatchFragment1();
            wf.setWatchFragment(cat.getSubj(), cat.getTitle(), id, false);
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.from_right_web, R.anim.to_left_web)
                    .replace(R.id.parent, wf)
                    .addToBackStack(null)
                    .commit();
        });

        holder.revers.setOnClickListener(v -> {
            LearnFragment lf = new LearnFragment();
            lf.setLearnFragment(cat.getSubj(), cat.getTitle(), cat.getId(), true, false);
            manager.beginTransaction()
                    .replace(R.id.parent, lf)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.cat_butt_rev_lay;
    }
    public class ViewHolder extends TreeViewBinder.ViewHolder{
        RaiflatButton learn;
        RaiflatButton watch;
        RaiflatImageButton revers;

        public ViewHolder(View rootview){
            super(rootview);
            learn = rootview.findViewById(R.id.but_learn);
            watch = rootview.findViewById(R.id.but_watch);
            revers = rootview.findViewById(R.id.but_rev);
        }
    }
}