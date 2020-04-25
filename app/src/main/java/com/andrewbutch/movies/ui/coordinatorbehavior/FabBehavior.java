package com.andrewbutch.movies.ui.coordinatorbehavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FabBehavior extends AppBarLayout.Behavior {
    FloatingActionButton fab;
    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0) {
            int childCount = coordinatorLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = coordinatorLayout.getChildAt(i);
                if (view instanceof FloatingActionButton) {
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                    int fab_bottomMargin = layoutParams.bottomMargin;
                    fab = (FloatingActionButton) view;
                    fab.animate().translationY(child.getHeight() + fab_bottomMargin).setInterpolator(new LinearInterpolator()).start();
                    fab.hide();
                    break;
                }
            }

        } else if (dyConsumed < 0) {
            int childCount = coordinatorLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = coordinatorLayout.getChildAt(i);
                if (view instanceof FloatingActionButton) {
                    fab = (FloatingActionButton) view;
                    fab.show();
                    fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
                    break;
                }
            }
        }
    }

}
