@Library('github.com/connexta/cx-pipeline-library@master') _
@Library('github.com/connexta/github-utils-shared-library@master') __

pipeline {
    agent {
        node {
            label 'linux-small'
            customWorkspace "/jenkins/workspace/${JOB_NAME}/${BUILD_NUMBER}"
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr:'25'))
        disableConcurrentBuilds()
        timestamps()
        skipDefaultCheckout()
    }
    environment {
        LARGE_MVN_OPTS = '-Xmx8192M -Xss128M -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC '
        LINUX_MVN_RANDOM = '-Djava.security.egd=file:/dev/./urandom'
        GITHUB_USERNAME = 'codice'
        GITHUB_TOKEN = credentials('github-api-cred')
        GITHUB_REPONAME = 'codice-itest'
        DOCKERHUB_CREDS = credentials ('dockerhub-codicebot')
    }
    stages {
        stage('Setup') {
            steps {
                dockerd {}
                slackSend color: 'good', message: "STARTED: ${JOB_NAME} ${BUILD_NUMBER} ${BUILD_URL}"
                postCommentIfPR("Internal build has been started, your results will be available at build completion.", "${GITHUB_USERNAME}", "${GITHUB_REPONAME}", "${GITHUB_TOKEN}")
            }
        }

        // Checkout the repository
        stage('Checkout repo') {
            steps {
                retry(3) {
                    checkout scm
                }
            }
        }

        // The incremental build will be triggered only for PRs. It will build the differences between the PR and the target branch
        stage('Incremental Build') {
            when {
                allOf {
                    expression { env.CHANGE_ID != null }
                    expression { env.CHANGE_TARGET != null }
                }
            }
            options {
                timeout(time: 1, unit: 'HOURS')
            }
            steps {
                withMaven(maven: 'maven-latest', globalMavenSettingsConfig: 'default-global-settings', mavenSettingsConfig: 'codice-maven-settings', mavenOpts: '${LARGE_MVN_OPTS} ${LINUX_MVN_RANDOM}') {
                    sh 'mvn install -B -DskipStatic=true -DskipTests=true'
                    sh 'mvn clean install -B -Dgib.enabled=true -Dgib.referenceBranch=/refs/remotes/origin/$CHANGE_TARGET'
                }
            }
        }

        stage('Full Build') {
            when {
                expression { env.CHANGE_ID == null } 
            }
            options {
                timeout(time: 1, unit: 'HOURS')
            }
            steps {
                withMaven(maven: 'maven-latest', mavenSettingsConfig: 'codice-maven-settings', mavenOpts: '${LARGE_MVN_OPTS} ${LINUX_MVN_RANDOM}') {
                    sh 'mvn clean install'
                }
            }
        }

        /*
          Deploy stage will only be executed for deployable branches. 
          It will also only deploy in the presence of an environment variable JENKINS_ENV = 'prod'. This can be passed in globally from the jenkins master node settings.
        */
        stage('Deploy') {
            when {
                allOf {
                    expression { env.CHANGE_ID == null }
                    expression { env.BRANCH_NAME ==~ /((?:\d*\.)?\d*\.x|master)/ }
                    environment name: 'JENKINS_ENV', value: 'prod'
                }
            }
        steps{
                withCredentials([usernameColonPassword(credentialsId: 'dockerhub-codicebot', variable: 'DOCKERHUB_TOKEN')]) {
                    withMaven(maven: 'maven-latest', globalMavenSettingsConfig: 'default-global-settings', mavenSettingsConfig: 'codice-maven-settings', mavenOpts: '${LINUX_MVN_RANDOM}') {
                        sh 'mvn deploy -B -DskipStatic=true -DskipTests=true -Djib.to.auth.username=$DOCKERHUB_CREDS_USR -Djib.to.auth.password=$DOCKERHUB_CREDS_PSW -P push'
                    }
                }
            }
        }
    }
    post {
        always{
            postCommentIfPR("Build ${currentBuild.currentResult} See the job results in [legacy Jenkins UI](${BUILD_URL}) or in [Blue Ocean UI](${BUILD_URL}display/redirect).", "${GITHUB_USERNAME}", "${GITHUB_REPONAME}", "${GITHUB_TOKEN}")
        }
        success {
            slackSend color: 'good', message: "SUCCESS: ${JOB_NAME} ${BUILD_NUMBER}"
        }
        failure {
            slackSend color: '#ea0017', message: "FAILURE: ${JOB_NAME} ${BUILD_NUMBER}. See the results here: ${BUILD_URL}"
        }
        unstable {
            slackSend color: '#ffb600', message: "UNSTABLE: ${JOB_NAME} ${BUILD_NUMBER}. See the results here: ${BUILD_URL}"
        }
        cleanup {
            catchError(buildResult: null, stageResult: 'FAILURE') {
                echo '...Cleaning up workspace'
                cleanWs()
                wrap([$class: 'MesosSingleUseSlave']) {
                    sh 'echo "...Shutting down Jenkins slave: `hostname`"'
                }
            }
        }
    }
}
