OSX/Linux CI: [![Travis Build Status](https://travis-ci.org/bmsantos/cola-maven-plugin.svg?branch=master)](https://travis-ci.org/bmsantos/cola-maven-plugin)

Windows CI: [![AppVeyor Build status](https://ci.appveyor.com/api/projects/status/3k6ewjhnvr2itn9c?svg=true)](https://ci.appveyor.com/project/bmsantos/cola-maven-plugin)

Questions? [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bmsantos/cola-maven-plugin?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

#cola-maven-plugin

JUnit + BDD = COLA Maven Plugin

COLA Tests [Web Site](http://bmsantos.github.io/cola-maven-plugin/)
COLA Tests [Wiki](https://github.com/bmsantos/cola-tests/wiki)

###Maven Setup:
```xml

    <properties>
        <cola.version>0.1.0</cola.version>
    </properties>

    <!-- If using snapshot uncomment the next snapshot repo defenition -->
    <!-- repositories>
        <repository>
            <id>snapshots-repo</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            <releases><enabled>false</enabled></releases>
            <snapshots><enabled>true</enabled></snapshots>
        </repository>
    </repositories -->

    <!-- pluginRepositories>
        <pluginRepository>
            <id>snapshots-repo</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
            <releases><enabled>false</enabled></releases>
            <snapshots><enabled>true</enabled></snapshots>
        </pluginRepository>
    </pluginRepositories -->

    <dependencies>
        <dependency>
            <groupId>com.github.bmsantos</groupId>
            <artifactId>cola-tests</artifactId>
            <version>${cola.version}</version>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.github.bmsantos</groupId>
                <artifactId>cola-maven-plugin</artifactId>
                <version>${cola.version}</version>
                <configuration>
                    <ideBaseClass>com.github.bmsantos.maven.cola.BaseColaTest</ideBaseClass>
                    <ideTestMethod>iWillBeRemoved</ideTestMethod>
                    <includes>
                        <include>**/*Test.class</include>
                    </includes>
                    <excludes>
                        <exclude>**/ExcludedTest.class</exclude>
                    </excludes>
                </configuration>
                <executions>
                    <execution>
                        <id>compile-cola-tests</id>
                        <phase>process-test-classes</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```
