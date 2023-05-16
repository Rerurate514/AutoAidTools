package com.example.autoaidtools

import androidx.lifecycle.ViewModel



class ViewModel : ViewModel(){

    private val arraySize = 16
    private val deleteTarget = -1
    private val noMeaningNum = 0
    val oneTap = 1
    val nDelay = 2

    /** list number mean ...
     *  $deleteTarget   ->  aligning target number after it is deleted
     *  $noMeaningNum   ->  no meaning
     *  $oneTap         ->  auto tap mode
     *  $nDelay         ->  n second delay mode
     */

    private var _doAutoCycleList : Array<Int> = Array(arraySize) { noMeaningNum }
        val doAutoCycleList : Array<Int>
            get() = _doAutoCycleList

    private var _delayTimeList : Array<Int> = Array(arraySize) { noMeaningNum }
    val delayTimeList : Array<Int>
        get() = _delayTimeList

    fun addCycleList(num: Int, delayTime: Int = noMeaningNum){
        _doAutoCycleList += num
        _delayTimeList += delayTime
    }

    fun insertCycleList(num: Int, index: Int, delayTime: Int = noMeaningNum){
        _doAutoCycleList[index] = num
        _delayTimeList[index] = delayTime
    }

    fun removeCycleList(index: Int){
        _doAutoCycleList[index] = deleteTarget
        _delayTimeList[index] = deleteTarget

        listAlign()
    }

    private fun listAlign(){
        for(i in _doAutoCycleList.indices){
            if(_doAutoCycleList[i] == deleteTarget){
                _doAutoCycleList[i] = _doAutoCycleList[i + 1]
                _doAutoCycleList[i + 1] = noMeaningNum

                _delayTimeList[i] = _delayTimeList[i + 1]
                _delayTimeList[i + 1] = noMeaningNum
            }
        }
    }
}