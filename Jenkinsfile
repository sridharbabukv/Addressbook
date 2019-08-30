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
	
		stage ('Code Quality Analysis') 
		{
			steps {
				bat "${MAVEN_HOME}/bin/mvn --batch-mode -V -U -e checkstyle:checkstyle pmd:pmd pmd:cpd findbugs:findbugs"
				script {
					def checkstyle = scanForIssues tool: checkStyle(pattern: '**/target/checkstyle-result.xml') 
					publishIssues issues: [checkstyle]

					def pmd = scanForIssues tool: pmdParser(pattern: '**/target/pmd.xml')
					publishIssues issues: [pmd]

					def cpd = scanForIssues tool: cpd(pattern: '**/target/cpd.xml')
					publishIssues issues: [cpd]

					def spotbugs = scanForIssues tool: spotBugs(pattern: '**/target/findbugsXml.xml')
					publishIssues issues: [spotbugs]

					def maven = scanForIssues tool: mavenConsole()
					publishIssues issues: [maven]

					publishIssues id: 'analysis', name: 'All Issues', 
					issues: [checkstyle, pmd, spotbugs], 
					filters: [includePackage('io.jenkins.plugins.analysis.*')]
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
				  FILENAME = bat(label: 'Get file name', returnStdout: true, script:"@docker ps -a -q --filter  name=QAServer").trim()
			  
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
				bat 'docker run -itd --name QAServer -p 9533:8080 kellavijay/javadockerimage:1.0'                
				script {
				  FILENAME = bat(label: 'Get file name', returnStdout: true, script:"@docker ps -a -q --filter  name=QAServer").trim()
			  
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
		   <tr><td> <b>BUILD URL:</b>&nbsp;<a href="http://192.168.10.55:9533/addressbook/">http://192.168.10.55:9533/addressbook/<a></td> </tr>
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
