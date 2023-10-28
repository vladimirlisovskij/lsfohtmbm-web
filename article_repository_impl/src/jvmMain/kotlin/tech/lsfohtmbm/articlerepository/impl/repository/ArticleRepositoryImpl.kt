package tech.lsfohtmbm.articlerepository.impl.repository

import tech.lsfohtmbm.article.Article
import tech.lsfohtmbm.article.Paragraph
import tech.lsfohtmbm.article.Text
import tech.lsfohtmbm.articlerepository.api.ArticleRepository
import tech.lsfohtmbm.articlerepository.impl.database.generated.ArticlesDatabase

internal class ArticleRepositoryImpl(
    private val database: ArticlesDatabase
) : ArticleRepository {
    override fun getDynamicArticle(id: Long): Article {
        TODO("Not yet implemented")
    }

    private fun mapToParagraphList(rawText: String): List<Paragraph> {
        val paragraphs = mutableListOf<Paragraph>()
        var prevSeparatorIndex = 0
        while (true) {
            val typeEndIndex = rawText.indexOf('|', prevSeparatorIndex)
            if (typeEndIndex == -1) break
            val type = rawText.substring(prevSeparatorIndex + 1, typeEndIndex)
            val textEndIndex = rawText.indexOf('\n', typeEndIndex)
            val text = rawText.substring(typeEndIndex + 1, textEndIndex)
            paragraphs.add(Text(text))

            prevSeparatorIndex = textEndIndex
        }

        return paragraphs
    }
}

