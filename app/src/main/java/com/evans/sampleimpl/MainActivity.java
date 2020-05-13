package com.evans.sampleimpl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SectionAdapter.ItemInteraction {

    private SectionViewModel mSectionViewModel;
    private List<Section> mSections = new ArrayList<>();
    private RecyclerView sectionRecycler;
    private SectionAdapter mSectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionViewModel = new ViewModelProvider(this).get(SectionViewModel.class);
        sectionRecycler = findViewById(R.id.sectionRecycler);

        mSectionAdapter = new SectionAdapter(this);

        getSections();
    }

    private void populateRecycler(List<Section> sections) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        sectionRecycler.setHasFixedSize(true);
        sectionRecycler.setLayoutManager(manager);

        mSectionAdapter.setData(sections);
        sectionRecycler.setAdapter(mSectionAdapter);
    }

    private void getSections() {
        mSectionViewModel.getSections("1000", "1");
        mSectionViewModel.getSectionResponse().observe(this, sectionResponse -> {
            if (sectionResponse.getThrowable() == null){
                mSections.addAll(sectionResponse.getSections());
                populateRecycler(mSections);
            } else {
                Toast.makeText(MainActivity.this, sectionResponse.getThrowable().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void sectionClick(Section section, View view) {
        switch (view.getId()){
            case R.id.playBtn:
                previewSection(section);
                break;
            case R.id.sectionThumbnail:
                openTopics(section);
                break;
        }
    }

    private void openTopics(Section section) {
        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra("sectionId", section.getSectionId());
        intent.putExtra("sectionTitle", section.getTitle());
        startActivity(intent);
    }

    private void previewSection(Section section) {
        Toast.makeText(this, "previewing " + section.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
