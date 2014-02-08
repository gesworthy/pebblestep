package com.gesworthy.pebblepedometer;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Gary on 1/22/14.
 */
public class MainFragment extends Fragment {

    private static TextView countText;
    private StepMessage messageReceiver = new StepMessage();

    public MainFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Grab the count view
        countText = (TextView) rootView.findViewById(R.id.count);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(messageReceiver, new IntentFilter("steps"));
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(messageReceiver);
    }

    public static class StepMessage extends BroadcastReceiver {
        public StepMessage() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int steps = intent.getExtras().getInt("steps");
            countText.setText(String.valueOf(steps));
        }
    }
}
