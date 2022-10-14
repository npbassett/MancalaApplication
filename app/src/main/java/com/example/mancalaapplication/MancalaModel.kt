package com.example.mancalaapplication

data class MancalaModel(
    var pocket_bottom1_stones: Int = 4,
    var pocket_bottom2_stones: Int = 4,
    var pocket_bottom3_stones: Int = 4,
    var pocket_bottom4_stones: Int = 4,
    var pocket_bottom5_stones: Int = 4,
    var pocket_bottom6_stones: Int = 4,
    var store_bottom_stones: Int = 0,
    var pocket_top1_stones: Int = 4,
    var pocket_top2_stones: Int = 4,
    var pocket_top3_stones: Int = 4,
    var pocket_top4_stones: Int = 4,
    var pocket_top5_stones: Int = 4,
    var pocket_top6_stones: Int = 4,
    var store_top_stones: Int = 0
)