/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.lusen.cardola.framework.uikit.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.lusen.cardola.R;
import com.lusen.cardola.framework.uikit.pulltorefresh.PullToRefreshBase;


public class CircleProgressLoadingLayout extends LoadingLayout {

    private final Matrix mHeaderImageMatrix;


    private float mRotationPivotX, mRotationPivotY;

    private final boolean mRotateDrawableWhilePulling;

    public CircleProgressLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);

        if (attrs != null) {
            mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);
        } else {
            mRotateDrawableWhilePulling = true;
        }

        final float rotateAngle = mode == PullToRefreshBase.Mode.PULL_FROM_START ? 0.0f : 180.0f;
        mHeaderImage.setScaleType(ScaleType.MATRIX);
        mHeaderImageMatrix = new Matrix();
        mHeaderImageMatrix.setRotate(rotateAngle, mRotationPivotX, mRotationPivotY);
        mHeaderImage.setImageMatrix(mHeaderImageMatrix);
    }


    public void onLoadingDrawableSet(Drawable imageDrawable) {
        if (null != imageDrawable) {
            mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
            mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
        }
    }

    protected void onPullImpl(float scaleOfLayout) {
        float angle = 0.0f;
        float progressRotate = 0.0f;
        if (scaleOfLayout > 1.0f) {
            progressRotate = 1.0f;
        } else {
            progressRotate = scaleOfLayout;
        }
        if (mMode == PullToRefreshBase.Mode.PULL_FROM_START) {
            if (mRotateDrawableWhilePulling) {
                angle = progressRotate * 2 * 90f;
            } else {
                angle = Math.max(0f, Math.min(180f, progressRotate * 360f - 180f));
            }
        } else if (mMode == PullToRefreshBase.Mode.PULL_FROM_END) {
            angle = progressRotate * 2 * 90f + 180f;
        }
        mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
        mHeaderImage.setImageMatrix(mHeaderImageMatrix);
        int progress = 0;
        if (scaleOfLayout > 1.0f) {
            progress = 100;
        } else {
            progress = (int) (scaleOfLayout * 100);
        }
        mRefreshCircle.setProgress((int) (progress * 3.6));
    }

    @Override
    protected void refreshingImpl() {
        mHeaderImage.setVisibility(INVISIBLE);
        mRefreshCircle.setVisibility(INVISIBLE);
        mHeaderProgress.setVisibility(VISIBLE);
    }

    @Override
    protected void resetImpl() {
        mHeaderImage.clearAnimation();
        resetImageRotation();
        mHeaderImage.setVisibility(View.VISIBLE);
        mHeaderProgress.setVisibility(View.GONE);
        if (mRefreshCircle != null) {
            mRefreshCircle.setProgress(0);
            mRefreshCircle.setVisibility(VISIBLE);
        }
    }

    private void resetImageRotation() {
        if (null != mHeaderImageMatrix) {
            mHeaderImageMatrix.reset();
            mHeaderImage.setImageMatrix(mHeaderImageMatrix);
        }
    }

    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.pull_down_refresh;
    }

}
