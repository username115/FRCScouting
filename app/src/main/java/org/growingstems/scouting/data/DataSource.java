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

package org.growingstems.scouting.data;

import android.os.AsyncTask;
import android.util.SparseArray;
import android.util.SparseIntArray;

import org.frc836.database.DB;
import org.frc836.database.MatchStatsStruct;
import org.frc836.yearly.MatchStatsYearly;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataSource {

    public enum DataType {Graphs, AVG}

    private final DB db;

    public DataSource(DB db) {
        this.db = db;
    }

    public void getGraphData(int team, String event, DataCallback callback) {
        Data data = new Data(callback, DataType.Graphs);
        data.setTeamNum(team);
        data.setEventName(event);
        db.getMatchesForTeam(team, event, new DBResp(data));
    }

    public void getAVGData(int team, String event, DataCallback callback) {
        Data data = new Data(callback, DataType.AVG);
        data.setTeamNum(team);
        data.setEventName(event);
        db.getMatchesForTeam(team, event, new DBResp(data));
    }

    private static class DBResp implements DB.DBCallback {

        private final Data _data;

        public DBResp(Data data) {
            _data = data;
        }

        @Override
        public void onFinish(DB.DBData data) {
            _data._input = data;
            if (_data.getDataType() == DataType.Graphs)
                (new GraphsAsync()).execute(_data);
            if (_data.getDataType() == DataType.AVG)
                (new AVGAsync()).execute(_data);
        }
    }

    public interface DataCallback {
        void onFinished(Data data);
    }

    public static class Data {
        protected DataCallback _callback;

        private final DataType _dataType;

        protected DB.DBData _input;

        private int _teamNum = -1;

        private String _eventName = null;

        protected Map<String, Map<String, SparseIntArray>> _graphs = null; //<eventName, <GraphName, graphData>>
        protected Map<String, Map<String, Double>> _avgs = null; //<eventName, <GraphName, AVG>>

        public Data(DataCallback callback, DataType type) {
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

        public Map<String, SparseIntArray> getEventGraphs() {
            return getEventGraphs(null);
        }

        public Map<String, Map<String, SparseIntArray>> getGraphs() {
            return _graphs;
        }

        public Map<String, SparseIntArray> getEventGraphs(String eventName) {
            if (_dataType != DataType.Graphs) {
                return null;
            }
            if (eventName == null && _eventName != null) {
                return _graphs.get(_eventName);
            } else if (eventName != null) {
                return _graphs.get(eventName);
            }
            return null;
        }

        public Set<String> getEventsWithScores() {
            return _graphs.keySet();
        }

        public Map<String, Map<String, Double>> getAVGs() {
            return _avgs;
        }

        public Map<String, Double> getEventAVGs(String eventName) {
            if (_dataType != DataType.AVG) {
                return null;
            }
            if (eventName == null && _eventName != null) {
                return _avgs.get(_eventName);
            } else if (eventName != null) {
                return _avgs.get(eventName);
            }
            return null;
        }

    }

    private static class GraphsAsync extends AsyncTask<Data, Integer, Data> {


        @Override
        protected Data doInBackground(Data... params) {

            Map<String, SparseArray<MatchStatsStruct>> eventMap = params[0]._input.getMatches();

            params[0]._graphs = new HashMap<>(eventMap.size());

            int count = 0;

            for (Map.Entry<String, SparseArray<MatchStatsStruct>> event : eventMap.entrySet()) {
                SparseArray<MatchStatsStruct> matches = event.getValue();
                Map<String, SparseIntArray> graphs = new HashMap<>(MatchStatsYearly.NUM_GRAPHS);

                for (int j = 0; j < MatchStatsYearly.NUM_GRAPHS; j++) {
                    SparseIntArray scores = new SparseIntArray(matches.size());

                    for (int i = 0; i < matches.size(); i++) {
                        int matchNum = matches.keyAt(i);
                        int score = MatchStatsYearly.getStat(j, matches.get(matchNum));
                        scores.put(matchNum, score);
                        count++;
                    }
                    graphs.put(MatchStatsYearly.getGraphNames().get(j), scores);
                }
                params[0]._graphs.put(event.getKey(), graphs);
            }
            return params[0];
        }

        protected void onPostExecute(Data data) {
            if (data != null && data._callback != null)
                data._callback.onFinished(data);
        }
    }

    private static class AVGAsync extends AsyncTask<Data, Integer, Data> {


        @Override
        protected Data doInBackground(Data... params) {

            Map<String, SparseArray<MatchStatsStruct>> eventMap = params[0]._input.getMatches();

            params[0]._avgs= new HashMap<>(eventMap.size());

            int count = 0;

            for (Map.Entry<String, SparseArray<MatchStatsStruct>> event : eventMap.entrySet()) {
                SparseArray<MatchStatsStruct> matches = event.getValue();
                Map<String, Double> avgs = new HashMap<>(MatchStatsYearly.NUM_GRAPHS);

                for (int j = 0; j < MatchStatsYearly.NUM_GRAPHS; j++) {
                    int avg = 0;

                    for (int i = 0; i < matches.size(); i++) {
                        int matchNum = matches.keyAt(i);
                        int score = MatchStatsYearly.getStat(j, matches.get(matchNum));
                        avg += score;
                        count++;
                    }
                    avgs.put(MatchStatsYearly.getGraphNames().get(j), (matches.size() > 0) ? ((double)avg / (double)matches.size()) : 0.0);
                }
                params[0]._avgs.put(event.getKey(), avgs);
            }
            return params[0];
        }

        protected void onPostExecute(Data data) {
            if (data != null && data._callback != null)
                data._callback.onFinished(data);
        }
    }

}
