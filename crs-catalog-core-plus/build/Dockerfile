FROM azul/zulu-openjdk:25.0.4.1-jre
WORKDIR /app
ARG PORT
ENV PORT $PORT

COPY data/crs_catalog_v2.json /mnt/crs_catalogs/crs_catalog_v2.json
COPY crs-catalog-core-plus/target/crs-catalog-core-plus-*-spring-boot.jar crs-catalog-core-plus.jar

# Add a non-root user
RUN groupadd -g 10001 -r nonroot \
  && useradd -g 10001 -r -u 10001 nonroot

# Run as non-root user
USER 10001:10001
# Run the web service on container startup.
CMD java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${PORT} -jar /app/crs-catalog-core-plus.jar
