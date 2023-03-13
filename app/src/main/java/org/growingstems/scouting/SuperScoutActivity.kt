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

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import org.frc836.database.DBActivity
import org.frc836.database.SuperScoutStats
import org.frc836.yearly.SuperScoutStatsYearly

class SuperScoutActivity : DBActivity() {

    private val timer: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val loadDelay: Long = 500
    private val tasks = ArrayList<MatchNumTask>(3)

    private val team1Data = SuperScoutStats()
    private val team2Data = SuperScoutStats()
    private val team3Data = SuperScoutStats()

    private val defaultEvent = "CHS District Oxon Hill MD Event"

    private val schedule = MatchSchedule()

    private var redAlliance = true

    private val matchT: EditText? by lazy { findViewById(R.id.ssMatchNum) }
    private val posText: TextView? by lazy { findViewById(R.id.ssPos) }

    private val team1RankingsT: TextView? by lazy { findViewById(R.id.ssTeam1RankingsT) }
    private val team2RankingsT: TextView? by lazy { findViewById(R.id.ssTeam2RankingsT) }
    private val team3RankingsT: TextView? by lazy { findViewById(R.id.ssTeam3RankingsT) }

    private val team1NotesLabelT: TextView? by lazy { findViewById(R.id.ssTeam1NotesLabelT) }
    private val team2NotesLabelT: TextView? by lazy { findViewById(R.id.ssTeam2NotesLabelT) }
    private val team3NotesLabelT: TextView? by lazy { findViewById(R.id.ssTeam3NotesLabelT) }

    private val team1OffenseB: Button by lazy { findViewById(R.id.ssTeam1OffenseB) }
    private val team2OffenseB: Button by lazy { findViewById(R.id.ssTeam2OffenseB) }
    private val team3OffenseB: Button by lazy { findViewById(R.id.ssTeam3OffenseB) }

    private val team1DefenseB: Button by lazy { findViewById(R.id.ssTeam1DefenseB) }
    private val team2DefenseB: Button by lazy { findViewById(R.id.ssTeam2DefenseB) }
    private val team3DefenseB: Button by lazy { findViewById(R.id.ssTeam3DefenseB) }

    private val team1DriverB: Button by lazy { findViewById(R.id.ssTeam1DriverB) }
    private val team2DriverB: Button by lazy { findViewById(R.id.ssTeam2DriverB) }
    private val team3DriverB: Button by lazy { findViewById(R.id.ssTeam3DriverB) }

    private val team1NotesT: EditText by lazy { findViewById(R.id.ssTeam1NotesT) }
    private val team2NotesT: EditText by lazy { findViewById(R.id.ssTeam2NotesT) }
    private val team3NotesT: EditText by lazy { findViewById(R.id.ssTeam3NotesT) }

    private val submitB: Button by lazy { findViewById(R.id.ssSubmitB) }

    private var allianceChanged = false
    private var firstRun = true

    override val helpMessage: String by lazy {
        """
            Watch the assigned alliance for offense, defense, and driver skill.
            Enter the match number before the match begins. Team numbers will automatically populate.
            Rank each team in each category from 1 to 3, compared to the other teams in the same alliance.
            Rank 1 means the best of that alliance for that match.
            Tap the buttons in the order in which the teams should be ranked, Tapping a button a second time clears it.
            Record any pertinent notes about each team in the provided text boxes.
            """.trimIndent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_superscout)
        updateAlliance()

        posText?.setOnClickListener { MainMenuSelection.openSettings(this) }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        submitB.setOnClickListener { submit() }

        team1OffenseB.setOnClickListener(
            OnRankPressed(
                team1Data,
                team2Data,
                team3Data,
                RankType.OFFENSE
            )
        )
        team1DefenseB.setOnClickListener(
            OnRankPressed(
                team1Data,
                team2Data,
                team3Data,
                RankType.DEFENSE
            )
        )
        team1DriverB.setOnClickListener(
            OnRankPressed(
                team1Data,
                team2Data,
                team3Data,
                RankType.DRIVER
            )
        )
        team2OffenseB.setOnClickListener(
            OnRankPressed(
                team2Data,
                team1Data,
                team3Data,
                RankType.OFFENSE
            )
        )
        team2DefenseB.setOnClickListener(
            OnRankPressed(
                team2Data,
                team1Data,
                team3Data,
                RankType.DEFENSE
            )
        )
        team2DriverB.setOnClickListener(
            OnRankPressed(
                team2Data,
                team1Data,
                team3Data,
                RankType.DRIVER
            )
        )
        team3OffenseB.setOnClickListener(
            OnRankPressed(
                team3Data,
                team1Data,
                team2Data,
                RankType.OFFENSE
            )
        )
        team3DefenseB.setOnClickListener(
            OnRankPressed(
                team3Data,
                team1Data,
                team2Data,
                RankType.DEFENSE
            )
        )
        team3DriverB.setOnClickListener(
            OnRankPressed(
                team3Data,
                team1Data,
                team2Data,
                RankType.DRIVER
            )
        )


        matchT?.addTextChangedListener(MatchTextChangeListener())
    }

    override fun onResume() {
        super.onResume()

        firstRun = false

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

        team1Data.position_id = if (redAlliance) {
            "Red 1"
        } else {
            "Blue 1"
        }
        team2Data.position_id = if (redAlliance) {
            "Red 2"
        } else {
            "Blue 2"
        }
        team3Data.position_id = if (redAlliance) {
            "Red 3"
        } else {
            "Blue 3"
        }

        if (matchT?.text?.length?.let { it > 0 } == true) {
            setMatch(matchT?.text.toString().toInt(), !allianceChanged)
        } else {
            clearData()
        }

        allianceChanged = false
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backDialog()
            }
        }

    fun backDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Cancel Data Entry?\nChanges will not be saved.")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _: DialogInterface?, _: Int -> this.finish() }
            .setNegativeButton(
                "No"
            ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override val resultForPrefs = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        schedule.updateSchedule(
            Prefs.getEvent(this, defaultEvent),
            this, false
        )
        updateAlliance()
    }

    private fun updateAlliance() {
        val newRedAlliance = !(Prefs.getPosition(this, "Red 1").contains("Blue"))

        if (newRedAlliance != redAlliance) {

            allianceChanged = !firstRun //only flag the alliance has changed if it isn't the first run

            redAlliance = newRedAlliance

            posText?.setTextColor(
                if (redAlliance) {
                    Color.RED
                } else {
                    Color.BLUE
                }
            )
            posText?.setText(
                if (redAlliance) {
                    R.string.red
                } else {
                    R.string.blue
                }
            )
        }
    }

    private fun loadData() {
        team1OffenseB.text =
            if (team1Data.offense_rank > 0) team1Data.offense_rank.toString() else ""
        team1DefenseB.text =
            if (team1Data.defense_rank > 0) team1Data.defense_rank.toString() else ""
        team1DriverB.text = if (team1Data.driver_rank > 0) team1Data.driver_rank.toString() else ""
        team1NotesT.setText(team1Data.notes)
        team2OffenseB.text =
            if (team2Data.offense_rank > 0) team2Data.offense_rank.toString() else ""
        team2DefenseB.text =
            if (team2Data.defense_rank > 0) team2Data.defense_rank.toString() else ""
        team2DriverB.text = if (team2Data.driver_rank > 0) team2Data.driver_rank.toString() else ""
        team2NotesT.setText(team2Data.notes)
        team3OffenseB.text =
            if (team3Data.offense_rank > 0) team3Data.offense_rank.toString() else ""
        team3DefenseB.text =
            if (team3Data.defense_rank > 0) team3Data.defense_rank.toString() else ""
        team3DriverB.text = if (team3Data.driver_rank > 0) team3Data.driver_rank.toString() else ""
        team3NotesT.setText(team3Data.notes)
    }

    private fun saveData() {
        //grid values are saved as they are entered
        //This leaves just notes

        team1Data.notes = team1NotesT.text.toString()
        team2Data.notes = team2NotesT.text.toString()
        team3Data.notes = team3NotesT.text.toString()
    }

    private fun setMatch(match: Int, resume: Boolean) {

        team1Data.match_id = match
        team2Data.match_id = match
        team3Data.match_id = match

        val alliance = if (redAlliance) {
            "Red"
        } else {
            "Blue"
        }

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

        if (resume) {
            loadData()
        } else if (!dataClear()) {
            loadDataDialog(match)
        } else {
            loadFromDb(match)
        }

    }

    private fun loadFromDb(match: Int) {

        val stats1 = db.getSuperScoutStats(
            team1Data.event_id,
            match,
            team1Data.team_id,
            team1Data.practice_match
        )
        val stats2 = db.getSuperScoutStats(
            team2Data.event_id,
            match,
            team2Data.team_id,
            team2Data.practice_match
        )
        val stats3 = db.getSuperScoutStats(
            team3Data.event_id,
            match,
            team3Data.team_id,
            team3Data.practice_match
        )

        stats1?.let { SuperScoutStatsYearly.copyData(it, team1Data) }
            ?: SuperScoutStatsYearly.clearContents(team1Data)
        stats2?.let { SuperScoutStatsYearly.copyData(it, team2Data) }
            ?: SuperScoutStatsYearly.clearContents(team2Data)
        stats3?.let { SuperScoutStatsYearly.copyData(it, team3Data) }
            ?: SuperScoutStatsYearly.clearContents(team3Data)

        loadData()
    }

    @SuppressLint("SetTextI18n")
    private fun submit() {
        saveData()

        if (team1Data.match_id <= 0) {
            val builder = AlertDialog.Builder(this)

            builder.setMessage(
                "No match number entered, please enter a match number\n This will cause you to lose data"
            )
                .setCancelable(true)
                .setPositiveButton(
                    "OK"
                ) { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                    matchT?.requestFocus()
                }
            builder.show()
            return
        }

        if (db.submitSuperScout(team1Data, team2Data, team3Data)) {
            clear()
            matchT?.setText((team1Data.match_id + 1).toString())
        } else {
            Toast.makeText(applicationContext, "Error in local database", Toast.LENGTH_LONG).show()
        }

    }

    private fun clear() {
        clearData()
        matchT?.setText("")
    }

    private fun clearData() {
        SuperScoutStatsYearly.clearContents(team1Data)
        SuperScoutStatsYearly.clearContents(team2Data)
        SuperScoutStatsYearly.clearContents(team3Data)

        team1RankingsT?.setText(R.string.team1)
        team1NotesLabelT?.setText(R.string.team1)
        team2RankingsT?.setText(R.string.team2)
        team2NotesLabelT?.setText(R.string.team2)
        team3RankingsT?.setText(R.string.team3)
        team3NotesLabelT?.setText(R.string.team3)

        loadData()
    }

    private fun dataClear(): Boolean {
        return team1OffenseB.text?.length?.let { it == 0 } == true
            && team1DefenseB.text?.length?.let { it == 0 } == true
            && team1DriverB.text?.length?.let { it == 0 } == true
            && team1NotesT.text?.length?.let { it == 0 } == true
            && team2OffenseB.text?.length?.let { it == 0 } == true
            && team2DefenseB.text?.length?.let { it == 0 } == true
            && team2DriverB.text?.length?.let { it == 0 } == true
            && team2NotesT.text?.length?.let { it == 0 } == true
            && team3OffenseB.text?.length?.let { it == 0 } == true
            && team3DefenseB.text?.length?.let { it == 0 } == true
            && team3DriverB.text?.length?.let { it == 0 } == true
            && team3NotesT.text?.length?.let { it == 0 } == true
    }

    private fun loadDataDialog(match: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("You had already entered data. Load new data?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _: DialogInterface?, _: Int ->
                loadFromDb(match)
            }
            .setNegativeButton(
                "No"
            ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun clearDataDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("You had already entered data. Clear form?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _: DialogInterface?, _: Int ->
                clearData()
            }
            .setNegativeButton(
                "No"
            ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private inner class MatchNumTask(match_num: Int) : Runnable {

        val matchNum: Int = match_num

        override fun run() {
            if (matchT?.text?.length?.let { it > 0 } == true
                && matchT?.text?.toString()?.toInt()?.let { it == matchNum } == true) {
                setMatch(matchNum, false)
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
                timer.postDelayed(task, loadDelay)
            } else if (dataClear()) {
                clearData()
                for (task in tasks) {
                    timer.removeCallbacks(task)
                }
                tasks.clear()
            } else {
                clearDataDialog()
                for (task in tasks) {
                    timer.removeCallbacks(task)
                }
                tasks.clear()
            }
        }
    }

    private enum class RankType { OFFENSE, DEFENSE, DRIVER }

    private inner class OnRankPressed(
        val myTeam: SuperScoutStats,
        val otherTeam1: SuperScoutStats,
        val otherTeam2: SuperScoutStats,
        val rankType: RankType
    ) : OnClickListener {

        override fun onClick(v: View?) {
            when (rankType) {
                RankType.OFFENSE -> {
                    if (myTeam.offense_rank > 0) {
                        myTeam.offense_rank = 0
                        (v as Button).text = ""
                    } else {
                        val otherTeamsHigh = maxOf(otherTeam1.offense_rank, otherTeam2.offense_rank)
                        val otherTeamsLow = minOf(otherTeam1.offense_rank, otherTeam2.offense_rank)
                        if ((otherTeamsHigh == 0 && otherTeamsLow == 0) || otherTeamsLow > 1) {
                            myTeam.offense_rank = 1
                            (v as Button).text = "1"
                        } else if (otherTeamsHigh == 1 && otherTeamsLow == 0) {
                            myTeam.offense_rank = 2
                            (v as Button).text = myTeam.offense_rank.toString()
                        } else if (otherTeamsLow == 0) {
                            myTeam.offense_rank = 1
                            (v as Button).text = myTeam.offense_rank.toString()
                        } else if (otherTeamsHigh == 2 && otherTeamsLow == 1) {
                            myTeam.offense_rank = 3
                            (v as Button).text = myTeam.offense_rank.toString()
                        } else if (otherTeamsLow == 1) {
                            myTeam.offense_rank = 2
                            (v as Button).text = myTeam.offense_rank.toString()
                        }
                    }
                }
                RankType.DEFENSE -> {
                    if (myTeam.defense_rank > 0) {
                        myTeam.defense_rank = 0
                        (v as Button).text = ""
                    } else {
                        val otherTeamsHigh = maxOf(otherTeam1.defense_rank, otherTeam2.defense_rank)
                        val otherTeamsLow = minOf(otherTeam1.defense_rank, otherTeam2.defense_rank)
                        if ((otherTeamsHigh == 0 && otherTeamsLow == 0) || otherTeamsLow > 1) {
                            myTeam.defense_rank = 1
                            (v as Button).text = "1"
                        } else if (otherTeamsHigh == 1 && otherTeamsLow == 0) {
                            myTeam.defense_rank = 2
                            (v as Button).text = myTeam.defense_rank.toString()
                        } else if (otherTeamsLow == 0) {
                            myTeam.defense_rank = 1
                            (v as Button).text = myTeam.defense_rank.toString()
                        } else if (otherTeamsHigh == 2 && otherTeamsLow == 1) {
                            myTeam.defense_rank = 3
                            (v as Button).text = myTeam.defense_rank.toString()
                        } else if (otherTeamsLow == 1) {
                            myTeam.defense_rank = 2
                            (v as Button).text = myTeam.defense_rank.toString()
                        }
                    }
                }
                RankType.DRIVER -> {
                    if (myTeam.driver_rank > 0) {
                        myTeam.driver_rank = 0
                        (v as Button).text = ""
                    } else {
                        val otherTeamsHigh = maxOf(otherTeam1.driver_rank, otherTeam2.driver_rank)
                        val otherTeamsLow = minOf(otherTeam1.driver_rank, otherTeam2.driver_rank)
                        if ((otherTeamsHigh == 0 && otherTeamsLow == 0) || otherTeamsLow > 1) {
                            myTeam.driver_rank = 1
                            (v as Button).text = "1"
                        } else if (otherTeamsHigh == 1 && otherTeamsLow == 0) {
                            myTeam.driver_rank = 2
                            (v as Button).text = myTeam.driver_rank.toString()
                        } else if (otherTeamsLow == 0) {
                            myTeam.driver_rank = 1
                            (v as Button).text = myTeam.driver_rank.toString()
                        } else if (otherTeamsHigh == 2 && otherTeamsLow == 1) {
                            myTeam.driver_rank = 3
                            (v as Button).text = myTeam.driver_rank.toString()
                        } else if (otherTeamsLow == 1) {
                            myTeam.driver_rank = 2
                            (v as Button).text = myTeam.driver_rank.toString()
                        }
                    }
                }
            }
        }

    }
}
