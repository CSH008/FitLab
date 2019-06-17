package com.jq.app.ui.exercise.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;

import com.jq.app.ui.exercise.adapter.ExerciseItemsAdapter.ViewHolder;

public class ItemMoveCallbackChild extends Callback {
    private final ItemTouchHelperContract mAdapter;

    public interface ItemTouchHelperContract {
        void onRowClear(ViewHolder viewHolder);

        void onRowMoved(int i, int i2);

        void onRowSelected(ViewHolder viewHolder);
    }

    public ItemMoveCallbackChild(ItemTouchHelperContract adapter) {
        this.mAdapter = adapter;
    }

    public boolean isLongPressDragEnabled() {
        return true;
    }

    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    }

    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(3, 0);
    }

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        this.mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != 0 && (viewHolder instanceof ViewHolder)) {
            this.mAdapter.onRowSelected((ViewHolder) viewHolder);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ViewHolder) {
            this.mAdapter.onRowClear((ViewHolder) viewHolder);
        }
    }
}
