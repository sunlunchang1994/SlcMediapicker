package android.slc.mp.model;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.lifecycle.ForceExpansionLiveData;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.slc.mp.po.i.IBaseItem;

import java.util.List;
import java.util.Map;

/**
 * @author slc
 * @date 2019/11/29 14:30
 */
class BaseMpModel<T> extends ForceExpansionLiveData<T> {
    protected Map<Observer, PureObserver> observerArrayMap = new ArrayMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (observerArrayMap.containsKey(observer)) {
            return;
        }
        PureObserver<? super T> pureObserver = new PureObserver<>(observer, getVersion());
        observerArrayMap.put(observer, pureObserver);
        super.observe(owner, pureObserver);
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        if (observerArrayMap.containsKey(observer)) {
            return;
        }
        PureObserver<? super T> pureObserver = new PureObserver<>(observer, getVersion());
        observerArrayMap.put(observer, pureObserver);
        super.observeForever(pureObserver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        if (observer instanceof PureObserver) {
            PureObserver<? super List<IBaseItem>> pureObserver = (PureObserver) observer;
            observerArrayMap.remove(pureObserver.getObserver());
            super.removeObserver(observer);
        } else {
            PureObserver pureObserver = observerArrayMap.remove(observer);
            if (pureObserver != null) {
                super.removeObserver(pureObserver);
            } else {
                super.removeObserver(observer);
            }
        }
    }
}
