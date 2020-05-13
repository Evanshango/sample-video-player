package com.evans.sampleimpl;

import java.util.List;

public class SectionResponse {

    private List<Section> mSections;
    private Throwable mThrowable;

    public SectionResponse(List<Section> sections) {
        mSections = sections;
        mThrowable = null;
    }

    public SectionResponse(Throwable throwable) {
        mThrowable = throwable;
        mSections = null;
    }

    public List<Section> getSections() {
        return mSections;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }
}
