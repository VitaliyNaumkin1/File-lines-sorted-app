Тестовое задание Java ,Сортировки строк исходящих файлов по 3 результирующим файлам integers.txt, floats.txt , strings.txt 
_______________________________________________________________________
Инструкция по запуску :
Версия Java: openjdk 18.9
Сборка - Maven 3.8.1
Пример запуска утилиты
java -jar util-jar-with-dependencies -s -o c:\1 -a -p sample- in1.txt in2.txt
_______________________________________________________________________
Особенности реализации:
1)параметр -o : при отсутствии этого параметра, или отсутствии пути ,отсортированные файлы будут находиться в папке input files\\ в корне проекта .
2)Если входные файлы не переданы , дается возможность в ручную прописать полный путь к файлам или же написать имя файла с расширением который находится в стандартной дирректории (input files\\)
3)при указании двух параметров сбора статистики -s -f , будет собираться только полная статистика

_______________________________________________________________________
Библиотеки:
<dependencies>
        <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-lang3 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.12.0</version>
        </dependency>
    </dependencies>

Maven зависимость для сборки jar файла :
 <build>
        <finalName>util</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>ru.naumkin.java.test.sort.file.contents.Main</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>

            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <descriptorRefs>
                        <descriptorRef>jar-with-dependencies</descriptorRef>
                    </descriptorRefs>
                    <archive>
                        <manifest>
                            <mainClass>ru.naumkin.java.test.sort.file.contents.Main</mainClass>
                        </manifest>
                    </archive>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <phase>package</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
    
