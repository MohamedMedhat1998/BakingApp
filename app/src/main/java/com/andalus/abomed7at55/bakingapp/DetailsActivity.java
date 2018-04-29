package com.andalus.abomed7at55.bakingapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_view_full_description)
    TextView tvShowViewFullDescription;
    @BindView(R.id.simple_exo_player)
    SimpleExoPlayerView simpleExoPlayerView;

    private Step currentStep;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    private String videoLink;
    private TrackSelector selector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //TODO Support onSaveInstanceState
        ButterKnife.bind(this);



        currentStep = getIntent().getExtras().getParcelable(getString(R.string.keySelectedStep));

        tvShowViewFullDescription.setText(currentStep.getDescription());
        //TODO Handle the case when there is no URL
        videoLink = currentStep.getVideoURL();
        //TODO Tide things up
        playVideoFromTheInternet();
    }

    private void playVideoFromTheInternet(){
        mediaDataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)),
                (TransferListener<? super DataSource>) bandwidthMeter);

        bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory selectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);

        selector = new DefaultTrackSelector(selectionFactory);

        LoadControl loadControl = new DefaultLoadControl();

        DefaultExtractorsFactory defaultExtractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoLink),
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
