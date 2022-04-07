pipeline {

   agent any

    environment {
         IMAGE = readMavenPom().getArtifactId()
         VERSION = readMavenPom().getVersion()
    }
    stages {

        stage('Build maven') {
            steps {
                sh 'update-java-alternatives -s java-1.11.0-openjdk-amd64'
                sh 'mvn -B -U -DskipTests clean package'
                sh 'update-java-alternatives -s java-1.8.0-openjdk-amd64' 
            }
        }

        stage("Deploy - Producao"){
            when {
                 expression { VERSION ==~  /[0-9]+\.[0-9]+\.[0-9]+\  }
            }
            steps {
                script {
                    docker.build('us.gcr.io/poc-kubernetes-256618/${IMAGE}:${VERSION}','--build-arg AMBIENTE=P --build-arg JAR_FILE=${IMAGE}-${VERSION}.jar .' )
                    sh "docker push us.gcr.io/poc-kubernetes-256618/${IMAGE}:${VERSION}"
                }
                    sh "kubectl set image deployment.v1.apps/easytravel-deployment easytravel=us.gcr.io/poc-kubernetes-256618/${IMAGE}:${VERSION}"
            }
        }

        stage("Deploy - Homologacao"){
            when {
                expression { readMavenPom().getVersion().endsWith("SNAPSHOT") }
            }
            steps {
                sh "docker build --build-arg AMBIENTE=H --build-arg JAR_FILE=${IMAGE}-${VERSION}.jar . -t 127.0.0.1:32000/${IMAGE}:${VERSION}"
                sh "docker push 127.0.0.1:32000/${IMAGE}:${VERSION}"
                sh "microk8s.kubectl set image deployment.v1.apps/easytravel-deployment easytravel-deployment=127.0.0.1:32000/${IMAGE}:${VERSION}"
            }

        }

    }
}
