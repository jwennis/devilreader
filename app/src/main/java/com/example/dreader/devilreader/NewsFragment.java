package com.example.dreader.devilreader;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.dreader.devilreader.data.StoryContract.StoryEntry;
import com.example.dreader.devilreader.model.Story;
import com.example.dreader.devilreader.sync.StorySyncAdapter;
import com.example.dreader.devilreader.ui.StoryAdapter;


public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String PARAM_FILTER = "PARAM_FILTER";

    private static final int LOADER_ID = 0;

    private StoryAdapter mAdapter;
    private Filter mFilter;

    private ViewGroup layout_root;

    @BindView(R.id.news_recycler)
    RecyclerView recycler;


    public NewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            mFilter = (Filter) savedInstanceState.getSerializable(PARAM_FILTER);

        } else {

            mFilter = Filter.UNREAD;
        }

        layout_root = (ViewGroup) inflater.inflate(R.layout.fragment_news, container, false);

        bindNews();

        return layout_root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager()
                .initLoader(LOADER_ID, null, this).forceLoad();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putSerializable(PARAM_FILTER, mFilter);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.news, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager manager = getActivity().getSupportFragmentManager();
        DialogFragment dialog;

        switch(item.getItemId()) {

            case R.id.action_filter: {

                dialog = new FilterDialogFragment();

                Bundle args = new Bundle();
                args.putSerializable(PARAM_FILTER, mFilter);

                dialog.setArguments(args);
                dialog.setTargetFragment(this, 0);
                dialog.show(manager, FilterDialogFragment.FRGAMENT_TAG);

                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id == LOADER_ID) {

            StringBuilder selectionBuilder = new StringBuilder();
            List<String> argsList = new ArrayList<>();

            if(mFilter == Filter.SAVED) {

                selectionBuilder.append(StoryEntry.COL_IS_SAVED + " > ?");
                argsList.add("0");

            } else if (mFilter == Filter.UNREAD) {

                selectionBuilder.append(StoryEntry.COL_IS_READ + " = ?");
                argsList.add("0");
            }

            String[] projection = new String[] { };
            String selection = selectionBuilder.toString();
            String[] selectionArgs = argsList.toArray(new String[argsList.size()]);
            String sortOrder = String.format("%1$s DESC", StoryEntry.COL_PUBDATE);

            return new CursorLoader(getContext(), StoryEntry.CONTENT_URI,
                    projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId() == LOADER_ID) {

            if(mAdapter == null) {

                mAdapter = new StoryAdapter(data, NewsFragment.class.getSimpleName());
                recycler.setAdapter(mAdapter);

            } else {

                mAdapter.swapCursor(data);
            }

            initSwipeToDismiss();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        if(loader.getId() == LOADER_ID) {

            mAdapter.swapCursor(null);
        }
    }


    private void bindNews() {

        ButterKnife.bind(this, layout_root);

        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(R.string.drawer_news);

        setHasOptionsMenu(true);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());

        StorySyncAdapter.syncImmediately(getContext());
    }


    /**
     * Marks a Story as read in the database and removes from the RecyclerView
     * upon swiping the element from left right
     */
    private void initSwipeToDismiss() {

        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        int pos = viewHolder.getAdapterPosition();

                        Story item = mAdapter.getItemAt(pos);

                        item.markAsRead();

                        ContentValues values = new ContentValues();
                        values.put(StoryEntry.COL_IS_READ, 1);

                        getContext().getContentResolver()
                                .update(StoryEntry.buildUri(item.getKey()), values, null, null);

                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onMove(RecyclerView view, RecyclerView.ViewHolder holder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }
                };

        ItemTouchHelper swipeHelper = new ItemTouchHelper(swipeCallback);
        swipeHelper.attachToRecyclerView(recycler);
    }


    /**
     * Used to filter which stories are shown in the RecyclerView
     */
    private enum Filter {

        ALL(0), SAVED(1), UNREAD(2);

        private final int value;
        private Filter(int v) { this.value = v; }
        public int getValue() { return value; }
    }


    /**
     * Sets the filter criteria and restarts the loader
     *
     * @param filter the chosen filter criteria
     */
    private void applyFilter(Filter filter) {

        if(mFilter != filter) {

            mFilter = filter;

            getActivity().getSupportLoaderManager()
                    .restartLoader(LOADER_ID, null, this);
        }
    }


    /**
     * Creates a popup dialog to choose the filter criteria
     */
    public static class FilterDialogFragment extends DialogFragment {

        public static final String FRGAMENT_TAG = "FilterDialogFragment";

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Filter activeFilter = (Filter) getArguments().getSerializable(PARAM_FILTER);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.action_filter);

            builder.setSingleChoiceItems(R.array.settings_news_filters, activeFilter.getValue(),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int index) {

                            ((NewsFragment) getTargetFragment())
                                    .applyFilter(Filter.values()[index]);

                            dismiss();
                        }
                    });

            return builder.create();
        }
    }
}
