package com.andalus.abomed7at55.bakingapp.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.R;
import com.andalus.abomed7at55.bakingapp.Recipes.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

/**
 * This class represents the details (video with instruction) fragment
 */

public class FragmentVideoWithInstructions extends Fragment {

    public static final int FLAG_NORMAL = 1;
    public static final int FLAG_TABLET = 10;
    private int mFlag = FLAG_NORMAL;
    private Step currentStep;

    private TextView tvStepDescription;
    private SimpleExoPlayerView simpleExoPlayerView;
    private ImageView ivNoVideo;

    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    private String videoLink;
    private TrackSelector selector;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_with_instruction,container,false);

        if(mFlag == FLAG_TABLET){
            tvStepDescription = view.findViewById(R.id.tv_view_full_description);
            tvStepDescription.setText(currentStep.getDescription());

            simpleExoPlayerView = view.findViewById(R.id.simple_exo_player);
            ivNoVideo = view.findViewById(R.id.iv_no_video);
            checkAndPlay(view.getContext());

        }

        return view;
    }

    public void setFlag(int flag) {
        mFlag = flag;
    }

    public void setSelectedStep(Step selectedStep) {
        this.currentStep = selectedStep;
    }

    /**
     * Use this method to update the content of the fragment instead of recreating it
     * @param selectedStep the new step
     */
    public void updateContent(Step selectedStep){
        if(!selectedStep.getId().equals(currentStep.getId())){
            tvStepDescription.setText(selectedStep.getDescription());
            setSelectedStep(selectedStep);
            checkAndPlay(view.getContext());
        }
    }

    /**
     * The main purpose of this method is to fix the visibility when there is no videos to show
     * @param context the context of the fragment
     */
    private void checkAndPlay(Context context){
        if(currentStep.getVideoURL().isEmpty()){
            if(!currentStep.getThumbnailURL().isEmpty()){
                if(simpleExoPlayerView.getVisibility()==View.INVISIBLE){
                    simpleExoPlayerView.setVisibility(View.VISIBLE);
                    ivNoVideo.setVisibility(View.INVISIBLE);
                }
                videoLink = currentStep.getThumbnailURL();
                playVideoFromTheInternet(videoLink,context);
            }else{
                simpleExoPlayerView.setVisibility(View.INVISIBLE);
                ivNoVideo.setVisibility(View.VISIBLE);
            }
        }else {
            if(simpleExoPlayerView.getVisibility()==View.INVISIBLE){
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                ivNoVideo.setVisibility(View.INVISIBLE);
            }
            videoLink = currentStep.getVideoURL();
            playVideoFromTheInternet(videoLink,context);
        }
    }

    /**
     * This method is used to play the videos from a given urls. Just pass the video url and the method
     * will do what you expect
     * @param videoUrl the link of the desired video
     * @param context the fragment Context
     */
    private void playVideoFromTheInternet(String videoUrl,Context context){
        mediaDataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, getString(R.string.app_name)),
                (TransferListener<? super DataSource>) bandwidthMeter);

        bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory selectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);

        selector = new DefaultTrackSelector(selectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        DefaultExtractorsFactory defaultExtractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                mediaDataSourceFactory,defaultExtractorsFactory,null,null);

        player = ExoPlayerFactory.newSimpleInstance(context,selector,loadControl);
        /*
        if (playPosition != C.TIME_UNSET && indexOnRotation == currentIndex)
            player.seekTo(playPosition); */

        player.prepare(mediaSource);

        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(true);
    }
}
