package com.common.android.utils.ui.recyclerView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.common.android.utils.extensions.ClassExtensions;
import com.common.android.utils.interfaces.LogTag;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jan Rabe on 09/09/15.
 */
public class PresenterAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LogTag {

    @NonNull
    private final ArrayList<Pair<T, Class>> data;

    @Nullable
    private OnItemClickListener<T> onItemClickListener;

    @Nullable
    private OnItemFocusChangeListener<T> onItemFocusChangeListener;

    @Nullable
    private View.OnKeyListener onKeyListener;

    @NonNull
    private List<Presenter<T, ?>> binderType;
    public RecyclerView recyclerView;

    public PresenterAdapter() {
        this.data = new ArrayList<>();
        this.binderType = new ArrayList<>();
    }

    protected <VH extends RecyclerView.ViewHolder> void addBinder(@NonNull final Presenter<T, VH> binder) {
        binderType.add(binder);
    }

    @Nullable
    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(@Nullable final OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Nullable
    public OnItemFocusChangeListener<T> getOnItemFocusChangeListener() {
        return onItemFocusChangeListener;
    }

    public void setOnItemFocusChangeListener(@Nullable final OnItemFocusChangeListener<T> onItemFocusChangeListener) {
        this.onItemFocusChangeListener = onItemFocusChangeListener;
    }

    @Nullable
    public View.OnKeyListener getOnKeyListener() {
        return onKeyListener;
    }

    public void setOnKeyListener(@Nullable final View.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return getDataBinder(viewType).onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
        getPosition(position).bindViewHolder(viewHolder, get(position), position);
    }

    public void add(final int index, @NonNull final T t, @NonNull final Class clazz) {
        data.add(index, new Pair<>(t, clazz));
        addIfNotExists(clazz);
        notifyItemInserted(index);
    }

    public void add(@NonNull final T t, @NonNull final Class clazz) {
        data.add(new Pair<>(t, clazz));
        addIfNotExists(clazz);
        notifyItemInserted(data.size() - 1);
    }

    @SuppressWarnings("unchecked")
    private void addIfNotExists(@NonNull final Class clazz) {
        for (final Presenter<T, ?> binderType : this.binderType)
            if (ClassExtensions.equals(binderType.getClass(), clazz))
                return;

        final Constructor<T> constructor = (Constructor<T>) clazz.getConstructors()[0];
        Presenter<T, ?> instance = null;
        try {
            instance = (Presenter<T, ?>) constructor.newInstance(this);
            binderType.add(instance);
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }

        if (instance == null)
            throw new IllegalArgumentException(clazz.getCanonicalName() + " has no constructor with parameter: " + getClass().getCanonicalName());
    }

    public T get(final int position) {
        return data.get(position).first;
    }

    public int getItemViewType(final int position) {
        for (int i = 0; i < binderType.size(); ++i)
            if (ClassExtensions.equals(data.get(position).second, binderType.get(i).getClass()))
                return i;

        return 0;
    }

    private Presenter<T, ? extends RecyclerView.ViewHolder> getDataBinder(final int viewType) {
        return binderType.get(viewType);
    }


    private Presenter getPosition(final int position) {
        return binderType.get(getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        binderType.clear();
        data.clear();
        notifyDataSetChanged();
    }

    public void removeAllViews() {
        if (recyclerView != null)
            recyclerView.removeAllViews();
    }

    @NonNull
    @Override
    final public String tag() {
        return getClass().getSimpleName();
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}