/*
 * Copyright 2023 Daniel Logan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.frc836.yearly;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import org.frc836.database.MatchStatsStruct;
import org.growingstems.scouting.R;

class GridClickListener implements View.OnClickListener{

    public enum GridContents {
        NONE,
        CUBE,
        CONE,
        HYBRID
    }

    public interface GridCallback {
        GridContents getNewState(MatchStatsStruct data);
    }

    private final Context m_context;
    private final GridCallback m_setter;
    private final MatchStatsStruct m_stats;

    public GridClickListener(@NonNull Context context, @NonNull GridCallback callback, @NonNull MatchStatsStruct stats) {
        m_setter = callback;
        m_context = context;
        m_stats = stats;
    }


    @Override
    public void onClick(View v) {
        GridContents con = m_setter.getNewState(m_stats);
        switch (con)
        {
            case NONE:
                v.setForeground(null);
                break;
            case CUBE:
                v.setForeground(AppCompatResources.getDrawable(m_context, R.drawable.selection_square_purple));
                break;
            case CONE:
                v.setForeground(AppCompatResources.getDrawable(m_context, R.drawable.selection_square_yellow));
                break;
            case HYBRID:
                v.setForeground(AppCompatResources.getDrawable(m_context, R.drawable.selection_square_green));
                break;
        }
    }

}
