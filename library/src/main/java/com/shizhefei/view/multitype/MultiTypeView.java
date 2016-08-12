package com.shizhefei.view.multitype;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by LuckyJayce on 2016/8/11.
 */
public class MultiTypeView extends FrameLayout {
    private RecyclerView recyclerView;
    private TypeAdapter<?> typeAdapter;
    private MyRecycledViewPool recycledViewPool;

    public MultiTypeView(Context context) {
        super(context);
        init(context);
    }

    public MultiTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        recyclerView = new RecyclerView(context);
        addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setRecycledViewPool(recycledViewPool = new MyRecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public MyRecycledViewPool getRecycledViewPool() {
        return recycledViewPool;
    }

    public void setAdapter(TypeAdapter<?> adapter) {
        if (typeAdapter != null) {
            typeAdapter.unregisterAdapterDataObserver(dataObserver);
        }
        this.typeAdapter = adapter;
        typeAdapter.registerAdapterDataObserver(dataObserver);
        this.recyclerView.setAdapter(typeAdapter);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return recyclerView.getLayoutManager();
    }

    public TypeAdapter<?> getAdapter() {
        return typeAdapter;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle stateBundle = new Bundle();
        TypeAdapter adapter = getAdapter();
//        if (adapter != null) {
//            adapter.onSaveInstanceState(this, stateBundle);
//        }
        Parcelable superParcelable = super.onSaveInstanceState();
        stateBundle.putParcelable("superParcelable", superParcelable);
        return stateBundle;
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle stateBundle = (Bundle) state;
            TypeAdapter adapter = getAdapter();
//            if (adapter != null) {
//                adapter.onRestoreInstanceState(this, stateBundle);
//            }

            Parcelable parcelable = stateBundle.getParcelable("superParcelable");
            super.onRestoreInstanceState(parcelable);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            dataChange();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            dataChange();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            dataChange();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            dataChange();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            dataChange();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            dataChange();
        }

        private void dataChange() {

        }
    };


    /**
     * RecycledViewPool lets you share Views between multiple RecyclerViews.
     * <p/>
     * If you want to recycle views across RecyclerViews, create an instance of RecycledViewPool
     * and use {@link RecyclerView#setRecycledViewPool(RecyclerView.RecycledViewPool)}.
     * <p/>
     * RecyclerView automatically creates a pool for itself if you don't provide one.
     */
    static class MyRecycledViewPool extends RecyclerView.RecycledViewPool {
        private SparseArray<RecyclerView.ViewHolder> fragmentViewHolders = new SparseArray<>();

        public void putFragmentViewHolder(int viewType, RecyclerView.ViewHolder viewHolder) {
            fragmentViewHolders.put(viewType, viewHolder);
        }

        @Override
        public RecyclerView.ViewHolder getRecycledView(int viewType) {
            RecyclerView.ViewHolder viewHolder = fragmentViewHolders.get(viewType);
            if (viewHolder != null) {
                View view = viewHolder.itemView;
                ViewParent parent = view.getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup) parent).removeView(view);
                }
                view.setVisibility(View.VISIBLE);
                fragmentViewHolders.remove(viewType);
                return viewHolder;
            }
            return super.getRecycledView(viewType);
        }
    }

}
