package android.slc.mp.model;

import androidx.lifecycle.ForceExpansionLiveData;
import androidx.lifecycle.Observer;

class PureObserver<T> implements Observer<T> {
    private Observer<T> mObserver;
    private boolean preventNextEvent;

    public PureObserver(Observer<T> observer, int version) {
        mObserver = observer;
        preventNextEvent = version > ForceExpansionLiveData.START_VERSION;
    }

    public Observer<T> getObserver() {
        return mObserver;
    }

    @Override
    public void onChanged(T data) {
        if (preventNextEvent) {
            preventNextEvent = false;
            return;
        }
        if (mObserver != null) {
            mObserver.onChanged(data);
        }
    }
}