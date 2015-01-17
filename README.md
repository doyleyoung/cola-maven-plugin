OSX/Linux CI: [![Travis Build Status](https://travis-ci.org/bmsantos/cola-maven-plugin.svg?branch=master)](https://travis-ci.org/bmsantos/cola-maven-plugin)

Windows CI: [![AppVeyor Build status](https://ci.appveyor.com/api/projects/status/3k6ewjhnvr2itn9c)](https://ci.appveyor.com/project/bmsantos/cola-maven-plugin)

Questions? [!Join Chat](https://badges.gitter.im/Join Chat.svg)(https://gitter.im/bmsantos/cola-maven-plugin)

#cola-maven-plugin

JUnit + BDD = COLA Maven Plugin

COLA Test [Web Site](http://bmsantos.github.io/cola-maven-plugin/)

Current version: 0.0.3

Current snapshot (non-production): 0.0.4-SNAPSHOT

###Maven Setup:
```xml

    <properties>
        <cola.version>0.0.3</cola.version>
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
                    <ideBaseClassTest>iWillBeRemoved</ideBaseClassTest>
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
