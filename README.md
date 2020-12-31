# jfr-sample

## 参考文献
- [サンプルプログラムはこちらのリポジトリを参考](https://github.com/Takaichi00/jdbc-bad-sample)
- [最初の1リクエスト目で何をしているのか？](https://b.chiroito.dev/entry/2020/09/20/212719)
- [Java Flight Recorderでトラブルシューティング](https://qiita.com/sahn/items/952f8c1fdc463fa372b4)
- [JDBC & JFR Sample](https://github.com/chiroito/Jfr4Jdbc)
- [Quarkus で JFR Event Streaming](https://b.chiroito.dev/entry/2020/05/28/185832)
- [JITとコードの暖気の実体](https://b.chiroito.dev/entry/2020/09/18/221458)
- [Rediness Probeでアプリケーションが十分にJITされたことを検知する](https://b.chiroito.dev/entry/2020/09/19/225533)
- [Java Mission Control の紹介 (JMC)](https://docs.oracle.com/javase/jp/7/technotes/guides/jmc/intro.html)
- [パフォーマンスのトラブルシュート入門](https://speakerdeck.com/chiroito/getting-started-performance-troubleshoot)
- [Java 11のFlight Recorderを試す](https://matsumana.info/blog/2018/10/16/jdk11-flight-recorder/)
- [JFR に関する情報がまとまった gitbook ページ](https://koduki.github.io/docs/book-introduction-of-jfr/site/)
    - [1.2 Javaにおけるパフォーマンス分析と障害診断](https://koduki.github.io/docs/book-introduction-of-jfr/site/01/02-other_tools.html)
        - こちらは JFR に限らず Java におけるいろいろなツール (jps, jstack, etc...)が紹介されている

## 実行メモ
- 実行時に `-XX:StartFlightRecording` をつける
    - [Java 11のFlight Recorderを試す](https://matsumana.info/blog/2018/10/16/jdk11-flight-recorder/)
```
mvn clean package -DskipTests=true
java -jar target/quarkus-sample-0.0.1-SNAPSHOT-runner.jar -XX:StartFlightRecording
```
- `jmc` コマンドを実行しようとしたが、`$JAVA_HOME` に jmc コマンドがない
    - [1.4 JFRの動作環境とJMCのインストール](https://koduki.github.io/docs/book-introduction-of-jfr/site/01/04-install_jmc.html)を参考に、docker で起動してみる
```
git clone https://github.com/openjdk/jmc.git
cd jmc
docker-compose -f docker/docker-compose.yml run jmc

...
[WARNING] Error injecting: org.eclipse.tycho.core.maven.TychoMavenLifecycleParticipant
java.lang.TypeNotPresentException: Type org.eclipse.tycho.core.maven.TychoMavenLifecycleParticipant not present
	at org.eclipse.sisu.space.URLClassSpace.loadClass(URLClassSpace.java:147)
	at org.eclipse.sisu.space.NamedClass.load(NamedClass.java:46)
	at org.eclipse.sisu.space.AbstractDeferredClass.get(AbstractDeferredClass.java:48)
	at com.google.inject.internal.ProviderInternalFactory.provision(ProviderInternalFactory.java:81)
	at com.google.inject.internal.InternalFactoryToInitializableAdapter.provision(InternalFactoryToInitializableAdapter.java:53)
	at com.google.inject.internal.ProviderInternalFactory$1.call(ProviderInternalFactory.java:65)
	at com.google.inject.internal.ProvisionListenerStackCallback$Provision.provision(ProvisionListenerStackCallback.java:115)
	at org.eclipse.sisu.bean.BeanScheduler$Activator.onProvision(BeanScheduler.java:176)
	at com.google.inject.internal.ProvisionListenerStackCallback$Provision.provision(ProvisionListenerStackCallback.java:126)
	at com.google.inject.internal.ProvisionListenerStackCallback.provision(ProvisionListenerStackCallback.java:68)
	at com.google.inject.internal.ProviderInternalFactory.circularGet(ProviderInternalFactory.java:63)
	at com.google.inject.internal.InternalFactoryToInitializableAdapter.get(InternalFactoryToInitializableAdapter.java:45)
	at com.google.inject.internal.ProviderToInternalFactoryAdapter$1.call(ProviderToInternalFactoryAdapter.java:46)
	at com.google.inject.internal.InjectorImpl.callInContext(InjectorImpl.java:1103)
	at com.google.inject.internal.ProviderToInternalFactoryAdapter.get(ProviderToInternalFactoryAdapter.java:40)
	at com.google.inject.internal.SingletonScope$1.get(SingletonScope.java:145)
	at com.google.inject.internal.InternalFactoryToProviderAdapter.get(InternalFactoryToProviderAdapter.java:41)
	at com.google.inject.internal.InjectorImpl$2$1.call(InjectorImpl.java:1016)
	at com.google.inject.internal.InjectorImpl.callInContext(InjectorImpl.java:1092)
	at com.google.inject.internal.InjectorImpl$2.get(InjectorImpl.java:1012)
	at org.eclipse.sisu.inject.LazyBeanEntry.getValue(LazyBeanEntry.java:81)
	at org.eclipse.sisu.plexus.LazyPlexusBean.getValue(LazyPlexusBean.java:51)
	at org.eclipse.sisu.wire.EntryListAdapter$ValueIterator.next(EntryListAdapter.java:111)
	at java.util.AbstractCollection.addAll(AbstractCollection.java:343)
	at org.apache.maven.DefaultMaven.getLifecycleParticipants(DefaultMaven.java:400)
	at org.apache.maven.DefaultMaven.doExecute(DefaultMaven.java:262)
	at org.apache.maven.DefaultMaven.doExecute(DefaultMaven.java:193)
	at org.apache.maven.DefaultMaven.execute(DefaultMaven.java:106)
	at org.apache.maven.cli.MavenCli.execute(MavenCli.java:863)
	at org.apache.maven.cli.MavenCli.doMain(MavenCli.java:288)
	at org.apache.maven.cli.MavenCli.main(MavenCli.java:199)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced(Launcher.java:289)
	at org.codehaus.plexus.classworlds.launcher.Launcher.launch(Launcher.java:229)
	at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode(Launcher.java:415)
	at org.codehaus.plexus.classworlds.launcher.Launcher.main(Launcher.java:356)
Caused by: java.lang.UnsupportedClassVersionError: org/eclipse/tycho/core/maven/TychoMavenLifecycleParticipant has been compiled by a more recent version of the Java Runtime (class file version 55.0), this version of the Java Runtime only recognizes class file versions up to 52.0
	at java.lang.ClassLoader.defineClass1(Native Method)
```
- maven のバージョンが古い? [Dockerfile](https://github.com/openjdk/jmc/blob/master/docker/Dockerfile-jmc) を見てみると openjdk:8 となっている。これを11にしてみる。
- しかし以下のエラーが発生、windows 対応なのか? 諦めて公式サイトからバイナリを取得する。
```
[ERROR] An error occurred while transferring artifact canonical: osgi.bundle,com.make.chromium.cef.win32.win32.x86_64,0.4.0.202005172227 from repository https://equo-chromium-cef.ams3.digitaloceanspaces.com/rls/repository:
[ERROR]    Unable to read repository at https://equo-chromium-cef.ams3.digitaloceanspaces.com/rls/repository/plugins/com.make.chromium.cef.win32.win32.x86_64_0.4.0.202005172227.jar.
[ERROR] Internal error: org.eclipse.tycho.repository.local.MirroringArtifactProvider$MirroringFailedException: Could not mirror artifact osgi.bundle,com.make.chromium.cef.win32.win32.x86_64,0.4.0.202005172227 into the local Maven repository.See log output for details. Premature end of Content-Length delimited message body (expected: 56,220,832; received: 14,569,984) -> [Help 1]
org.apache.maven.InternalErrorException: Internal error: org.eclipse.tycho.repository.local.MirroringArtifactProvider$MirroringFailedException: Could not mirror artifact osgi.bundle,com.make.chromium.cef.win32.win32.x86_64,0.4.0.202005172227 into the local Maven repository.See log output for details.
	at org.apache.maven.DefaultMaven.execute(DefaultMaven.java:121)
	at org.apache.maven.cli.MavenCli.execute(MavenCli.java:863)
	at org.apache.maven.cli.MavenCli.doMain(MavenCli.java:288)
	at org.apache.maven.cli.MavenCli.main(MavenCli.java:199)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced(Launcher.java:289)
	at org.codehaus.plexus.classworlds.launcher.Launcher.launch(Launcher.java:229)
	at org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode(Launcher.java:415)
	at org.codehaus.plexus.classworlds.launcher.Launcher.main(Launcher.java:356)
Caused by: org.eclipse.tycho.repository.local.MirroringArtifactProvider$MirroringFailedException: Could not mirror artifact osgi.bundle,com.make.chromium.cef.win32.win32.x86_64,0.4.0.202005172227 into the local Maven repository.See log output for details.

```

- [AdoptOpenJDK の JMC (Mac) をダウンロード](https://adoptopenjdk.net/jmc.html)
- 以下を実行することで .app ファイルが取得できる
    - 必要に応じて [Mac 番の JDK をインストールする](https://www.oracle.com/java/technologies/javase-jdk15-downloads.html)
```
cat org.openjdk.jmc-7.1.1-macosx.cocoa.x86_64.tar.gz |tar xv -
```
