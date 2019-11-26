package com.example.twitterhangman;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.lang.reflect.Array;

public class GameActivity extends AppCompatActivity {

    private ConstraintSet gameConstraintSet;
    private ConstraintLayout gameConstraintLayout;
    private ImageView[] aUnderscoreView;            ///< Hold the dynamically created underscore views
    private ImageView[] aAlphaView;                 ///< Holds the 28 alphabet Imageviews;
    private enum Alphabet
    {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z, Ex, Qu
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d("LOG","in create");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aAlphaView = new ImageView[28];
        setAlphaArray();

        // TODO - hardcoded need to pass in depending on word length
        int NUM_UNDERSCORES = 6;
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
        // TODO - hard coded temp code
        aUnderscoreView[0].setTag("E");
        aUnderscoreView[1].setTag("X");
        aUnderscoreView[2].setTag("O");
        aUnderscoreView[3].setTag("D");
        aUnderscoreView[4].setTag("I");
        aUnderscoreView[5].setTag("A");

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

        for (ImageView underScoreView : aUnderscoreView)
        {
            underScoreView.setOnDragListener(new MyDragListener());
        }
    }

    private void setUnderscoreParams(int underscoreInd, int width, int height)
    {
        aUnderscoreView[underscoreInd].setId(View.generateViewId());
        aUnderscoreView[underscoreInd].setLayoutParams(new ConstraintLayout.LayoutParams(width, height));
        aUnderscoreView[underscoreInd].setAdjustViewBounds(true);
        aUnderscoreView[underscoreInd].setScaleType(ImageView.ScaleType.FIT_XY);
    }

    private void setAlphaArray()
    {
        aAlphaView[0] = findViewById(R.id.alphaA);
        aAlphaView[1] = findViewById(R.id.alphaB);
        aAlphaView[2] = findViewById(R.id.alphaC);
        aAlphaView[3] = findViewById(R.id.alphaD);
        aAlphaView[4] = findViewById(R.id.alphaE);
        aAlphaView[5] = findViewById(R.id.alphaF);
        aAlphaView[6] = findViewById(R.id.alphaG);
        aAlphaView[7] = findViewById(R.id.alphaH);
        aAlphaView[8] = findViewById(R.id.alphaI);
        aAlphaView[9] = findViewById(R.id.alphaJ);
        aAlphaView[10] = findViewById(R.id.alphaK);
        aAlphaView[11] = findViewById(R.id.alphaL);
        aAlphaView[12] = findViewById(R.id.alphaM);
        aAlphaView[13] = findViewById(R.id.alphaN);
        aAlphaView[14] = findViewById(R.id.alphaO);
        aAlphaView[15] = findViewById(R.id.alphaP);
        aAlphaView[16] = findViewById(R.id.alphaQ);
        aAlphaView[17] = findViewById(R.id.alphaR);
        aAlphaView[18] = findViewById(R.id.alphaS);
        aAlphaView[19] = findViewById(R.id.alphaT);
        aAlphaView[20] = findViewById(R.id.alphaU);
        aAlphaView[21] = findViewById(R.id.alphaV);
        aAlphaView[22] = findViewById(R.id.alphaW);
        aAlphaView[23] = findViewById(R.id.alphaX);
        aAlphaView[24] = findViewById(R.id.alphaY);
        aAlphaView[25] = findViewById(R.id.alphaZ);
        aAlphaView[26] = findViewById(R.id.alphaExclamation);
        aAlphaView[27] = findViewById(R.id.alphaQuestion);

        for (int i = Alphabet.A.ordinal(); i < Alphabet.Qu.ordinal(); ++i)
        {
            aAlphaView[i].setOnTouchListener(new MyTouchListener());
            aAlphaView[i].setTag(Alphabet.values()[i]);
        }
        Log.d("LOG", aAlphaView[3].getTag().toString());
    }

    // Drag class
    private class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                // TODO - Makes shadow builder really tiny. This shouldnt be a problem since the image will be replaced in the future instead.
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                //iew.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    // Drop class - listens to drag events
    private class MyDragListener implements View.OnDragListener {
        // Draws animations while dragging?
        //Drawable enterShape = getResources().getDrawable(
        //R.drawable.shape_droptarget);
        //Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    if (v.getTag().toString().equals(view.getTag().toString()))
                    {
                        Drawable img = ((ImageView) view).getDrawable();
                        ((ImageView) v).setImageDrawable(img);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }

    }

}
