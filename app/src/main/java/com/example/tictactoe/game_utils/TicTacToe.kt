package com.example.tictactoe.game_utils

object TicTacToe {

    fun isAnyPlayerWin(list: MutableList<MutableList<String>>): Boolean {
        return isWin(list) || isCrossWin(list)
    }

    private fun isCrossWin(list: MutableList<MutableList<String>>): Boolean {
        val end = list.lastIndex
        for (i in 0..end) {
            var endCt = list.lastIndex
            var isWin: Boolean
            val crossMidLeft = mutableListOf<String>()
            val crossMidRight = mutableListOf<String>()
            val crossLeft = mutableListOf<String>()
            val crossRight = mutableListOf<String>()

            for ((start, j) in (i..end).withIndex()) {
                crossLeft.add(list[j][start])
                crossRight.add(list[j][endCt])

                crossMidLeft.add(list[start][j])
                crossMidRight.add(list[start][end - j])
                --endCt
            }

            for (indexCo in 0..crossMidLeft.size) {
                isWin = crossLeft.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
                if (isWin) {
                    return true
                }
                isWin = crossRight.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
                if (isWin) {
                    return true
                }

                isWin = crossMidLeft.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
                if (isWin) {
                    return true
                }
                isWin = crossMidRight.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
                if (isWin) {
                    return true
                }
            }
        }
        return false
    }

    private fun isWin(list: MutableList<MutableList<String>>): Boolean {
        var isWin: Boolean
        for (col in list.indices) {
            val colList = mutableListOf<String>()
            for (index in list.indices) {
                colList.add(list[index][col])
            }
            for (indexCo in 0..colList.size) {
                isWin = colList.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
                if (isWin) {
                    return true
                }
                isWin = list[col].compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
                if (isWin) {
                    return true
                }
            }
        }
        return false
    }

    fun MutableList<String>.compareRowOrColAndReturnIsWin(from: Int = 0, isFrom3By3Grid: Boolean = true): Boolean {
        return if (this.size == 3 && isFrom3By3Grid) {
            this.all { it == "X" } || this.all { it == "O" }
        } else if (this.size in 4..7) {
            val till = from + 3

            if (till <= this.lastIndex) {
                val sliceRange = this.slice(from..till)
                sliceRange.all { it == "X" } || sliceRange.all { it == "O" }
            } else false

        } else false
    }
}

//private fun isCrossWin1(list: MutableList<MutableList<String>>): Boolean {
//    val crossLeft = mutableListOf<MutableList<String>>()
//    val crossRight = mutableListOf<MutableList<String>>()
//
//    var end = list.lastIndex
//    for (co in list.indices) {
//        crossLeft.add(list[co].slice(0..co).toMutableList())
//        crossRight.add(list[co].slice(end..list.lastIndex).toMutableList())
//        --end
//    }
//
//    for (i in crossLeft.indices) {
//        val acl = mutableListOf<String>()
//        val acr = mutableListOf<String>()
//        for (j in crossLeft.indices) {
//            try {
//                acl.add(crossLeft[j].removeLast())
//                acr.add(crossRight[j].removeFirst())
//            } catch (e: Exception) {
//                e.localizedMessage
//            }
//        }
//        var isWin: Boolean
//        for (indexCo in acl.indices) {
//            isWin = acl.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
//            if (isWin) {
//                return true
//            }
//            isWin = acr.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
//            if (isWin) {
//                return true
//            }
//        }
//    }
//
//
//    for (i in (1..list.lastIndex)) {
//        val cml = mutableListOf<String>()
//        val cmr = mutableListOf<String>()
//        for (j in i..list.lastIndex) {
//            cml.add(list[j - i][j])
//            cmr.add(list[j - i][list.lastIndex - j])
//        }
//        var isWin: Boolean
//        for (indexCo in cml.indices) {
//            isWin = cml.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
//            if (isWin) {
//                return true
//            }
//            isWin = cmr.compareRowOrColAndReturnIsWin(indexCo, list.size == 3)
//            if (isWin) {
//                return true
//            }
//        }
//    }
//
//    return false
//}




