buildscript {

    // region Builds scripts constants

    val localPropertiesFilename = "local.properties"

    // endregion

    java.util.Properties()
        .apply { file(localPropertiesFilename).run { if (exists()) load(reader()) } }
        .forEach { project.extra.set(it.key.toString(), it.value) }
}
