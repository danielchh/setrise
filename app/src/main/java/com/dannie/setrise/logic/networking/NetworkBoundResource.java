package com.dannie.setrise.logic.networking;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public abstract class NetworkBoundResource<ResultType> {

    private final MutableLiveData<Resource<ResultType>> result = new MutableLiveData<>();

    @MainThread
    public NetworkBoundResource() {
        result.postValue(Resource.loading(null));
        fetchFromNetwork();
    }

    private void fetchFromNetwork() {
        LiveData<ApiResponse<ResultType>> apiResponse = createCall();
        apiResponse.observeForever(new Observer<ApiResponse<ResultType>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<ResultType> resultTypeApiResponse) {
                if (resultTypeApiResponse != null && resultTypeApiResponse.isSuccessful()) {
                    AppExecutors.diskIO().execute(() -> {
                        saveCallResult(processResponse(resultTypeApiResponse));
                        AppExecutors.mainThread().execute(() ->
                                result.setValue(Resource.success(resultTypeApiResponse.getData())));
                    });
                } else {
                    result.setValue(Resource.error("Unexpected error occurred", null, null));
                }
                apiResponse.removeObserver(this);
            }
        });
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    private ResultType processResponse(ApiResponse<ResultType> response) {
        return response.getData();
    }

    /**
     * Used to save call result to db, not using it in this project
     *
     * @param item
     */
    @WorkerThread
    private void saveCallResult(@NonNull ResultType item) {
    }

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<ResultType>> createCall();
}
