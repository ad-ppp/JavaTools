package com.jacky.view.node

import java.io.RandomAccessFile
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

/**
 * Created by Jacky on 2020/12/7
 */

private val TAG_MERGE: String? = "merge"
private const val TAG_INCLUDE = "include"
private const val TAG_1995 = "blink"
private const val TAG_REQUEST_FOCUS = "requestFocus"
private const val TAG_TAG = "tag"


const val ATTR_CONTENT_DESCRIPTION = "android:contentDescription"

val skipTags = mutableListOf(
    TAG_MERGE,
    TAG_INCLUDE,
    TAG_1995,
    TAG_REQUEST_FOCUS,
    TAG_TAG
).apply { Collections.unmodifiableCollection(this) }

interface IDRule {
    fun genId(): String
}

class UUIDRule : IDRule {
    override fun genId(): String = UUID.randomUUID().toString()
}

class ViewNodeAnalyzer(private val rule: IDRule? = UUIDRule()) {
    private val p = Pattern.compile("\\S+=\"(.*?)\"")


    fun start(path: String) {
        val raf = RandomAccessFile(path, "rw")

        var depth = 0
        var root: Node? = null
        var currentNode: Node? = null
        var parent: Node? = null

        var lastDepth = 0
        var line: String?

        while (raf.readLine().apply { line = this?.trim() } != null) {
            val l = line?.trim() ?: return
            if (l.startsWith("<?") || l.isEmpty()) {
                continue
            }

            when {
                // end
                l.endsWith("/>") || l.startsWith("</") -> {
                    depth--
                    val n = currentNode ?: return
                    handleContent(l
                        .replace("/>", "")
                        .replace("</", ""), n)
                    doEndTag(n, raf)
                }
                l.startsWith("<") -> {
                    depth++

                    val tag = l.replace("<", "").split(" ")[0]
                    val n = Node(tag, depth)

                    // parent
                    when {
                        lastDepth + 1 == depth -> {
                            // last Node
                            currentNode?.addChild(n)
                        }
                        lastDepth == depth -> {
                            currentNode?.parent?.addChild(n)
                        }
                        lastDepth - 1 == depth -> {
                            var p = currentNode?.parent
                            while (p?.depth != depth - 1) {
                                p = p?.parent
                            }
                            p.addChild(n)
                        }
                    }

                    n.offset = raf.filePointer
                    n.parent = parent
                    if (root == null) {
                        root = n
                    }

                    lastDepth = depth
                    currentNode = n
                    handleContent(l.replace("<${tag}", ""), currentNode)
                }
                else -> {
                    if (handleContent(l, currentNode)) return
                }
            }
        }
        assert(depth == 0)
    }

    private fun handleContent(l: String, currentNode: Node?): Boolean {
        val sp = ArrayList<String>()
        val matcher = p.matcher(l
            .replace("<", "")
            .replace(">", ""))
        while (matcher.find()) {
            sp.add(matcher.group(0))
        }

        if (sp.isEmpty()) {
            return true
        }

        var i = 0
        while (i < sp.size) {
            currentNode?.readLine(sp[i])
            i++
        }
        return false
    }

    private fun doEndTag(n: Node, raf: RandomAccessFile) {
        if (n.hasContentDescription()) {
            // android:
            val contentD = " android:contentDescription=\"${rule?.genId()}\" "
            val length = contentD.length

            val filePointer = raf.filePointer + length
            raf.seek(n.offset)
            raf.writeChars(contentD)
            raf.seek(filePointer)
        }
    }
}