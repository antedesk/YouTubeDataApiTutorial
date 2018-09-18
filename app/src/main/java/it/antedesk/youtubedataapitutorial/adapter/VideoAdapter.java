package it.antedesk.youtubedataapitutorial.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.youtubedataapitutorial.R;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder>{

    private List<String> videoList;
    private final String TAG = VideoAdapter.class.getName();
    private final VideoAdapterOnClickHandler mClickHandler;
    private String mYouTubeApiKey;

    public VideoAdapter(VideoAdapterOnClickHandler mClickHandler, String mYouTubeApiKey) {
        this.mClickHandler = mClickHandler;
        this.mYouTubeApiKey = mYouTubeApiKey;
    }

    public interface VideoAdapterOnClickHandler {
        void onClick(String selectedTrailer);
    }

    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context parentContex = parent.getContext();
        int layoutIdForListItem = R.layout.video_item;
        LayoutInflater inflater = LayoutInflater.from(parentContex);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new VideoAdapter.VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapterViewHolder holder, int position) {
        final int currentPosition = position;

        holder.mYouTubeThumbnailView.initialize(mYouTubeApiKey, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(videoList.get(currentPosition));

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Log.e(TAG, "Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(TAG, "Youtube Initialization Failure");
            }
        });

    }

    @Override
    public int getItemCount() {
        if (videoList == null || videoList.isEmpty()) return 0;
        return videoList.size();
    }


    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.youtube_thumbnail) YouTubeThumbnailView mYouTubeThumbnailView;
        @BindView(R.id.video_cv)
        CardView mVideoCardView;

        public VideoAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int selectedPosition = getAdapterPosition();
            Log.d(TAG, "new position: "+ selectedPosition);
            String selectedTrailer = videoList.get(selectedPosition);
            mClickHandler.onClick(selectedTrailer);
        }
    }
    public void setVideosData(List<String> trailersData) {
        videoList = trailersData;
        notifyDataSetChanged();
    }
}
