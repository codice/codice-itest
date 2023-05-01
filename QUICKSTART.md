# Codice ITest Quickstart

## Create the ITest project

1) Clone the `codice-itest` repository:

```shell
git clone git@github.com:codice/codice-itest.git
```

2) Build the component: 

```shell
cd codice-itest && mvn clean install
```

3) The command below will create a `hello world` version of a codice-itest application. You can change the `groupId`,
`artifactId` and `version` to values that make sense for your use-case. The `archetype*` values must be used as-is.

```shell
mvn archetype:generate -DinteractiveMode=false \
                       -DarchetypeCatalog=local \
                       -DarchetypeGroupId=org.codice \
                       -DarchetypeArtifactId=codice-itest-archetype \
                       -DarchetypeVersion=1.0.0 \
                       -DgroupId=com.foo.bar \
                       -DartifactId=hello-world-test \
                       -Dversion=1.0.0
```

4) Add ITests. 
  * An example is given at `src/main/java/mil/af/soaesb/tests/IntegrationTestConfiguration.java`. This class 
can be deleted once you've added your own. 
  * In essence, each ITest is a Spring Bean that implements the `org.codice.itest.api.IntegrationTest` interface.

## Build and run your ITest project

1) Build the project:

```shell
mvn clean install
```

2) Run the project:

```shell
java -jar target/hello-world-test-1.0.0.jar
```

## [Optional] Build and run as a docker container:

1) Build container: 

```shell 
mvn jib:dockerBuild
```

2) Run container: 

```shell
docker images | grep hello-world | tr -s ' ' | cut -d " " -f3 | uniq
```