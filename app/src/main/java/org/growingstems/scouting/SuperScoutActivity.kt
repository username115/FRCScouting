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
package org.growingstems.scouting

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.frc836.database.DBActivity
import org.frc836.database.SuperScoutStats
import org.frc836.yearly.SuperScoutStatsYearly

class SuperScoutActivity : DBActivity() {

    private val timer = Handler(Looper.getMainLooper())
    private val DELAY: Long = 500
    private val tasks = ArrayList<MatchNumTask>(3)

    private val team1Data = SuperScoutStats()
    private val team2Data = SuperScoutStats()
    private val team3Data = SuperScoutStats()

    private val defaultEvent = "CHS District Oxon Hill MD Event"

    private val schedule = MatchSchedule()

    private var redAlliance = true

    private val matchT: EditText? by lazy { findViewById(R.id.ssMatchNum) }
    private val posText: TextView? by lazy { findViewById(R.id.ssPos) }

    private val team1RankingsT: TextView? by lazy ( findViewById(R.id.ssTeam1RankingsT) )
    private val team2RankingsT: TextView? by lazy ( findViewById(R.id.ssTeam2RankingsT) )
    private val team3RankingsT: TextView? by lazy ( findViewById(R.id.ssTeam3RankingsT) )

    private val team1NotesLabelT: TextView? by lazy ( findViewById(R.id.ssTeam1NotesLabelT) )
    private val team2NotesLabelT: TextView? by lazy ( findViewById(R.id.ssTeam2NotesLabelT) )
    private val team3NotesLabelT: TextView? by lazy ( findViewById(R.id.ssTeam3NotesLabelT) )

    private val team1OffenseB: Button by lazy ( findViewById(R.id.ssTeam1OffenseB) )
    private val team2OffenseB: Button by lazy ( findViewById(R.id.ssTeam2OffenseB) )
    private val team3OffenseB: Button by lazy ( findViewById(R.id.ssTeam3OffenseB) )

    private val team1DefenseB: Button by lazy ( findViewById(R.id.ssTeam1DefenseB) )
    private val team2DefenseB: Button by lazy ( findViewById(R.id.ssTeam2DefenseB) )
    private val team3DefenseB: Button by lazy ( findViewById(R.id.ssTeam3DefenseB) )

    private val team1DriverB: Button by lazy ( findViewById(R.id.ssTeam1DriverB) )
    private val team2DriverB: Button by lazy ( findViewById(R.id.ssTeam2DriverB) )
    private val team3DriverB: Button by lazy ( findViewById(R.id.ssTeam3DriverB) )

    private val team1NotesT: EditText by lazy ( findViewById(R.id.ssTeam1NotesT) )
    private val team2NotesT: EditText by lazy ( findViewById(R.id.ssTeam2NotesT) )
    private val team3NotesT: EditText by lazy ( findViewById(R.id.ssTeam3NotesT) )

    override val helpMessage: String by lazy {
        """
            FIXME
            """.trimIndent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_superscout)
        updateAlliance()

        posText?.setOnClickListener{ MainMenuSelection.openSettings(this) }


        matchT?.addTextChangedListener(MatchTextChangeListener())
    }

    override fun onResume() {
        super.onResume()

        for (task in tasks) {
            timer.removeCallbacks(task)
        }
        tasks.clear()

        val eventId = Prefs.getEvent(this, defaultEvent)

        if (!schedule.isValid(this)) {
            schedule.updateSchedule(eventId, this, false)
        }

        team1Data.event_id = eventId
        team2Data.event_id = team1Data.event_id
        team3Data.event_id = team1Data.event_id

        team1Data.practice_match = Prefs.getPracticeMatch(applicationContext, false)
        team2Data.practice_match = team1Data.practice_match
        team3Data.practice_match = team1Data.practice_match

        team1Data.position_id = if (redAlliance) { "Red 1" } else { "Blue 1" }
        team2Data.position_id = if (redAlliance) { "Red 2" } else { "Blue 2" }
        team3Data.position_id = if (redAlliance) { "Red 3" } else { "Blue 3" }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override val resultForPrefs = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        schedule.updateSchedule(Prefs.getEvent(this, defaultEvent),
            this, false)
        updateAlliance()
        if (matchT?.text?.length?.let { it > 0 } == true) {
            setMatch(matchT?.text.toString().toInt())
        }
    }

    private fun updateAlliance() {
        redAlliance = !(Prefs.getPosition(this, "Red 1").contains("Blue"))

        posText?.setTextColor(
            if (redAlliance) { Color.RED }
            else { Color.BLUE }
        )
        posText?.setText(
            if (redAlliance) { R.string.red }
            else { R.string.blue }
        )
    }

    private fun loadData() {

    }

    private fun saveData() {

    }

    private fun setMatch(match :Int) {
        val alliance = if (redAlliance) { "Red" } else { "Blue" }

        val team1S = schedule.getTeam(match, "$alliance 1", this, "0")
        team1Data.team_id = team1S?.toInt() ?: 0
        val team2S = schedule.getTeam(match, "$alliance 2", this, "0")
        team2Data.team_id = team2S?.toInt() ?: 0
        val team3S = schedule.getTeam(match, "$alliance 3", this, "0")
        team3Data.team_id = team3S?.toInt() ?: 0

        team1RankingsT?.text = team1S
        team1NotesLabelT?.text = team1S
        team2RankingsT?.text = team2S
        team2NotesLabelT?.text = team2S
        team3RankingsT?.text = team3S
        team3NotesLabelT?.text = team3S

        val stats1 = db.getSuperScoutStats(team1Data.event_id, match, team1Data.team_id, team1Data.practice_match)
        val stats2 = db.getSuperScoutStats(team2Data.event_id, match, team2Data.team_id, team2Data.practice_match)
        val stats3 = db.getSuperScoutStats(team3Data.event_id, match, team3Data.team_id, team3Data.practice_match)

        SuperScoutStatsYearly.copyData(stats1, team1Data)
        SuperScoutStatsYearly.copyData(stats2, team2Data)
        SuperScoutStatsYearly.copyData(stats3, team3Data)

        loadData()

    }

    private inner class MatchNumTask(match_num : Int) : Runnable {

        val matchNum: Int = match_num

        override fun run() {
            if (matchT?.text?.length?.let { it > 0 } == true
                && matchT?.text?.toString()?.toInt()?.let { it == matchNum } == true)
            {
                setMatch(matchNum)
            }
        }

    }

    private inner class MatchTextChangeListener : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //Do nothing
        }

        override fun afterTextChanged(s: Editable?) {
            if (s?.length?.let { it > 0 } == true) {
                val task = MatchNumTask(s.toString().toInt())
                tasks.add(task)
                timer.postDelayed(task, DELAY)
            }
        }

    }
}
