package com.evans.sampleimpl;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SectionViewModel extends AndroidViewModel {

    private LiveData<SectionResponse> mSectionResponse;
    private SectionRepo mSectionRepo;

    public SectionViewModel(@NonNull Application application) {
        super(application);
        mSectionRepo = new SectionRepo();
    }

    void getSections(String journeyId, String levelId){
        mSectionResponse = mSectionRepo.getLevelSections(journeyId, levelId);
    }

    LiveData<SectionResponse> getSectionResponse() {
        return mSectionResponse;
    }
}
