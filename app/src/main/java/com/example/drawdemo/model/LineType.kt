package com.example.drawdemo.model

import com.example.drawdemo.R

enum class LineType(val imgRes: Int) {
    VERTICAL(R.drawable.pipe_l),                 // y
    HORIZONTAL(R.drawable.pipe_),                // x

    UP_FROM_LEFT(R.drawable.pipe_top_left),      // x
    DOWN_TO_RIGHT(R.drawable.pipe_top_right),    // y
    DOWN_FROM_LEFT(R.drawable.pipe_bottom_left), // x
    UP_TO_RIGHT(R.drawable.pipe_bottom_right),   // y

}