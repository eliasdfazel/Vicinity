package net.geeksempire.vicinity.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.geeksempire.vicinity.android.databinding.EntryConfigurationViewBinding

class EntryConfiguration : AppCompatActivity() {

    lateinit var entryConfigurationViewBinding: EntryConfigurationViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryConfigurationViewBinding = EntryConfigurationViewBinding.inflate(layoutInflater)
        setContentView(entryConfigurationViewBinding.root)
    }
}