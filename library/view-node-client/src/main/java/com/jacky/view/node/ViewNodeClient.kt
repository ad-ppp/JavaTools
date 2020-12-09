package com.jacky.view.node

fun main(args: Array<String>) {
    val client =  ViewNodeClient()
    client.start()
    println("Hello, world!111")
}

class ViewNodeClient {
    fun start() {
        val analyzer = ViewNodeAnalyzer()
        analyzer.start("/Users/yangjianfei/myWidget/android/ad-ppp/JavaTools/library/view-node-client/gen/activity_main.xml")
    }
}