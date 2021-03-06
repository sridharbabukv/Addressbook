pipeline 
{
   agent any
	stages 
	{   
		/*Source code checkout from GIT*/
		stage ('Checkout from GIT'){
			steps {
				git credentialsId: 'bc8a73bd-e260-4c70-acce-8f6daa7dd67d', url: 'https://github.com/sridharbabukv/HouseApp.git'
			}
		}
		/*Unit Testing with Maven Build Tool*/
		stage ('Unit Testing'){
			steps {
				bat label: '', script: 'mvn clean test'
			}
		}
		/*Code Analysis with SonarQube and PostgreSQL
		stage('Code Analysis'){
			steps{				
				withSonarQubeEnv('sonar1')
				{
					bat "${MAVEN_HOME}/bin/mvn sonar:sonar"
				}
			}
		}	*/	
		/* Package with Maven Build Tool */	
		 stage('Package')
		  {
			  steps 
			  {
				bat label: '', script: 'mvn package'

			  }			  		
		  }
		  /* Remove Existing Docker Container */
		stage('Remove Existing Docker Image')
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
   		/* Create Docker Image */
		stage('Create Docker Image')
		{
			steps {
			bat label: '', script: 'docker build -t kellavijay/javadockerimage:1.0 .'
			}
		}
		/* Push Docker Image to Docker Hub */
		stage('Push Docker Image'){
			steps {
			withCredentials([string(credentialsId: 'vijayDockerHub', variable: 'dockerHudPwd')]) {
				bat "docker login -u kellavijay -p ${dockerHudPwd}"
			}
		
			bat 'docker push kellavijay/javadockerimage:1.0'
			}
		}
		/* Create Docker Container and Deploy to QA Environment */
		stage('Deploy to QA Environment'){
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
            	/* Sent Success Notification through EMail */
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
			/* Sent Failure Notification through EMail */
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
