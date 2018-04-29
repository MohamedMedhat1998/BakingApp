package com.andalus.abomed7at55.bakingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;
import com.andalus.abomed7at55.bakingapp.Recipes.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity {

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

    private Step currentStep;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    private String videoLink;
    private TrackSelector selector;
    private ArrayList<Step> allSteps;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //TODO Support onSaveInstanceState
        ButterKnife.bind(this);

        currentStep = getIntent().getExtras().getParcelable(getString(R.string.keySelectedStep));

        allSteps = StepsActivity.getExportableSteps();
        int i = Integer.parseInt(currentStep.getId());
        int n = allSteps.size();
        currentIndex = i;

        if(i == n-1){
            btnNextStep.setBackgroundResource(R.drawable.next_grey);
        }else if(i == 0){
            btnPreviousStep.setBackgroundResource(R.drawable.previous_grey);
        }

        tvCurrentStep.setText(currentStep.getShortDescription());

        tvShowViewFullDescription.setText(currentStep.getDescription());
        //TODO Handle the case when there is no URL
        if(currentStep.getVideoURL().isEmpty()){
            if(!currentStep.getThumbnailURL().isEmpty()){
                videoLink = currentStep.getThumbnailURL();
            }else{
                simpleExoPlayerView.setVisibility(View.INVISIBLE);
                //TODO Add an image view overlapping the exoPlayerView and make it visible when there is no video , and invisible when there is a video
                //TODO Run on the emulator and read the error occurring in this case
            }
        }else {
            videoLink = currentStep.getVideoURL();
        }
        //TODO Tide things up
        playVideoFromTheInternet(videoLink);
    }

    @OnClick(R.id.btn_next_step)
    void onBtnNextClicked(){
        //TODO fix visibility
        if(currentIndex != allSteps.size()-1){
            currentIndex++;
            if (player != null) {
                player.release();
                player = null;
                selector = null;
            }
            playVideoFromTheInternet(allSteps.get(currentIndex).getVideoURL());
            tvShowViewFullDescription.setText(allSteps.get(currentIndex).getDescription());
            tvCurrentStep.setText(allSteps.get(currentIndex).getShortDescription());
            fixColors();
        }

    }

    @OnClick(R.id.btn_prev_step)
    void onBtnPreviousClicked(){
        //TODO fix visibility
        if(currentIndex != 0){
            currentIndex--;
            if (player != null) {
                player.release();
                player = null;
                selector = null;
            }
            playVideoFromTheInternet(allSteps.get(currentIndex).getVideoURL());
            tvShowViewFullDescription.setText(allSteps.get(currentIndex).getDescription());
            tvCurrentStep.setText(allSteps.get(currentIndex).getShortDescription());
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
        player.prepare(mediaSource);

        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(true);
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
}
