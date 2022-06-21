# java-venda-carros

É necessário configurar o wildfly para possuir o driver do PostgreSQL e também o datasource utilizado no backend, para isso:

• Configurar o driver PostgreSQL no wildfly: <br>
  1 - Navegar até a pasta do wildfly: wildfly-{sua-versao}\standalone\configuration; <br>
  2 - Abrir o arquivo standalone-full.xml com editor de texto; <br>
  3 - Adicionar a seguinte tag dentro da tag \<drivers\>: <br>
\<driver name="postgresql" module="org.postgresql"\> <br>
  \<xa-datasource-class\>org.postgresql.xa.PGXADataSource\</xa-datasource-class\> <br>
\</driver\> <br>
  4 - Adicionar o datasource do projeto, adicionando a seguinte tag dentro da tag \<datasources\>:
  <datasource jta="true" jndi-name="java:jboss/datasources/CarrosDS" pool-name="CarrosDSPOOL" enabled="true" use-ccm="false"> <br>
\<connection-url\>jdbc:postgresql://localhost:5432/vendacarros\</connection-url\> <br>
\<driver-class\>org.postgresql.Driver\</driver-class\> <br>
\<driver\>postgresql\</driver\> <br>
\<security\> <br>
\<user-name\>postgres\</user-name\> <br>
\<password\>root\</password\> <br>
\</security\> <br>
\</datasource\> <br>

Após isso, configurar o server do wildfly no NetBeans apontando para o standalone-full.xml alterado previamente e rodar o projeto. <br>
O banco de dados será inicializado com as tabelas utilizadas no backend automáticamente. <br>

A collection está também no repositório caso queira testar o backend.
