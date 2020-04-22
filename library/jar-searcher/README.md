# JarSearcher
我们在写Android plugin的时候，在分析Transform过程的时候，会分析到文件夹下面的Jar包等等。 
此工具可以用来查找Jar包里面包含的文件。

#### Helper

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