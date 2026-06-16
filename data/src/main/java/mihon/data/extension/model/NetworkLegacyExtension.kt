package mihon.data.extension.model

import android.annotation.SuppressLint
import eu.kanade.tachiyomi.extension.model.Extension
import eu.kanade.tachiyomi.extension.model.ExtensionType
import kotlinx.serialization.Serializable
import mihon.domain.extension.model.ExtensionStore

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NetworkLegacyExtension(
    val name: String,
    val pkg: String,
    val apk: String,
    val lang: String,
    val code: Long,
    val version: String,
    val nsfw: Int,
    val type: String? = null,
    val sources: List<Source>?,
) {
    @Serializable
    data class Source(
        val id: Long,
        val lang: String,
        val name: String,
        val baseUrl: String,
    )

    fun toAvailableExtension(store: ExtensionStore, storeBaseUrl: String): Extension.Available {
        return when (ExtensionType.fromMetadataValue(type) ?: ExtensionType.MANGA) {
            ExtensionType.MANGA -> Extension.AvailableManga(
                name = name.substringAfter("Tachiyomi: "),
                pkgName = pkg,
                apkUrl = "$storeBaseUrl/apk/$apk",
                iconUrl = "$storeBaseUrl/icon/$pkg.png",
                libVersion = version.substringBeforeLast('.').toDouble(),
                versionCode = code,
                versionName = version,
                lang = lang,
                isNsfw = nsfw == 1,
                sources = if (sources.isNullOrEmpty()) {
                    listOf(
                        Extension.AvailableManga.Source(
                            id = 0,
                            name = name,
                            lang = lang,
                            baseUrl = "",
                        ),
                    )
                } else {
                    sources.map { source ->
                        Extension.AvailableManga.Source(
                            id = source.id,
                            name = source.name,
                            lang = source.lang,
                            baseUrl = source.baseUrl,
                        )
                    }
                },
                store = store,
            )

            ExtensionType.ANIME -> Extension.AvailableAnime(
                name = name.substringAfter("Tachiyomi: "),
                pkgName = pkg,
                apkUrl = "$storeBaseUrl/apk/$apk",
                iconUrl = "$storeBaseUrl/icon/$pkg.png",
                libVersion = version.substringBeforeLast('.').toDouble(),
                versionCode = code,
                versionName = version,
                lang = lang,
                isNsfw = nsfw == 1,
                sources = sources.orEmpty().map { source ->
                    Extension.AvailableAnime.Source(
                        id = source.id,
                        name = source.name,
                        lang = source.lang,
                        baseUrl = source.baseUrl,
                    )
                },
                store = store,
            )
        }
    }
}
