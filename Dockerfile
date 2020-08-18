FROM base-spring-container:0.0.1
COPY ./target/integrationtest-*.jar /opt/boot/internal-lib/
COPY ./target/gson-*.jar /opt/boot/internal-lib/
COPY ./target/commons-lang3*.jar /opt/boot/internal-lib/