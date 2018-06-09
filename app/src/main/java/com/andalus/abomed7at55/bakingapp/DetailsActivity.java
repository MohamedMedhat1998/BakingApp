package com.andalus.abomed7at55.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.Recipes.Step;
import com.andalus.abomed7at55.bakingapp.UI.FragmentVideoWithInstructions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity {

    private static final String SHOULD_PLAY_WHEN_READY_KEY = "should_play";

    @BindView(R.id.tv_view_full_description)
    TextView tvShowViewFullDescription;
    @BindView(R.id.simple_exo_player)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.tv_current_step)
    TextView tvCurrentStep;
    @BindView(R.id.btn_next_step)
    Button btnNextStep;
    @BindView(R.id.btn_prev_step)
    Button btnPreviousStep;
    @BindView(R.id.iv_no_video)
    ImageView ivNoVideo;

    private Step currentStep;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    private String videoLink;
    private TrackSelector selector;
    private ArrayList<Step> allSteps;
    private int currentIndex, indexOnRotation;
    private int n , i;
    private long playPosition;
    private int shouldPlayWhenReady = FragmentVideoWithInstructions.PLAY_WHEN_READY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.activity_details);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(MainActivity.getExportableRecipes().get(Integer.parseInt(StepsActivity.getId()) - 1).getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        ButterKnife.bind(this);

        if(savedInstanceState == null){
            currentStep = getIntent().getExtras().getParcelable(getString(R.string.keySelectedStep));
            allSteps = StepsActivity.getExportableSteps();
            i = Integer.parseInt(currentStep.getId());
            n = allSteps.size();
            currentIndex = i;
        }else{
            allSteps = savedInstanceState.getParcelableArrayList(getString(R.string.steps));
            currentIndex = savedInstanceState.getInt(getString(R.string.index));
            indexOnRotation = savedInstanceState.getInt(getString(R.string.index_on_rotation));
            shouldPlayWhenReady = savedInstanceState.getInt(SHOULD_PLAY_WHEN_READY_KEY);
            currentStep = allSteps.get(currentIndex);
            i = currentIndex;
            n = allSteps.size();
            playPosition = savedInstanceState.getLong(getString(R.string.play_position),C.TIME_UNSET);
        }


        if(i == n-1){
            btnNextStep.setBackgroundResource(R.drawable.next_grey);
        }else if(i == 0){
            btnPreviousStep.setBackgroundResource(R.drawable.previous_grey);
        }

        tvCurrentStep.setText(currentStep.getShortDescription());
        checkAndPlay();
        tvShowViewFullDescription.setText(currentStep.getDescription());

    }

    @OnClick(R.id.btn_next_step)
    void onBtnNextClicked(){
        if(currentIndex != allSteps.size()-1){
            currentIndex++;
            if (player != null) {
                player.release();
                player = null;
                selector = null;
            }
            currentStep = allSteps.get(currentIndex);
            checkAndPlay();
            tvShowViewFullDescription.setText(currentStep.getDescription());
            tvCurrentStep.setText(currentStep.getShortDescription());
            fixColors();
        }

    }

    @OnClick(R.id.btn_prev_step)
    void onBtnPreviousClicked(){
        if(currentIndex != 0){
            currentIndex--;
            if (player != null) {
                player.release();
                player = null;
                selector = null;
            }
            currentStep = allSteps.get(currentIndex);
            checkAndPlay();
            tvShowViewFullDescription.setText(currentStep.getDescription());
            tvCurrentStep.setText(currentStep.getShortDescription());
            fixColors();
        }
    }

    /**
     * A special method used to fix the colors of the next and previous buttons
     */
    private void fixColors(){
        if(currentIndex == 0){
            btnPreviousStep.setBackgroundResource(R.drawable.previous_grey);
        }else{
            btnPreviousStep.setBackgroundResource(R.drawable.previous);
        }
        if(currentIndex == allSteps.size()-1){
            btnNextStep.setBackgroundResource(R.drawable.next_grey);
        }else{
            btnNextStep.setBackgroundResource(R.drawable.next);
        }
    }

    /**
     * The main purpose of this method is to fix the visibility when there is no videos to show
     */
    private void checkAndPlay(){
        if(currentStep.getVideoURL().isEmpty()){
            if(!currentStep.getThumbnailURL().isEmpty()){
                if(simpleExoPlayerView.getVisibility()==View.INVISIBLE){
                    simpleExoPlayerView.setVisibility(View.VISIBLE);
                    ivNoVideo.setVisibility(View.INVISIBLE);
                }
                videoLink = currentStep.getThumbnailURL();
                playVideoFromTheInternet(videoLink);
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
            playVideoFromTheInternet(videoLink);
        }
    }

    /**
     * This method is used to play the videos from a given urls. Just pass the video url and the method
     * will do what you expect
     * @param videoUrl the link of the desired video
     */
    private void playVideoFromTheInternet(String videoUrl){
        mediaDataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)),
                (TransferListener<? super DataSource>) bandwidthMeter);

        bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory selectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);

        selector = new DefaultTrackSelector(selectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        DefaultExtractorsFactory defaultExtractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                mediaDataSourceFactory,defaultExtractorsFactory,null,null);

        player = ExoPlayerFactory.newSimpleInstance(this,selector,loadControl);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if(playWhenReady){
                    shouldPlayWhenReady = FragmentVideoWithInstructions.PLAY_WHEN_READY;
                }else {
                    shouldPlayWhenReady = FragmentVideoWithInstructions.DO_NOT_PLAY_WHEN_READY;
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });

        if (playPosition != C.TIME_UNSET && indexOnRotation == currentIndex)
            player.seekTo(playPosition);

        player.prepare(mediaSource);

        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);

        if(shouldPlayWhenReady == FragmentVideoWithInstructions.DO_NOT_PLAY_WHEN_READY && indexOnRotation == currentIndex){
            player.setPlayWhenReady(false);
        }else if(shouldPlayWhenReady == FragmentVideoWithInstructions.PLAY_WHEN_READY && indexOnRotation == currentIndex){
            player.setPlayWhenReady(true);
        }else {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.steps),allSteps);
        outState.putInt(getString(R.string.index),currentIndex);
        outState.putInt(SHOULD_PLAY_WHEN_READY_KEY,shouldPlayWhenReady);
        if (player != null) {
            outState.putLong(getString(R.string.play_position),player.getCurrentPosition());
            outState.putInt(getString(R.string.index_on_rotation), currentIndex);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
            selector = null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == ActionBar.DISPLAY_HOME_AS_UP){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
