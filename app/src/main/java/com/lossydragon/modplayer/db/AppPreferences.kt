package com.lossydragon.modplayer.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.helllabs.libxmp.Xmp
import timber.log.Timber

private val Context.dataStore by preferencesDataStore(
    name = "xmp_prefs",
    corruptionHandler = ReplaceFileCorruptionHandler {
        Timber.e("Preferences corrupted, resetting.")
        emptyPreferences()
    }
)

class AppPreferences(context: Context) {

    private val dataStore: DataStore<Preferences> = context.dataStore

    private val lastDirectoryUri = stringPreferencesKey("last_directory_uri")
    private val sampleRate = intPreferencesKey("sample_rate")
    private val bufferMs = intPreferencesKey("buffer_ms")
    private val defaultPan = intPreferencesKey("default_pan")
    private val volumeBoost = intPreferencesKey("volume_boost")
    private val stereoMix = intPreferencesKey("stereo_mix")
    private val dspEffects = intPreferencesKey("dsp_effects")
    private val playerVolume = intPreferencesKey("player_volume")
    private val interpolationType = intPreferencesKey("interpolation_type")
    private val playerFlags = intPreferencesKey("player_flags")

    private fun <T> flow(key: Preferences.Key<T>, default: T): Flow<T> =
        dataStore.data.map { it[key] ?: default }

    private fun <T> flowNullable(key: Preferences.Key<T>): Flow<T?> =
        dataStore.data.map { it[key] }

    private suspend fun <T> get(key: Preferences.Key<T>, default: T): T =
        dataStore.data.map { it[key] ?: default }.firstOrNull() ?: default

    private suspend fun <T> set(key: Preferences.Key<T>, value: T) =
        dataStore.edit { it[key] = value }

    fun getLastDirectoryFlow() = flowNullable(lastDirectoryUri)
    suspend fun getLastDirectoryUri() = get(lastDirectoryUri, "").ifBlank { null }
    suspend fun setLastDirectoryUri(v: String) = set(lastDirectoryUri, v)

    fun getSampleRateFlow() = flow(sampleRate, Xmp.DEFAULT_SAMPLE_RATE)
    suspend fun getSampleRate() = get(sampleRate, Xmp.DEFAULT_SAMPLE_RATE)
    suspend fun setSampleRate(v: Int) = set(sampleRate, v)

    fun getBufferMsFlow() = flow(bufferMs, Xmp.DEFAULT_BUFFER_MS)
    suspend fun getBufferMs() = get(bufferMs, Xmp.DEFAULT_BUFFER_MS)
    suspend fun setBufferMs(v: Int) = set(bufferMs, v)

    fun getDefaultPanFlow() = flow(defaultPan, Xmp.DEFAULT_PAN_SEPARATION)
    suspend fun getDefaultPan() = get(defaultPan, Xmp.DEFAULT_PAN_SEPARATION)
    suspend fun setDefaultPan(v: Int) = set(defaultPan, v)

    fun getVolumeBoostFlow() = flow(volumeBoost, Xmp.DEFAULT_VOLUME_BOOST)
    suspend fun getVolumeBoost() = get(volumeBoost, Xmp.DEFAULT_VOLUME_BOOST)
    suspend fun setVolumeBoost(v: Int) = set(volumeBoost, v)

    fun getStereoMixFlow() = flow(stereoMix, Xmp.DEFAULT_STEREO_MIX)
    suspend fun getStereoMix() = get(stereoMix, Xmp.DEFAULT_STEREO_MIX)
    suspend fun setStereoMix(v: Int) = set(stereoMix, v)

    fun getDspEffectFlow() = flow(dspEffects, Xmp.XMP_DSP_LOWPASS)
    suspend fun getDspEffect() = get(dspEffects, Xmp.XMP_DSP_LOWPASS)
    suspend fun setDspEffect(v: Int) = set(dspEffects, v)

    fun getPlayerVolumeFlow() = flow(playerVolume, Xmp.DEFAULT_PLAYER_VOLUME)
    suspend fun getPlayerVolume() = get(playerVolume, Xmp.DEFAULT_PLAYER_VOLUME)
    suspend fun setPlayerVolume(v: Int) = set(playerVolume, v)

    fun getInterpolationTypeFlow() = flow(interpolationType, Xmp.DEFAULT_INTERPOLATION)
    suspend fun getInterpolationType() = get(interpolationType, Xmp.DEFAULT_INTERPOLATION)
    suspend fun setInterpolationType(v: Int) = set(interpolationType, v)

    fun getPlayerFlagsFlow() = flow(playerFlags, 0)
    suspend fun getPlayerFlags() = get(playerFlags, 0)
    suspend fun setPlayerFlags(v: Int) = set(playerFlags, v)
}
