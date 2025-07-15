/*
 * This file is part of Glasspath Revenue.
 * Copyright (C) 2011 - 2023 Remco Poelstra
 * Authors: Remco Poelstra
 *
 * This program is offered under a commercial and under the AGPL license.
 * For commercial licensing, contact us at https://glasspath.org. For AGPL licensing, see below.
 *
 * AGPL licensing:
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.glasspath.common.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {

    public static final String TAG = "RecyclerView";

    private int autoScrollHeight = 0;

    public RecyclerView(@NonNull Context context) {
        super(context);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getAutoScrollHeight() {
        return autoScrollHeight;
    }

    public void setAutoScrollHeight(int autoScrollHeight) {
        this.autoScrollHeight = autoScrollHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean consumed = super.onInterceptTouchEvent(e);

        if (e != null && e.getActionMasked() == MotionEvent.ACTION_DOWN) {

            // Log.i(TAG, "getScrollState() = " + getScrollState() + " ,computeVerticalScrollOffset() = " + computeVerticalScrollOffset());
            if (getScrollState() != SCROLL_STATE_IDLE) {

                int verticalScrollOffset = computeVerticalScrollOffset();
                if (verticalScrollOffset == 0 || (autoScrollHeight > 0 && verticalScrollOffset > autoScrollHeight)) {

                    stopScroll();

                    return false;

                }

            }

        }

        return consumed;

    }

    public int getVerticalScrollPosition() {
        return computeVerticalScrollOffset();
    }

}
