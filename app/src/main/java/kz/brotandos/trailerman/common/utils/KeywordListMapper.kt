package kz.brotandos.trailerman.common.utils

import kz.brotandos.trailerman.common.models.Keyword

object KeywordListMapper {
    fun mapToStringList(keywords: List<Keyword>): List<String> {
        var list: MutableList<String> = ArrayList()
        keywords.forEach { list.add(it.name) }
        if (list.size > 7) {
            list = list.subList(0, 6)
        }
        return list
    }
}