FROM tomcat:9
# Take the war and copy to webapps of tomcat
COPY target/*.war /Tomcat 9.0/webapps
