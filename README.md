## 一群社区

## 资料
[srpint文档](https://spring.io/guides)

[springWeb文档](https://spring.io/guides/gs/serving-web-content/)

[githubOAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

##流程
mvn flyway:migrate

mvn flyway:repair

mvn clean compile flyway:migrate

mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate

java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar
