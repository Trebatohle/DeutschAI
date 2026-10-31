package com.deutschai.app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal fun filterVocabularyNotebookEntries(
    entries: List<VocabularyEntry>,
    savedIds: Set<String>
): List<VocabularyEntry> = entries
    .filter { it.id in savedIds }
    .sortedBy { it.word.lowercase() }

internal object VocabularyNotebookStore {
    private const val PREFS = "deutschai_personal_vocabulary_v1"
    private const val SAVED_IDS = "saved_ids"

    private fun preferences(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun entries(context: Context): List<VocabularyEntry> {
        val savedIds = preferences(context).getStringSet(SAVED_IDS, emptySet()).orEmpty().toSet()
        return filterVocabularyNotebookEntries(vocabularyCatalog, savedIds)
    }

    fun count(context: Context): Int =
        preferences(context).getStringSet(SAVED_IDS, emptySet()).orEmpty().size

    fun isSaved(context: Context, entryId: String): Boolean =
        entryId in preferences(context).getStringSet(SAVED_IDS, emptySet()).orEmpty()

    /** Returns true when the entry is saved after the toggle, false when it was removed. */
    fun toggle(context: Context, entryId: String): Boolean {
        val saved = preferences(context).getStringSet(SAVED_IDS, emptySet()).orEmpty().toMutableSet()
        val isSaved = if (entryId in saved) {
            saved.remove(entryId)
            false
        } else {
            saved.add(entryId)
            true
        }
        preferences(context).edit().putStringSet(SAVED_IDS, saved.toSet()).apply()
        return isSaved
    }

    fun remove(context: Context, entryId: String) {
        val saved = preferences(context).getStringSet(SAVED_IDS, emptySet()).orEmpty().toMutableSet()
        if (saved.remove(entryId)) {
            preferences(context).edit().putStringSet(SAVED_IDS, saved.toSet()).apply()
        }
    }
}

@Composable
internal fun VocabularyNotebookScreen(
    onBack: () -> Unit,
    onPractice: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val speaker = rememberGermanSpeechPlayer()
    var savedEntries by remember { mutableStateOf(VocabularyNotebookStore.entries(context)) }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier.fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = ForestText),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Zpět")
                    Text("Zpět")
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("DEUTSCHAI", color = ForestText, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
                    Text("Můj osobní slovníček", color = Muted, fontSize = 13.sp)
                }
            }

            Text("Moje uložená slova", color = Ink, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Text(
                "Výrazy, které si ukládáš během procvičování. Slovníček zůstává uložený přímo v telefonu a funguje offline.",
                color = Muted, fontSize = 14.sp, lineHeight = 21.sp
            )

            if (savedEntries.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(7.dp)
                    ) {
                        Text("Zatím tu nemáš žádná slova.", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Otevři procvičování 2 000 slov a u zajímavého výrazu klepni na „Uložit do mého slovníčku“.",
                            color = Muted, fontSize = 13.sp, lineHeight = 19.sp
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${savedEntries.size} uložených slov", color = ForestText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = onPractice,
                        colors = ButtonDefaults.buttonColors(containerColor = ActionColor, contentColor = ActionText),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Procvičit", fontWeight = FontWeight.Bold) }
                }

                savedEntries.forEach { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(17.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp),
                            verticalArrangement = Arrangement.spacedBy(7.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Text("${entry.article} ${entry.word}", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text(entry.czech, color = Muted, fontSize = 14.sp)
                                }
                                Button(
                                    onClick = {
                                        VocabularyNotebookStore.remove(context, entry.id)
                                        savedEntries = VocabularyNotebookStore.entries(context)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = OptionSurface, contentColor = Ink),
                                    shape = RoundedCornerShape(10.dp)
                                ) { Text("Odebrat", fontSize = 12.sp) }
                            }
                            Text(vocabularySentenceGerman(entry), color = Ink, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            GermanAudioControls(player = speaker, text = vocabularySentenceGerman(entry))
                            Text(vocabularySentenceCzech(entry), color = Muted, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
