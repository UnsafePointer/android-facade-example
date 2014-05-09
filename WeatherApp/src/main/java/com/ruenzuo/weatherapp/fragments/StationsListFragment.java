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
import com.ruenzuo.weatherapp.adapters.StationsAdapter;
import com.ruenzuo.weatherapp.managers.WeatherAppManager;
import com.ruenzuo.weatherapp.models.City;
import com.ruenzuo.weatherapp.models.Station;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by ruenzuo on 09/05/14.
 */
public class StationsListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {

    private City city;
    private StationsListFragmentListener listener;
    private SwipeRefreshLayout swipeContainer;

    public interface StationsListFragmentListener {
        public void onStationSeleted(Station station);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof StationsListFragmentListener) {
            listener = (StationsListFragmentListener)activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement StationsListFragment.StationsListFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        StationsAdapter adapter = new StationsAdapter(getActivity(), R.layout.row_station);
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
        StationsAdapter stationsAdapter = (StationsAdapter) getListAdapter();
        listener.onStationSeleted(stationsAdapter.getItem(position));
    }

    private void refresh() {
        final long startTime = System.currentTimeMillis();
        WeatherAppManager.INSTANCE.getStations(city).continueWith(new Continuation<Station[], Void>() {

            @Override
            public Void then(Task<Station[]> task) throws Exception {
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                Log.i("WeatherApp", String.valueOf(elapsedTime));
                if (!task.isFaulted()) {
                    swipeContainer.setRefreshing(false);
                    StationsAdapter stationsAdapter = (StationsAdapter) getListAdapter();
                    stationsAdapter.clear();
                    stationsAdapter.addAll(task.getResult());
                    stationsAdapter.notifyDataSetChanged();
                }
                return null;
            }

        }, Task.UI_THREAD_EXECUTOR);
    }

}
