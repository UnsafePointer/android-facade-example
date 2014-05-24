package com.ruenzuo.weatherapp.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ruenzuo.weatherapp.R;
import com.ruenzuo.weatherapp.adapters.CountriesAdapter;
import com.ruenzuo.weatherapp.managers.WeatherAppManager;
import com.ruenzuo.weatherapp.models.Country;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by ruenzuo on 07/05/14.
 */
public class CountriesListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private CountriesListFragmentListener listener;
    private SwipeRefreshLayout swipeContainer;

    public interface CountriesListFragmentListener {
        public void onCountrySelected(Country country);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CountriesListFragmentListener) {
            listener = (CountriesListFragmentListener)activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement CountriesListFragment.CountriesListFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countries, container, false);
        CountriesAdapter adapter = new CountriesAdapter(getActivity(), R.layout.row_country);
        setListAdapter(adapter);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark,
                android.R.color.holo_blue_bright,
                android.R.color.holo_blue_dark);
        swipeContainer.setRefreshing(true);
        refresh();
        return view;
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CountriesAdapter countriesAdapter = (CountriesAdapter) getListAdapter();
        listener.onCountrySelected(countriesAdapter.getItem(position));
    }

    private void refresh() {
        final long startTime = System.currentTimeMillis();
        WeatherAppManager.INSTANCE.getCountries().continueWith(new Continuation<Country[], Void>() {

            @Override
            public Void then(Task<Country[]> task) throws Exception {
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                Log.i("WeatherApp", String.valueOf(elapsedTime));
                if (!task.isFaulted()) {
                    Country[] countries = task.getResult();
                    WeatherAppManager.INSTANCE.startSyncService(countries);
                    swipeContainer.setRefreshing(false);
                    CountriesAdapter countriesAdapter = (CountriesAdapter) getListAdapter();
                    countriesAdapter.clear();
                    countriesAdapter.addAll(countries);
                    countriesAdapter.notifyDataSetChanged();
                }
                return null;
            }

        }, Task.UI_THREAD_EXECUTOR);
    }

}
