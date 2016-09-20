package ch.epfl.sweng.onebeat.Network;

/**
 * Created by Matthieu on 01.12.2015.
 */
public interface DataProviderObserver<T> {
    void onDataReception(T data, DataProvider.RequestTypes requestTypes);
}
