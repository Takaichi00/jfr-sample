# JFR (JDK Flight Recorder) Sample

* アプリケーションをビルド

```
mvn clean package
```

* Docker で MySQL DB を立ち上げる

```
cd local-env
docker-compose up -d
```

- テーブルのスキーマを flyway を用いて作成

```
mvn flyway:migrate

# スキーマ管理を再作成するときは以下のコマンドを実行します
mvn flyway:clean flyway:migrate
```

* １万件の csv データ生成 

```
for i in {1..10000}
do
echo "$i,\"name$i\",NULL" >> insert_tableA.csv
done

for i in {1..10000}
do
echo "\"name$i\",\"address$i\"" >> insert_tableB.csv
done
```

* csv データを MySQL に insert

```
# https://mita2db.hateblo.jp/entry/2020/01/13/163218
mysql -u root -h 127.0.0.1 -P 13306 -p -D test_database --local-infile=1

mysql> SET GLOBAL local_infile=on;
mysql> load data local infile "~/ideaProjects/jfr-sample/jdbc-bad-sample/src/main/resources/insert_tableA.csv" into table TABLE_A fields terminated by ',' optionally enclosed by '"';
mysql> load data local infile "~/ideaProjects/jfr-sample/jdbc-bad-sample/src/main/resources/insert_tableB.csv" into table TABLE_B fields terminated by ',' optionally enclosed by '"';
```

* 以下のコマンドを実行してアプリーケーションを起動

```
java \
-XX:StartFlightRecording=\ # JFR を有効にする
dumponexit=true,\  # JVM プロセスがシャットダウンした時にファイルにダンプする
filename=./output/jdbc-bad-sample-FULLGC.jfr,\
-Xms20M -Xmx20M -jar ./target/jdbc-bad-sample.jar
```

* しばらくすると、`Exception in thread "main" java.lang.OutOfMemoryError: Java heap space` が表示される

* JMC (JDK Mission Control) のダウンロード
  * Oracle: (https://www.oracle.com/java/technologies/javase/products-jmc8-downloads.html)
  * AdoptOpenJDK: (https://adoptopenjdk.net/jmc.html)
  * Zulu: (https://jp.azul.com/products/zulu-mission-control/)
* JMC の起動
  * 「ファイル(F)」→「ファイルを開く」から生成した jdbc-bad-sample-non-FULLGC.jfr, jdbc-bad-sample-FULLGC.jfr を選択すると分析結果が表示されていることを確認

![non-FULLGC](./img/non-FULLGC-jfr.png)

![FULLGC](./img/FULLGC-jfr.png)