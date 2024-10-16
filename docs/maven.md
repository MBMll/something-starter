### copy common resources

```xml
<build>
    <resources>
        <resource>
            <directory>${maven.multiModuleProjectDirectory}/config</directory>
            <includes>
                <include>logback.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>
</build>
```
### spring boot lib 分离打包

```xml

<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
            <configuration>
                <layout>ZIP</layout>
                <excludes>
                    <exclude>
                        <groupId>*</groupId>
                        <artifactId>*</artifactId>
                    </exclude>
                </excludes>
                <classifier>exec</classifier>
                <mainClass>com.github.mbmll.example.assembly.Application</mainClass>
            </configuration>
        </execution>
    </executions>
</plugin>
```

```shell

java -jar -Dloader.path=./lib assembly-plugin-example-dev-exec.jar

```