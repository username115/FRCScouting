package org.growingstems.scouting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PrefsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, Prefs())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
