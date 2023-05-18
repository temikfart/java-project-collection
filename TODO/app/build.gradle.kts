plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
}

application {
    mainClass.set("todo.TODO")
}

