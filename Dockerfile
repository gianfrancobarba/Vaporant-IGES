# Stage 1: Build con Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia file di configurazione Maven e directory sorgente
COPY pom.xml .
COPY src ./src
COPY WebContent ./WebContent

# Esegue la build del pacchetto escludendo i test (la WAR sarà generata in /app/target/ROOT.war)
RUN mvn -q -DskipTests clean package

# Stage 2: Runtime con Tomcat 9
FROM tomcat:9-jdk17-temurin

# Rimuove le applicazioni di default di Tomcat per un ambiente più pulito e sicuro
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia il driver MySQL dal progetto alle librerie globali di Tomcat (necessario per DataSource JNDI)
COPY WebContent/WEB-INF/lib/mysql-connector-java-8.0.12.jar /usr/local/tomcat/lib/

# Abilita Tomcat alla sostituzione delle variabili d'ambiente nel context.xml
RUN echo "org.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource" >> /usr/local/tomcat/conf/catalina.properties

# Copia la variante parametrizzata del context.xml
COPY docker/context.xml /usr/local/tomcat/conf/context.xml

# Copia il pacchetto .war compilato dallo stage precedente.
# Il nome del file determina il context path: 'Vaporant_IGES_war_exploded' allinea il container
# all'ambiente di sviluppo IntelliJ e alla base dei test Katalon (http://localhost:8080/Vaporant_IGES_war_exploded/).
COPY --from=build /app/target/ROOT.war /usr/local/tomcat/webapps/Vaporant_IGES_war_exploded.war

# Installa curl: serve all'healthcheck HTTP dell'applicazione (l'immagine base non lo include)
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# Esegue Tomcat con un utente non privilegiato (principio del minimo privilegio).
# Tutte le operazioni che richiedono root (copie, append, install) sono già concluse sopra;
# la porta 8080 (> 1024) è apribile anche da un utente non-root.
RUN groupadd -r tomcat && useradd -r -g tomcat tomcat \
 && chown -R tomcat:tomcat /usr/local/tomcat
USER tomcat

EXPOSE 8080
CMD ["catalina.sh", "run"]
