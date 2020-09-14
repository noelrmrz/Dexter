package com.noelrmrz.pokedex.utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.noelrmrz.pokedex.R;

public class ViewAnimation {

    public ViewAnimation() {

    }

    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (rotate) {
                            v.setBackgroundTintList(ColorStateList
                                    .valueOf(v.getResources().getColor(R.color.fire)));
                        } else {
                            v.setBackgroundTintList(ColorStateList
                                    .valueOf(v.getResources().getColor(R.color.colorAccent)));
                        }
                    }
                })
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }
}
