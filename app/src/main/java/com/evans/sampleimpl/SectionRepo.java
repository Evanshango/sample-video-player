package com.evans.sampleimpl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionRepo {

    private Api mApi;

    SectionRepo() {
        mApi = ApiService.getApiClient();
    }

    LiveData<SectionResponse> getLevelSections(String journeyId, String levelId){
        final MutableLiveData<SectionResponse> sectionResponse = new MutableLiveData<>();
        mApi.getLevelSections(journeyId, levelId).enqueue(new Callback<List<Section>>() {
            @Override
            public void onResponse(Call<List<Section>> call, Response<List<Section>> response) {
                if (response.isSuccessful() && response.body() != null){
                    sectionResponse.postValue(new SectionResponse(response.body()));
                } else {
                    sectionResponse.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Section>> call, Throwable t) {
                sectionResponse.postValue(new SectionResponse(t));
            }
        });
        return sectionResponse;
    }
}
