# vertx-whisky-crud

Simple CRUD tryout based on *Vert.x Gradle Starter*

### Start in hot reload mode
```
./gradlew run
```

This last command relaunches Gradle and the application as soon as you change something in `src/main`.

### Building the project

To build the project, just use:

```
./gradlew shadowJar
```

It generates a _fat-jar_ in the `build/libs` directory. Run it

```
java -jar build/libs/vertx-whiskey-crud-fat.jar
```

Running with custom configuration

```
java -jar build/libs/vertx-whiskey-crud-fat.jar -conf custom-application-conf.json
```

#### Vert.x Gradle Starter

This project is a template to start your own Vert.x project using Gradle.

##### Prerequisites

* JDK 8+

##### Getting started

Create your project with:


```
git clone https://github.com/vert-x3/vertx-gradle-starter.git PROJECT_NAME
```

Replace `PROJECT_NAME` with the name of your project.

On Linux and MacOSx (or Windows with `bash`), if you want to go faster and generate an already configured project run:

```
curl http://vertx.io/assets/starter-scripts/create-vertx-project-gradle.sh -o vertx-create-gradle-project.sh; bash vertx-create-gradle-project.sh
```

##### Running the project

Once you have retrieved the project, you can check that everything works with:

```
./gradlew test run
```

The command compiles the project and runs the tests, then  it launches the application, so you can check by yourself. Open your browser to http://localhost:8080. You should see a _Hello World_ message.

##### Anatomy of the project

The project contains:

* the Gradle project and its configuration (`build.gradle`)
* a _main_ verticle file (src/main/java/io/vertx/starter/MainVerticle.java)
* a unit test (src/main/test/io/vertx/starter/MainVerticleTest.java)

##### Start to hack

1. Delete the `.git` directory
2. Open the `build.gradle` file and customize the `version`. You can also change the `mainVerticleName` variable to use your own package name and verticle class.
3. Run `./gradlew run`.

This last command relaunches Gradle and the application as soon as you change something in `src/main`.

##### Building the project

To build the project, just use:

```
./gradlew shadowJar
```

It generates a _fat-jar_ in the `build/libs` directory. Run it

```
java -jar build/libs/vertx-whiskey-crud-fat.jar 
```


---
This project shows how to use the Vert.x 3.2 redeploy feature. Vert.x watches for file changes and will then compile these changes. The hello world verticle will be redeployed automatically.
Simply start the application with:

    ./gradlew run

Now point your browser at http://localhost:8080. Then you can make changes to the hello world verticle and reload the browser.

The whole configuration for this is rather simple:

    mainClassName = 'io.vertx.core.Launcher'
    def mainVerticleName = 'io.vertx.example.HelloWorldVerticle'

    // Vert.x watches for file changes in all subdirectories
    // of src/ but only for files with .java extension
    def watchForChange = 'src/**/*.java'

    // Vert.x will call this task on changes
    def doOnChange = './gradlew classes'

    run {
        args = ['run', mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$mainClassName", "--on-redeploy=$doOnChange"]
    }
