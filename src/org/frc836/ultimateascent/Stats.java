/*
 * Copyright 2013 Daniel Logan
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

package org.frc836.ultimateascent;

import java.util.List;
import java.util.Map;

public abstract class Stats {

	public static float getAvgScore(List<Map<String, String>> table) {
		float score = 0;
		for (Map<String, String> row : table) {
			score += Integer.valueOf(row.get("auto_score_high")) * 6;
			score += Integer.valueOf(row.get("auto_score_mid_left")) * 4;
			score += Integer.valueOf(row.get("auto_score_mid_right")) * 4;
			score += Integer.valueOf(row.get("auto_score_low")) * 2;
			score += Integer.valueOf(row.get("score_high")) * 3;
			score += Integer.valueOf(row.get("score_mid_left")) * 2;
			score += Integer.valueOf(row.get("score_mid_right")) * 2;
			score += Integer.valueOf(row.get("score_low"));
			score += Integer.valueOf(row.get("score_pyramid")) * 5;
			score += Integer.valueOf(row.get("pyramid_level_climbed")) * 10;
		}
		score /= table.size();
		return round(score);
	}

	public static int getMatchScore(Map<String, String> row) {
		int score = 0;
		score += Integer.valueOf(row.get("auto_score_high")) * 6;
		score += Integer.valueOf(row.get("auto_score_mid_left")) * 4;
		score += Integer.valueOf(row.get("auto_score_mid_right")) * 4;
		score += Integer.valueOf(row.get("auto_score_low")) * 2;
		score += Integer.valueOf(row.get("score_high")) * 3;
		score += Integer.valueOf(row.get("score_mid_left")) * 2;
		score += Integer.valueOf(row.get("score_mid_right")) * 2;
		score += Integer.valueOf(row.get("score_low"));
		score += Integer.valueOf(row.get("score_pyramid")) * 5;
		score += Integer.valueOf(row.get("pyramid_level_climbed")) * 10;
		return score;
	}

	public static float getAvgDiscScore(List<Map<String, String>> table) {
		float score = 0;
		for (Map<String, String> row : table) {
			score += Integer.valueOf(row.get("auto_score_high")) * 6;
			score += Integer.valueOf(row.get("auto_score_mid_left")) * 4;
			score += Integer.valueOf(row.get("auto_score_mid_right")) * 4;
			score += Integer.valueOf(row.get("auto_score_low")) * 2;
			score += Integer.valueOf(row.get("score_high")) * 3;
			score += Integer.valueOf(row.get("score_mid_left")) * 2;
			score += Integer.valueOf(row.get("score_mid_right")) * 2;
			score += Integer.valueOf(row.get("score_low"));
			score += Integer.valueOf(row.get("score_pyramid")) * 5;
		}
		score /= table.size();
		return round(score);
	}

	public static int getMatchDiscScore(Map<String, String> row) {
		int score = 0;
		score += Integer.valueOf(row.get("auto_score_high")) * 6;
		score += Integer.valueOf(row.get("auto_score_mid_left")) * 4;
		score += Integer.valueOf(row.get("auto_score_mid_right")) * 4;
		score += Integer.valueOf(row.get("auto_score_low")) * 2;
		score += Integer.valueOf(row.get("score_high")) * 3;
		score += Integer.valueOf(row.get("score_mid_left")) * 2;
		score += Integer.valueOf(row.get("score_mid_right")) * 2;
		score += Integer.valueOf(row.get("score_low"));
		score += Integer.valueOf(row.get("score_pyramid")) * 5;
		return score;
	}

	public static float getAvgAutoScore(List<Map<String, String>> table) {
		float score = 0;
		for (Map<String, String> row : table) {
			score += Integer.valueOf(row.get("auto_score_high")) * 6;
			score += Integer.valueOf(row.get("auto_score_mid_left")) * 4;
			score += Integer.valueOf(row.get("auto_score_mid_right")) * 4;
			score += Integer.valueOf(row.get("auto_score_low")) * 2;
		}
		score /= table.size();
		return round(score);
	}

	public static int getMatchAutoScore(Map<String, String> row) {
		int score = 0;
		score += Integer.valueOf(row.get("auto_score_high")) * 6;
		score += Integer.valueOf(row.get("auto_score_mid_left")) * 4;
		score += Integer.valueOf(row.get("auto_score_mid_right")) * 4;
		score += Integer.valueOf(row.get("auto_score_low")) * 2;
		return score;
	}

	public static float getAvgClimbScore(List<Map<String, String>> table) {
		float score = 0;
		for (Map<String, String> row : table) {
			score += Integer.valueOf(row.get("pyramid_level_climbed")) * 10;
		}
		score /= table.size();
		return round(score);
	}

	public static int getMatchClimbScore(Map<String, String> row) {
		return Integer.valueOf(row.get("pyramid_level_climbed")) * 10;
	}

	public static float getAvgAccuracy(List<Map<String, String>> table) {
		int scores = 0;
		int attempts = 0;
		for (Map<String, String> row : table) {
			scores += Integer.valueOf(row.get("auto_score_high"));
			scores += Integer.valueOf(row.get("auto_score_mid_left"));
			scores += Integer.valueOf(row.get("auto_score_mid_right"));
			scores += Integer.valueOf(row.get("auto_score_low"));
			scores += Integer.valueOf(row.get("score_high"));
			scores += Integer.valueOf(row.get("score_mid_left"));
			scores += Integer.valueOf(row.get("score_mid_right"));
			scores += Integer.valueOf(row.get("score_low"));
			scores += Integer.valueOf(row.get("score_pyramid"));
			attempts += Integer.valueOf(row.get("auto_score_high_attempt"));
			attempts += Integer.valueOf(row.get("auto_score_mid_left_attempt"));
			attempts += Integer
					.valueOf(row.get("auto_score_mid_right_attempt"));
			attempts += Integer.valueOf(row.get("auto_score_low_attempt"));
			attempts += Integer.valueOf(row.get("score_high_attempt"));
			attempts += Integer.valueOf(row.get("score_mid_left_attempt"));
			attempts += Integer.valueOf(row.get("score_mid_right_attempt"));
			attempts += Integer.valueOf(row.get("score_low_attempt"));
			attempts += Integer.valueOf(row.get("score_pyramid_attempt"));
		}
		float accuracy = ((float) scores) / ((float) attempts);
		return roundPercent(accuracy);
	}

	public static float getMatchAccuracy(Map<String, String> row) {
		int scores = 0;
		int attempts = 0;
		scores += Integer.valueOf(row.get("auto_score_high"));
		scores += Integer.valueOf(row.get("auto_score_mid_left"));
		scores += Integer.valueOf(row.get("auto_score_mid_right"));
		scores += Integer.valueOf(row.get("auto_score_low"));
		scores += Integer.valueOf(row.get("score_high"));
		scores += Integer.valueOf(row.get("score_mid_left"));
		scores += Integer.valueOf(row.get("score_mid_right"));
		scores += Integer.valueOf(row.get("score_low"));
		scores += Integer.valueOf(row.get("score_pyramid"));
		attempts += Integer.valueOf(row.get("auto_score_high_attempt"));
		attempts += Integer.valueOf(row.get("auto_score_mid_left_attempt"));
		attempts += Integer.valueOf(row.get("auto_score_mid_right_attempt"));
		attempts += Integer.valueOf(row.get("auto_score_low_attempt"));
		attempts += Integer.valueOf(row.get("score_high_attempt"));
		attempts += Integer.valueOf(row.get("score_mid_left_attempt"));
		attempts += Integer.valueOf(row.get("score_mid_right_attempt"));
		attempts += Integer.valueOf(row.get("score_low_attempt"));
		attempts += Integer.valueOf(row.get("score_pyramid_attempt"));
		float accuracy = ((float) scores) / ((float) attempts);
		return roundPercent(accuracy);
	}

	public static float round(float num) {
		int per = (int) (num * 10);
		float cent = ((float) per) / 10.0f;
		return cent;
	}
	
	public static float roundPercent(float num) {
		int per = (int) (num * 1000);
		float cent = ((float) per) / 10.0f;
		return cent;
	}

}
