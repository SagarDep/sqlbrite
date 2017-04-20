package com.example.sqlbrite.todo.ui;

import android.support.v7.util.DiffUtil;

import com.example.sqlbrite.todo.db.TodoItem;

import java.util.List;

/**
 * Created by yfcheng on 2017/4/20.
 */

public class TodoItemDiffCallback extends DiffUtil.Callback {
    private List<TodoItem> mOld;
    private List<TodoItem> mNew;

    public TodoItemDiffCallback(List<TodoItem> old, List<TodoItem> aNew) {
        mOld = old;
        mNew = aNew;
    }

    @Override
    public int getOldListSize() {
        return mOld.size();
    }

    @Override
    public int getNewListSize() {
        return mNew.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        TodoItem oldItem = mOld.get(oldItemPosition);
        TodoItem newItem = mNew.get(newItemPosition);
        return oldItem.id() == newItem.id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        TodoItem oldItem = mOld.get(oldItemPosition);
        TodoItem newItem = mNew.get(newItemPosition);
        return oldItem.complete() == newItem.complete() &&
                oldItem.description().equals(newItem.description());
    }
}
