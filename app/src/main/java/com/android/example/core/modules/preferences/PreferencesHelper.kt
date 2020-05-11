package com.android.example.core.modules.preferences

import android.content.SharedPreferences
import com.google.gson.Gson

class PreferencesHelper(private val preferences: SharedPreferences) {

    fun putString(key: String, value: String): Boolean {
        return this.preferences.edit().putString(key, value).commit()
    }

    fun getString(key: String): String? {
        val str = this.preferences.getString(key, "")
        return if (str.isNullOrEmpty()) null else str
    }

    fun putStringSet(key: String, value: Set<String>): Boolean {
        return this.preferences.edit().putStringSet(key, value).commit()
    }

    fun getStringSet(key: String): Set<String> {
        val str = this.preferences.getStringSet(key, emptySet())
        return str.orEmpty()
    }

    fun putBool(key: String, value: Boolean): Boolean {
        return this.preferences.edit().putBoolean(key, value).commit()
    }

    fun getBool(key: String, defaultValue: Boolean = false): Boolean {
        return this.preferences.getBoolean(key, defaultValue)
    }

    fun putInt(key: String, value: Int): Boolean {
        return this.preferences.edit().putInt(key, value).commit()
    }

    fun getInt(key: String, defaultValue: Int = -1): Int {
        return this.preferences.getInt(key, defaultValue)
    }

    fun putLong(key: String, value: Long): Boolean {
        return this.preferences.edit().putLong(key, value).commit()
    }

    fun getLong(key: String, defaultValue: Long = -1L): Long {
        return this.preferences.getLong(key, defaultValue)
    }

    fun remove(key: String): Boolean {
        return this.preferences.edit().remove(key).commit()
    }

    fun isEmpty(key: String): Boolean {
        return !this.preferences.contains(key)
    }

    inline fun <reified Obj> putObject(key: String, obj : Obj?) {
        val json = Gson().toJson(obj, Obj::class.java)
        putString(key, json)
    }

    inline fun <reified Obj> getObject(key: String) : Obj? {
        val json = getString(key)
        return Gson().fromJson(json, Obj::class.java)
    }

    fun clearAll(): Boolean {
        return this.preferences.edit().clear().commit()
    }


    fun getBoolDefaultTrue(key: String): Boolean {
        return this.preferences.getBoolean(key, true)
    }

    fun clear(): Boolean {
        return this.preferences.edit().clear().commit()
    }

    fun getCount(key: String): String {
        return this.preferences.getString(key, "3").orEmpty()
    }

    fun getMarkId(key: String): String {
        return this.preferences.getString(key, "5").orEmpty()
    }

    fun getMarkName(key: String): String {
        return this.preferences.getString(key, "А-95").orEmpty()
    }

    fun getTypeMark(key: String): String {
        return this.preferences.getString(key, "0").orEmpty()
    }

    fun getInfo(key: String) : String{
        return this.preferences.getString(key, "Тип пролета: Воздушный\n" +
            "Марка кабеля: А-95\n" +
            "Количество жил: 3").orEmpty()
    }
}