package org.growingstems.scouting

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.frc836.database.DBActivity

class SuperScoutActivity : DBActivity() {

    private var redAlliance = true
    private val posText: TextView? by lazy { findViewById(R.id.pos) }

    override val helpMessage: String by lazy {
        """
            FIXME
            """.trimIndent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_superscout)
        updateAlliance()
    }

    override val resultForPrefs = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        updateAlliance()
    }

    private fun updateAlliance() {
        redAlliance = !(Prefs.getPosition(this, "Red 1").contains("Blue"))

        posText?.setTextColor(
            if (redAlliance) { Color.RED }
            else { Color.BLUE }
        )
        posText?.text = (
            if (redAlliance) { "Red" }
            else { "Blue" }
        )
    }
}
