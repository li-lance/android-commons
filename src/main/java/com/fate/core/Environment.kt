package com.fate.core

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * Author：lance.li on 3/4/21 10:49
 * email：foryun@live.com
 */
data class Environment(val context: Context, val host: String, val build: Build, val kv: MMKV)
