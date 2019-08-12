
node {   
   def mvn = tool (name: 'LocalMaven', type: 'maven') + '/bin/mvn'
   stage('SCM Checkout'){
    // Clone repo
	git branch: 'master', 
	credentialsId: 'bc8a73bd-e260-4c70-acce-8f6daa7dd67d', 
	url: 'https://github.com/sridharbabukv/HouseApp.git'
   
   }
     
	
   stage('Mvn Package'){
	   // Build using maven
	   
	   sh "${mvn} clean package"
   }
   

   
}

