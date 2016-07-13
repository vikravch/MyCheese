/*
 * Copyright (C) 2015 The Android Open Source Project
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

package kistudio.com.cheesemy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheeseListFragment extends Fragment {

    private static final int LIST_SIZE = 30;

    private RecyclerView recyclerViewList;
    private List<Cheese> cheeseList;
    private Context context;

    // cheeseList initialise only once when we construct array list of fragments. We use for that default constructor.
    public CheeseListFragment() {
        LoadListAsyncTask loadListAsyncTask = new LoadListAsyncTask();
        loadListAsyncTask.execute();
    }

    //When fragment attached to activity we save context for using later
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //The main goal of onCreateView - initialise recyclerView when fragment chooses from tabs.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerViewList = (RecyclerView) inflater.inflate(R.layout.fragment_cheese_list, container, false);
        if (cheeseList !=null) setupRecyclerView(cheeseList);
        return recyclerViewList;
    }

    //setupRecyclerView using in onCreateView and onPostExecute (AsyncTask) methods.
    // That's why we must check if fragment was attach to activity. Async task can finish work before fragment attach and then we will have NullPointerExeption
    private void setupRecyclerView(List<Cheese> cheeses) {
        if (isAdded()){
            recyclerViewList.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewList.setAdapter(new SimpleStringRecyclerViewAdapter(context, cheeses));
        }
    }

    //Method starts after onPostExecute. Setting result to cheeseList object.
    private void saveList(List<Cheese> list) {
        this.cheeseList = list;
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Cheese> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Cheese mBoundItem;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public Cheese getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<Cheese> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mBoundItem = mValues.get(position);
            holder.mTextView.setText(holder.mBoundItem.getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CheeseDetailActivity.class);
                    intent.putExtra(CheeseDetailActivity.EXTRA_CHEESE, holder.mBoundItem);

                    context.startActivity(intent);
                }
            });

            Glide.with(holder.mImageView.getContext())
                    .load(holder.mBoundItem.getDrawableResId())
                    .fitCenter()
                    .into(holder.mImageView);
        }

        @Override
        public int getItemCount() {
            if (mValues!=null) return mValues.size();
                else return 0;
        }
    }

    //Async task for loading list using static method CheeseApi.listCheeses()
    private class LoadListAsyncTask extends AsyncTask<Void,Void,List<Cheese>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Cheese> doInBackground(Void... params) {
            List<Cheese> cheeses;
            try {
                cheeses = CheeseApi.listCheeses(LIST_SIZE);
            } catch (IOException e) {
                ArrayList<Cheese> list = new ArrayList<>();
                list.add(new Cheese(R.mipmap.ic_reload, getResources().getString(R.string.error_with_api)));
                cheeses = list;
            }
            return cheeses;
        }

        @Override
        protected void onPostExecute(List<Cheese> cheeses) {
            saveList(cheeses);
            setupRecyclerView(cheeses);
            super.onPostExecute(cheeses);
        }
    }
    
}
