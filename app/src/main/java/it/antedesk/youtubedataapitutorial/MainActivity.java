package it.antedesk.youtubedataapitutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.youtubedataapitutorial.adapter.VideoAdapter;


public class MainActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener,
        VideoAdapter.VideoAdapterOnClickHandler {

    private static final int RECOVERY_REQUEST = 1;
    private final List<String> videos =
            Arrays.asList("20bpjtCbCz0", "bI31WqFDxNs", "D86RtevtfrA", "xZNBFcwd7zc", "2-5Wv9UGkN8", "Z5ezsReZcxU");

    private YouTubePlayerSupportFragment mYuoTubePlayerFrag;
    private YouTubePlayer mYouTubePlayer;


    @BindView(R.id.video_rv)
    RecyclerView mVideoRecyclerView;
    private VideoAdapter mVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // finding UI elements
        mYuoTubePlayerFrag =
                (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);

        LinearLayoutManager trailerLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // setting the layoutManager on mRecyclerView
        mVideoRecyclerView.setLayoutManager(trailerLayoutManager);
        mVideoRecyclerView.setHasFixedSize(true);
        mVideoAdapter = new VideoAdapter(this, getString(R.string.youtube_data_api_key));
        mVideoAdapter.setVideosData(videos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mYuoTubePlayerFrag.initialize(getString(R.string.youtube_data_api_key), this);
        mVideoRecyclerView.setAdapter(mVideoAdapter);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,
                                        boolean wasRestored) {
        if (!wasRestored) {
            mYouTubePlayer = youTubePlayer;
            youTubePlayer.cueVideo(videos.get(0));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_REQUEST) {
            mYuoTubePlayerFrag =
                    (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        }
    }

    @Override
    public void onClick(String selectedTrailer) {
        if (mYuoTubePlayerFrag != null && mYouTubePlayer != null) {
            //load the selected video
            mYouTubePlayer.cueVideo(selectedTrailer);
        }
    }
}
