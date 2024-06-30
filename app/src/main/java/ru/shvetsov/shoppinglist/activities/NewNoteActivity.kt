package ru.shvetsov.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.ActivityNewNoteBinding
import ru.shvetsov.shoppinglist.entities.NoteItem
import ru.shvetsov.shoppinglist.fragments.NoteFragment
import ru.shvetsov.shoppinglist.utils.HtmlManager
import java.io.Serializable
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import ru.shvetsov.shoppinglist.utils.ColorPickerTouchListener
import ru.shvetsov.shoppinglist.utils.TimeManager.getCurrentTime

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var defaultPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        defaultPreference = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        actionBarSettings()
        getNote()
        init()
        setTextSize()
        onClickColor()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(ColorPickerTouchListener())
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun getNote() {
        note = intent.serializable(NoteFragment.NEW_NOTE_KEY)
        fillNote()
    }

    private fun fillNote() = with(binding) {
        if (note != null) {
            edTitle.setText(note?.title)
            edDescription.setText(HtmlManager.getTextFromHtml(note?.content!!).trim())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                setMainResult()
            }
            android.R.id.home -> {
                finish()
            }
            R.id.bold -> {
                setBoldForSelectedText()
            }
            R.id.colorPicker -> {
                if (binding.colorPicker.isShown) {
                    closeColorPicker()
                } else {
                    openColorPicker()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() = with(binding) {
        val startPosition = edDescription.selectionStart
        val endPosition = edDescription.selectionEnd
        val styles = edDescription.text.getSpans(startPosition, endPosition, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null

        if (styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        edDescription.text.setSpan(
            boldStyle,
            startPosition,
            endPosition,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        edDescription.text.trim()
        edDescription.setSelection(startPosition)
    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val startPosition = edDescription.selectionStart
        val endPosition = edDescription.selectionEnd
        val styles =
            edDescription.text.getSpans(startPosition, endPosition, ForegroundColorSpan::class.java)

        if (styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])

        edDescription.text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity, colorId)),
            startPosition,
            endPosition,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        edDescription.text.trim()
        edDescription.setSelection(startPosition)
    }

    private fun actionBarSettings() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            createNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val intent = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.textToHtml(edDescription.text)
        )
    }

    private fun createNewNote(): NoteItem {
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.textToHtml(binding.edDescription.text),
            getCurrentTime(),
            ""
        )
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun onClickColor() = with(binding) {
        ibRed.setOnClickListener {
            setColorForSelectedText(R.color.picker_red)
        }
        ibBlue.setOnClickListener {
            setColorForSelectedText(R.color.picker_blue)
        }
        ibBlack.setOnClickListener {
            setColorForSelectedText(R.color.picker_black)
        }
        ibGreen.setOnClickListener {
            setColorForSelectedText(R.color.picker_green)
        }
        ibOrange.setOnClickListener {
            setColorForSelectedText(R.color.picker_orange)
        }
        ibYellow.setOnClickListener {
            setColorForSelectedText(R.color.picker_yellow)
        }
    }

    private fun EditText.setTextSize(size: String?) {
        if (size != null) this.textSize = size.toFloat()
    }

    private fun setTextSize() = with(binding){
        edTitle.setTextSize(sharedPreferences?.getString("title_size_key", "16"))
        edDescription.setTextSize(sharedPreferences?.getString("content_size_key", "14"))
    }

    private fun getSelectedTheme(): Int {
        return if (defaultPreference.getString("theme_key", "green") == "green") {
            R.style.Theme_NewNoteGreen
        } else {
            R.style.Theme_NewNoteBlue
        }
    }
}