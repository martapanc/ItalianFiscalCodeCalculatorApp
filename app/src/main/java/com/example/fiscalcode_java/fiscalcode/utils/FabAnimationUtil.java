package com.example.fiscalcode_java.fiscalcode.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class FabAnimationUtil {

    public static void showIn(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        view.setTranslationY(view.getHeight());
        view.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    public static void showOut(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(1f);
        view.setTranslationY(view.getHeight());
        view.animate()
                .setDuration(200)
                .translationY(view.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(0f)
                .start();
    }

    public static void init(final View view) {
        view.setVisibility(View.GONE);
        view.setTranslationY(view.getHeight());
        view.setAlpha(0f);
    }
}
