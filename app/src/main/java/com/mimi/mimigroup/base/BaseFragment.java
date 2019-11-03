package com.mimi.mimigroup.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends Fragment {
    protected abstract int getLayoutResourceId();
    protected CompositeSubscription subscriptions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        ButterKnife.bind(this, view);
        subscriptions = new CompositeSubscription();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        subscriptions.clear();
    }
}
