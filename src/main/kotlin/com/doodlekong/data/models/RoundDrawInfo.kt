package com.doodlekong.data.models

import com.doodlekong.other.Constants.TYPE_CUR_ROUND_DRAW_INFO

data class RoundDrawInfo(
    val data: List<String>
) : BaseModel(type = TYPE_CUR_ROUND_DRAW_INFO)
