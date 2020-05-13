package com.evans.sampleimpl;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionHolder> {

    private List<Section> mSections;
    private ItemInteraction mItemInteraction;

    public SectionAdapter(ItemInteraction itemInteraction) {
        mItemInteraction = itemInteraction;
    }

    @NonNull
    @Override
    public SectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.section_item, parent, false);
        return new SectionHolder(view, mItemInteraction);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionHolder holder, int position) {
        holder.bind(mSections.get(position));
    }

    @Override
    public int getItemCount() {
        return mSections.size();
    }

    public void setData(List<Section> sections) {
        mSections = sections;
        notifyDataSetChanged();
    }

    class SectionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemInteraction mItemInteraction;
        CircleImageView sectionImg;
        TextView sectionTitle;
        ImageView sectionThumbnail, playBtn;

        SectionHolder(@NonNull View itemView, ItemInteraction itemInteraction) {
            super(itemView);
            mItemInteraction = itemInteraction;
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
            sectionImg = itemView.findViewById(R.id.sectionImg);
            sectionThumbnail = itemView.findViewById(R.id.sectionThumbnail);
            playBtn = itemView.findViewById(R.id.playBtn);
            sectionThumbnail.setOnClickListener(this);
            playBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemInteraction.sectionClick(mSections.get(getAdapterPosition()), v);
        }

        void bind(Section section) {
            sectionTitle.setText(section.getTitle());
        }
    }

    public interface ItemInteraction {

        void sectionClick(Section section, View view);
    }
}
