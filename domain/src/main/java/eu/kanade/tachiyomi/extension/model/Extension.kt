package eu.kanade.tachiyomi.extension.model

import android.graphics.drawable.Drawable
import eu.kanade.tachiyomi.source.AnimeSource
import eu.kanade.tachiyomi.source.Source
import mihon.domain.extension.model.ExtensionStore
import tachiyomi.domain.source.model.StubSource

sealed class Extension {

    abstract val name: String
    abstract val pkgName: String
    abstract val versionName: String
    abstract val versionCode: Long
    abstract val libVersion: Double
    abstract val lang: String?
    abstract val isNsfw: Boolean
    abstract val type: ExtensionType

    sealed class Installed : Extension() {
        abstract val pkgFactory: String?
        abstract val icon: Drawable?
        abstract val hasUpdate: Boolean
        abstract val isObsolete: Boolean
        abstract val isShared: Boolean
        abstract val store: ExtensionStore?
    }

    data class InstalledManga(
        override val name: String,
        override val pkgName: String,
        override val versionName: String,
        override val versionCode: Long,
        override val libVersion: Double,
        override val lang: String,
        override val isNsfw: Boolean,
        override val type: ExtensionType = ExtensionType.MANGA,
        override val pkgFactory: String?,
        val sources: List<Source>,
        override val icon: Drawable?,
        override val hasUpdate: Boolean = false,
        override val isObsolete: Boolean = false,
        override val isShared: Boolean,
        override val store: ExtensionStore? = null,
    ) : Installed()

    data class InstalledAnime(
        override val name: String,
        override val pkgName: String,
        override val versionName: String,
        override val versionCode: Long,
        override val libVersion: Double,
        override val lang: String,
        override val isNsfw: Boolean,
        override val type: ExtensionType = ExtensionType.ANIME,
        override val pkgFactory: String?,
        val sources: List<AnimeSource>,
        override val icon: Drawable?,
        override val hasUpdate: Boolean = false,
        override val isObsolete: Boolean = false,
        override val isShared: Boolean,
        override val store: ExtensionStore? = null,
    ) : Installed()

    sealed class Available : Extension() {
        abstract val apkUrl: String
        abstract val iconUrl: String
        abstract val store: ExtensionStore

        sealed class SourceItem {
            abstract val id: Long
            abstract val lang: String
            abstract val name: String
            abstract val baseUrl: String
        }
    }

    data class AvailableManga(
        override val name: String,
        override val pkgName: String,
        override val versionName: String,
        override val versionCode: Long,
        override val libVersion: Double,
        override val lang: String,
        override val isNsfw: Boolean,
        override val type: ExtensionType = ExtensionType.MANGA,
        val sources: List<Source>,
        override val apkUrl: String,
        override val iconUrl: String,
        override val store: ExtensionStore,
    ) : Available() {

        data class Source(
            override val id: Long,
            override val lang: String,
            override val name: String,
            override val baseUrl: String,
        ) : SourceItem() {
            fun toStubSource(): StubSource {
                return StubSource(
                    id = this.id,
                    lang = this.lang,
                    name = this.name,
                )
            }
        }
    }

    data class AvailableAnime(
        override val name: String,
        override val pkgName: String,
        override val versionName: String,
        override val versionCode: Long,
        override val libVersion: Double,
        override val lang: String,
        override val isNsfw: Boolean,
        override val type: ExtensionType = ExtensionType.ANIME,
        val sources: List<Source>,
        override val apkUrl: String,
        override val iconUrl: String,
        override val store: ExtensionStore,
    ) : Available() {

        data class Source(
            override val id: Long,
            override val lang: String,
            override val name: String,
            override val baseUrl: String,
        ) : SourceItem()
    }

    data class Untrusted(
        override val name: String,
        override val pkgName: String,
        override val versionName: String,
        override val versionCode: Long,
        override val libVersion: Double,
        val signatureHash: String,
        override val lang: String? = null,
        override val isNsfw: Boolean = false,
        override val type: ExtensionType,
    ) : Extension()
}
