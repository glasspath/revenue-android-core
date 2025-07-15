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
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LinearLayoutManager extends androidx.recyclerview.widget.LinearLayoutManager {

    public static final String TAG = "LinearLayoutManager";

    private final Map<Integer, Integer> itemHeights = new HashMap<>();

    public LinearLayoutManager(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            if (child != null) {

                int pos = getPosition(child);
                if (pos >= 0) {
                    itemHeights.put(pos, child.getHeight());
                }

            }

        }

    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {

        int scrollOffset = 0;

        if (getChildCount() > 0) {

            View firstVisibleChild = getChildAt(0);
            if (firstVisibleChild != null) {

                scrollOffset = -((int) firstVisibleChild.getY());

                int firstVisibleChildPosition = getPosition(firstVisibleChild);
                for (int i = 0; i < firstVisibleChildPosition; i++) {

                    Integer itemHeight = itemHeights.get(i);
                    if (itemHeight != null) {
                        scrollOffset += itemHeight;
                    }

                }

            }

        }

        return scrollOffset;

    }

    public boolean saveItemHeights(@NonNull Bundle bundle, String keyPrefix) {

        try {

            Set<Integer> keys = itemHeights.keySet();
            Collection<Integer> heights = itemHeights.values();
            if (keys.size() > 0 && heights.size() == keys.size()) {

                int[] keysArray = new int[keys.size()];
                int i = 0;
                for (Integer key : keys) {
                    keysArray[i++] = key.intValue();
                }
                bundle.putIntArray(keyPrefix + "ItemPositions", keysArray);

                int[] heightsArray = new int[keys.size()];
                i = 0;
                for (Integer height : heights) {
                    heightsArray[i++] = height.intValue();
                }
                bundle.putIntArray(keyPrefix + "ItemHeights", heightsArray);

                return true;

            }

        } catch (Exception e) {
            Log.e(TAG, "Exception while saving item heights", e);
        }

        return false;

    }

    public boolean restoreItemHeights(@NonNull Bundle bundle, String keyPrefix) {

        try {

            int[] keysArray = bundle.getIntArray(keyPrefix + "ItemPositions");
            int[] heightsArray = bundle.getIntArray(keyPrefix + "ItemHeights");
            if (keysArray != null && keysArray.length > 0 && heightsArray != null && heightsArray.length == keysArray.length) {

                for (int i = 0; i < keysArray.length; i++) {
                    itemHeights.put(keysArray[i], heightsArray[i]);
                }

                return true;

            }

        } catch (Exception e) {
            Log.e(TAG, "Exception while restoring item heights", e);
        }

        return false;

    }

}
