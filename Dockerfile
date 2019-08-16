FROM tomcat:9
# Take the war and copy to webapps of tomcat
COPY C:\Program Files (x86)\Jenkins\workspace\JavaAppPackage\target\*.war /usr/local/tomcat/webapps/addressbook.war
