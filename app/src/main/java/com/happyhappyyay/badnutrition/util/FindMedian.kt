package com.happyhappyyay.badnutrition.util

import java.util.*


class MedianFinder {
    private val min = PriorityQueue<Float>()
    private val max  = PriorityQueue<Float>()

    fun addNum(num: Float) {
        if (min.isEmpty() || num <= -min.peek()!!) min.offer(-num)
        else max.offer(num)
        if (min.size > max.size + 1) max.offer(-min.poll()!!)
        else if (max.size > min.size) min.offer(-max.poll()!!)
    }

    fun findMedian(): Float {
        val even = (max.size + min.size) % 2 == 0
        return if (even) (max.peek()!! + (-min.peek()!!)) / 2F
        else -min.peek()!!
    }

}