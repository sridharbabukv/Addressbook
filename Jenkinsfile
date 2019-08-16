pipeline {
        agent any
stages {
        stage('Build Docker Image'){
            steps {
            bat label: '', script: 'docker build -t sri1980/javaapp-image:2.0.0 .'
            }
        }
    
        stage('Push Docker Image'){
            steps {
            withCredentials([string(credentialsId: 'docker-hub', variable: 'dockerHudPwd')]) {
                bat "docker login -u sri1980 -p ${dockerHudPwd}"
            }
        
            bat 'docker push sri1980/javaapp-image:2.0.0'
            }
        }
    
        stage('Run Container on Dev Server'){
            steps {
            bat 'docker run -itd -p 9501:8080 sri1980/javaapp-image:2.0.0'
            }
        }
    }
}
