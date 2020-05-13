package com.evans.sampleimpl;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("journeys/{journeyId}/sections/level/{levelId}")
    Call<List<Section>> getLevelSections(
            @Path("journeyId") String journeyId,
            @Path("levelId") String levelId
    );

    @GET("sections/{sectionId}/topics")
    Call<List<Topic>> getSectionTopics(@Path("sectionId") String sectionId);
}
