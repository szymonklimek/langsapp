buildscript {

    // region Builds scripts constants

    val containerRegistryPropertyKey = "container.registry.url"
    val localPropertiesFilename = "local.properties"

    // endregion

    java.util.Properties()
        .apply {
            file(localPropertiesFilename).run { if (exists()) load(reader()) }
        }
        .forEach { project.extra.set(it.key.toString(), it.value) }

    findProperty(containerRegistryPropertyKey)
        ?: println(
            """
                _____________________________________
                IMPORTANT: Container Registry URL is missing. 
                Provide '$containerRegistryPropertyKey' in project properties, for example in 'local.properties' file.
                _____________________________________
            """.trimIndent()
        )
}
