pipeline 
{
   agent any
	stages 
	{   
		/*No required Comments*/
		stage ('Cloning'){
			steps {
				git credentialsId: 'bc8a73bd-e260-4c70-acce-8f6daa7dd67d', url: 'https://github.com/sridharbabukv/HouseApp.git'
			}
		}
		
		stage ('Unit Test'){
			steps {
				bat label: '', script: 'mvn clean test'
			}
		}
		
		stage('Sonar Qube Analysis'){
			steps{				
				withSonarQubeEnv('sonarH2')
				{
					bat "${MAVEN_HOME}/bin/mvn sonar:sonar"
				}
			}
		}		
				
		 stage('Package')
		  {
			  steps 
			  {
				bat label: '', script: 'mvn package'

			  }			  		
		  }
		  
		stage('Remove Existing QA env')
		{
			steps {    
				script {
				  FILENAME = bat(label: 'Get file name', returnStdout: true, script:"@docker ps -a -q --filter  name=TestingServer").trim()
			  
				echo FILENAME
				
				if(FILENAME !=null && FILENAME != "")
				{
					echo "Removing Existing Container"
					bat "docker rm --force ${FILENAME}"
				}
				
				}
			}
		}
   
		stage('Create Docker Image for QA env')
		{
			steps {
			bat label: '', script: 'docker build -t kellavijay/javadockerimage:1.0 .'
			}
		}

		stage('Push Docker Image to DockerHub'){
			steps {
			withCredentials([string(credentialsId: 'vijayDockerHub', variable: 'dockerHudPwd')]) {
				bat "docker login -u kellavijay -p ${dockerHudPwd}"
			}
		
			bat 'docker push kellavijay/javadockerimage:1.0'
			}
		}

		stage('Start QA env'){
			steps {    
				bat 'docker run -itd --name TestingServer -p 9553:8080 kellavijay/javadockerimage:1.0'                
				script {
				  FILENAME = bat(label: 'Get file name', returnStdout: true, script:"@docker ps -a -q --filter  name=TestingServer").trim()
			  
				echo FILENAME
				
					if(FILENAME !=null && FILENAME != "")
					{
						echo "started"
						bat "docker start ${FILENAME}"
					}
				}
			}
		}
		   
	}
    post 
    {
            
		success 
		{
		   emailext attachLog: true, 
		   body:         """<table>
		  <tr><td> Hi Team,</td></tr>
		   <tr><td> <p>Please find the below project Build Details.</h4></p></tr>
		   <tr><td> <b>PROJECT NAME:</b><span> \'${env.JOB_NAME}\' </span></td> </tr>
		   <tr><td> <b>BUILD VERSION:</b><span> \'${env.BUILD_NUMBER}\'</span></td> </tr>
		   <tr><td> <b>BUILD URL:</b>&nbsp;<a href="http://192.168.10.55:9553/addressbook/">http://192.168.10.55:9553/addressbook/<a></td> </tr>
			<!-- <tr><td><p>View console output at "<a href="${env.BUILD_URL}"> ${env.JOB_NAME}:${env.BUILD_NUMBER}</a>"</p></td></tr> -->
			 <tr><td><p><i>(Build log is attached.)</i></p></td></tr>
			  <tr><td><p>Thanks & Regards,<br/><span>Deployment Team.</span></p>       
			  </td></tr>
			</table>""",
		   compressLog: true,
		   recipientProviders: [[$class: 'DevelopersRecipientProvider'],[$class: 'RequesterRecipientProvider']],
		   replyTo: 'do-not-reply@company.com', 
		   subject: "Status: ${currentBuild.result?:'SUCCESS'} -  Job \'${env.JOB_NAME}:${env.BUILD_NUMBER}\'", 
			to: 'kellavijay@gmail.com sridharbabu.kv@gmail.com kvk999999@gmail.com'
		}
		failure 
		{
		 emailext attachLog: true, 
		   body:         """<table>
		  <tr><td> Hi Deployment Team,</td></tr>
		   <tr><td> <p>Build Failed please find the below project Build Details.</h4></p></tr>
		   <tr><td> <b>PROJECT NAME:</b><span> \'${env.JOB_NAME}\' </span></td> </tr>
		   <tr><td> <b>BUILD VERSION:</b><span> \'${env.BUILD_NUMBER}\'</span></td> </tr>
			 <tr><td><p><i>(Build log is attached.)</i></p></td></tr>
			  <tr><td><p>Thanks & Regards,<br/><span>Jenkins.</span></p>       
			  </td></tr>
			</table>""",
		   compressLog: true,
		   recipientProviders: [[$class: 'DevelopersRecipientProvider'],[$class: 'RequesterRecipientProvider']],
		   replyTo: 'do-not-reply@company.com', 
		   subject: "Status: ${currentBuild.result?:'SUCCESS'} -  Job \'${env.JOB_NAME}:${env.BUILD_NUMBER}\'", 
			to: 'kellavijay@gmail.com sridharbabu.kv@gmail.com kvk999999@gmail.com'
		}

	}
     
}
