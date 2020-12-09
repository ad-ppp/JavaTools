package com.jacky.view.node

/**
 * Created by Jacky on 2020/12/7
 */
class Node(val tag: String, val depth: Int) {
    /**
     * es:
     * app:layout_constraintLeft_toLeftOf="parent"
     * android:layout_height="100dp"
     */
    private val attrs = ArrayList<String>()
    private val values = ArrayList<String>()
    private val children = ArrayList<Node>()
    var offset = 0L


    var parent: Node? = null
    fun addChild(node: Node) {
        children.add(node)
    }

    /**
     * app:layout_constraintLeft_toLeftOf="parent"
     *
     * attr=>app:layout_constraintLeft_toLeftOf
     * value=>parent
     */
    fun readLine(line: String) {
        val split = line.trim().split("=")
        println("line:$line")
        attrs.add(split[0])
        values.add(split[1])
    }

    fun hasContentDescription(): Boolean {
        check()
        return attrs.contains(ATTR_CONTENT_DESCRIPTION)
    }

    private fun check() = attrs.size == values.size


}