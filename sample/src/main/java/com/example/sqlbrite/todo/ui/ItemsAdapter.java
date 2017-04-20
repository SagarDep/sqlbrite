/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sqlbrite.todo.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.example.sqlbrite.todo.db.TodoItem;
import com.jakewharton.rxbinding.view.RxView;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

final class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> implements Action1<List<TodoItem>> {
    private final LayoutInflater inflater;

    private List<TodoItem> items = Collections.emptyList();
    private BriteDatabase db;

    public ItemsAdapter(Context context, BriteDatabase db) {
        inflater = LayoutInflater.from(context);
        this.db = db;
    }

    @Override
    public void call(List<TodoItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    public TodoItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(android.R.layout.simple_list_item_multiple_choice, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoItem item = getItem(position);
        CheckedTextView textView = (CheckedTextView) holder.itemView;
        textView.setChecked(item.complete());

        CharSequence description = item.description();
        if (item.complete()) {
            SpannableString spannable = new SpannableString(description);
            spannable.setSpan(new StrikethroughSpan(), 0, description.length(), 0);
            description = spannable;
        }

        textView.setText(description);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            RxView.clicks(itemView) //
                    .observeOn(Schedulers.io())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            TodoItem item = getItem(getAdapterPosition());
                            boolean newValue = !item.complete();
                            db.update(TodoItem.TABLE, new TodoItem.Builder().complete(newValue).build(),
                                    TodoItem.ID + " = ?", String.valueOf(item.id()));
                        }
                    });
        }
    }
}
