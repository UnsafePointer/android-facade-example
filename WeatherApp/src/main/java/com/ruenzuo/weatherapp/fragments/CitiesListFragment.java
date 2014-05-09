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
import com.ruenzuo.weatherapp.adapters.CitiesAdapter;
import com.ruenzuo.weatherapp.managers.WeatherAppManager;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Country;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by ruenzuo on 09/05/14.
 */
public class CitiesListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private Country country;
    private CitiesListFragmentListener listener;
    private SwipeRefreshLayout swipeContainer;

    public interface CitiesListFragmentListener {
        public void onCitySeleted(City city);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CitiesListFragmentListener) {
            listener = (CitiesListFragmentListener)activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement CitiesListFragment.CitiesListFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);
        CitiesAdapter adapter = new CitiesAdapter(getActivity(), R.layout.row_city);
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
        CitiesAdapter citiesAdapter = (CitiesAdapter) getListAdapter();
        listener.onCitySeleted(citiesAdapter.getItem(position));
    }

    private void refresh() {
        final long startTime = System.currentTimeMillis();
        WeatherAppManager.INSTANCE.getCities(country.getCode()).continueWith(new Continuation<City[], Void>() {

            @Override
            public Void then(Task<City[]> task) throws Exception {
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                Log.i("WeatherApp", String.valueOf(elapsedTime));
                if (!task.isFaulted()) {
                    swipeContainer.setRefreshing(false);
                    CitiesAdapter citiesAdapter = (CitiesAdapter) getListAdapter();
                    citiesAdapter.clear();
                    citiesAdapter.addAll(task.getResult());
                    citiesAdapter.notifyDataSetChanged();
                }
                return null;
            }

        }, Task.UI_THREAD_EXECUTOR);
    }

}
