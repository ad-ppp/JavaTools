# JarSearcher

1. Run with gradle debug mode
`gradle runWithjavaExec -Dorg.gradle.debug=true  --no-daemon`

2. Define task for running JarSearcher#main
see build.gradle of app. See[gradle-run-java-main](https://www.baeldung.com/gradle-run-java-main)
with command as follows to pass arguments:
``
3. run with gradle
`gradle :jar-searcher:release`  to generate jar. 
With the jar run with
`java -jar *.jar -h` to find helper.