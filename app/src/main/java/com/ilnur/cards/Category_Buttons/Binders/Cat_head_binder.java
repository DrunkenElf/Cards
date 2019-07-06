package com.ilnur.cards.Category_Buttons.Binders;

import android.view.View;
import android.widget.TextView;

import com.ilnur.cards.Category_Buttons.Cat_head;
import com.ilnur.cards.R;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

public class Cat_head_binder extends TreeViewBinder<Cat_head_binder.ViewHolder> {
    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, TreeNode node) {
        Cat_head fileNode = (Cat_head) node.getContent();
        holder.tvName.setText(fileNode.getTitle());
    }

    @Override
    public int getLayoutId() {
        return R.layout.cat_head_lay;
    }

    public class ViewHolder extends TreeViewBinder.ViewHolder {
        public TextView tvName;

        public ViewHolder(View rootView) {
            super(rootView);
            this.tvName = (TextView) rootView.findViewById(R.id.cat_name);
        }

    }
}
