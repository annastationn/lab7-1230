Чтобы собрать проект: 
```shell
mvn package
```
После чего появится jar в папке target c именем lab7-1230-1.0-SNAPSHOT-jar-with-dependencies.jar
Его нужно запустить 
```shell
java -jar lab7-1230-1.0-SNAPSHOT-jar-with-dependencies.jar
```

После чего программа начнет работать

Для подключения к базе данных нужно указать данные в файле src/main/resources/ru/se/ifmo/application.properties
Для гелиоса там нужны его данные как их в полях db.user и db.password их можно получить зайдя на сервер и введя:
```shell
cat ~/.pgpass
```
у тебя db.user = s409267 db.password = 4LiKOoE7fo32aJl9
и также нужно изменить db.name = studs

Далее нужно создать твои таблицы(я это уже сделал но на всякий)
нужно перекинуть два файла src/main/sql/postgresql/create-tables.sql и src/main/sql/postgresql/insert-organization-types.sql на гелиос
далее комманды 
```shell
psql postgresql://s409267:4LiKOoE7fo32aJl9@localhost:5432/studs -f create-tables.sql
psql postgresql://s409267:4LiKOoE7fo32aJl9@localhost:5432/studs -f insert-organization-types.sql
```
теперь твоя бд работает как надо и имеет все нужные таблицы
