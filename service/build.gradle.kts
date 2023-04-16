buildscript {
    project.extra.set("scm.commit.hash",
        Runtime.getRuntime().exec("git rev-parse --verify --short HEAD").apply { waitFor() }
            .inputStream.reader().readText().replace("\n", "")
    )
    project.extra.set("package.group", "com.klimek.langsapp")
}
