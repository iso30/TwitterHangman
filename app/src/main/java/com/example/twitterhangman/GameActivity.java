package com.example.twitterhangman;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.lang.reflect.Array;

public class GameActivity extends AppCompatActivity {

    private ConstraintSet gameConstraintSet;
    private ImageView[] aUnderscoreView;            ///< Hold the dynamically created underscore views
    private ConstraintLayout gameConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d("LOG","in create");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO - hardcoded need to pass in depending on word length
        int NUM_UNDERSCORES = 5;
        gameConstraintSet = new ConstraintSet();
        gameConstraintLayout = (ConstraintLayout) findViewById(R.id.include_activity_game);

        // Get actual width at runtime and subtract boundries
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        int gameWindowWidth = outMetrics.widthPixels - (getResources().getDimensionPixelSize(R.dimen.game_side_border) * 2);

        // Duplicate underscore views and index them
        aUnderscoreView = new ImageView[NUM_UNDERSCORES];
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int underscoreInd = 0; underscoreInd < NUM_UNDERSCORES; ++underscoreInd)
        {
            aUnderscoreView[underscoreInd] = (ImageView) inflater.inflate(R.layout.duplicatable, null);
            setUnderscoreParams(underscoreInd,gameWindowWidth / NUM_UNDERSCORES, gameWindowWidth / (NUM_UNDERSCORES - 1));
            gameConstraintLayout.addView(aUnderscoreView[underscoreInd]);
        }

        // Set underscore view relative layout
        gameConstraintSet.clone(gameConstraintLayout);
        for (int underscoreInd = 0; underscoreInd < NUM_UNDERSCORES; ++underscoreInd)
        {
            gameConstraintSet.connect(aUnderscoreView[underscoreInd].getId(), ConstraintSet.BOTTOM, R.id.TableTopGuideline, ConstraintSet.TOP);
            if (underscoreInd == 0)
                gameConstraintSet.connect(aUnderscoreView[underscoreInd].getId(), ConstraintSet.LEFT, R.id.LeftGuideline, ConstraintSet.RIGHT);
            else
                gameConstraintSet.connect(aUnderscoreView[underscoreInd].getId(), ConstraintSet.LEFT, aUnderscoreView[underscoreInd - 1].getId(), ConstraintSet.RIGHT);
        }
        gameConstraintSet.applyTo(gameConstraintLayout);

    }

    private void setUnderscoreParams(int underscoreInd, int width, int height)
    {
        aUnderscoreView[underscoreInd].setId(View.generateViewId());
        aUnderscoreView[underscoreInd].setLayoutParams(new ConstraintLayout.LayoutParams(width, height));
        aUnderscoreView[underscoreInd].setAdjustViewBounds(true);
        aUnderscoreView[underscoreInd].setScaleType(ImageView.ScaleType.FIT_XY);
    }
    protected void onStart()
    {
        super.onStart();

    }

}
