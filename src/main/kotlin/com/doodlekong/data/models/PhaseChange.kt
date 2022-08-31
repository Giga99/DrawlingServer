package com.doodlekong.data.models

import com.doodlekong.data.Room
import com.doodlekong.other.Constants.TYPE_PHASE_CHANGE

data class PhaseChange(
    var phase: Room.Phase?,
    var time: Long,
    val drawingPlayer: String? = null
) : BaseModel(type = TYPE_PHASE_CHANGE)
