/*
 * Copyright 2016 Daniel Logan
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

package org.robobees.stronghold;

import android.os.AsyncTask;
import android.util.SparseArray;
import android.util.SparseIntArray;

import org.frc836.database.DB;
import org.frc836.database.MatchStatsStruct;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GraphDataSource {

    public enum DataType {None, Scores}

    private DB db;

    public GraphDataSource(DB db) {
        this.db = db;
    }

    public void getMaxScores(int team, String event, GraphDataCallback callback) {
        GraphData data = new GraphData(callback, DataType.Scores);
        data.setTeamNum(team);
        data.setEventName(event);
        db.getMatchesForTeam(team, event, new DBResp(data));
    }

    private class DBResp implements DB.DBCallback {

        private GraphData _data;

        public DBResp(GraphData data) {
            _data = data;
        }

        @Override
        public void onFinish(DB.DBData data) {
            _data._input = data;
            if (_data.getDataType() == DataType.Scores)
                (new ScoresAsync()).execute(_data);
        }
    }

    public interface GraphDataCallback {
        void onFinished(GraphData data);
    }

    public class GraphData {
        protected GraphDataCallback _callback;

        private DataType _dataType = DataType.None;

        protected DB.DBData _input;

        private int _teamNum = -1; //Scores

        private String _eventName = null;

        protected Map<String, SparseIntArray> _totalScores = null;

        protected double _averageScore = -1.0;

        public GraphData(GraphDataCallback callback, DataType type) {
            _callback = callback;
            _dataType = type;
        }

        public DataType getDataType() {
            return _dataType;
        }

        protected void setTeamNum(int team) {
            _teamNum = team;
        }

        protected void setEventName(String eventName) {
            _eventName = eventName;
        }

        public int getTeamNum() {
            return _teamNum;
        }

        public String getEventName() {
            return _eventName;
        }

        public SparseIntArray getTotalScores() {
            return getTotalScores(null);
        }

        public SparseIntArray getTotalScores(String eventName) {
            if (_dataType != DataType.Scores) {
                return null;
            }
            if (eventName == null && _eventName != null) {
                return _totalScores.get(_eventName);
            } else if (eventName != null) {
                return _totalScores.get(eventName);
            }
            return null;
        }

        public double getAverageScore() {
            return _averageScore;
        }

        public Set<String> getEventsWithScores() {
            return _totalScores.keySet();
        }


    }

    private class ScoresAsync extends AsyncTask<GraphData, Integer, GraphData> {


        @Override
        protected GraphData doInBackground(GraphData... params) {

            Map<String, SparseArray<MatchStatsStruct>> eventMap = params[0]._input.getMatches();

            params[0]._totalScores = new HashMap<String, SparseIntArray>(eventMap.size());

            params[0]._averageScore = 0.0;

            int count = 0;

            for (Map.Entry<String, SparseArray<MatchStatsStruct>> event : eventMap.entrySet()) {
                SparseArray<MatchStatsStruct> matches = event.getValue();
                SparseIntArray scores = new SparseIntArray(matches.size());

                for (int i = 0; i < matches.size(); i++) {
                    int matchNum = matches.keyAt(i);
                    int score = matches.get(matchNum).getTotalScore();
                    scores.put(matchNum, score);
                    count++;
                    params[0]._averageScore += score;
                }
                params[0]._totalScores.put(event.getKey(), scores);
            }
            params[0]._averageScore /= count;
            return params[0];
        }

        protected void onPostExecute(GraphData data) {
            if (data != null && data._callback != null)
                data._callback.onFinished(data);
        }
    }

}
