node('jdk8') {
  currentBuild.displayName = "#${env.BUILD_NUMBER} - ${params.BUILD_VERSION}"

  def serviceName = 'notification-service'
  def gitURL = "github.com/BISDigital/lite-${serviceName}"

  stage('Clean workspace'){
    deleteDir()
  }
  stage('Checkout files'){
    checkout scm
  }
  stage('Gradle publish'){
    sh 'chmod 777 gradlew'
    sh "./gradlew -PprojVersion=${params.BUILD_VERSION} :publishServicePublicationToLite-buildsRepository"
  }
  stage('Tag build'){
    withCredentials([usernamePassword(credentialsId: 'LITE-bot-github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
      sh("git -c 'user.name=Jenkins' -c 'user.email=jenkins@digital' tag  -a ${params.BUILD_VERSION} -m 'Jenkins'")
      sh("git push https://${env.GIT_USERNAME}:${env.GIT_PASSWORD}@${gitURL} --tags")
    }
  }
  stage('build'){
    build job: 'new-docker-build', parameters: [[$class: 'StringParameterValue', name: 'SERVICE_NAME', value: serviceName], [$class: 'StringParameterValue', name: 'BUILD_VERSION', value: params.BUILD_VERSION], [$class: 'StringParameterValue', name: 'DOCKERFILE_PATH', value: '.']]
  }
}
