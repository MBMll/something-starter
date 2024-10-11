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