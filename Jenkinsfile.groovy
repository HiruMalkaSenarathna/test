pipeline {
    agent any
    
    environment {
        //MYSQL_CREDS = credentials('mysql')
        def liquibaseProperties = """C:\\Users\\hirum\\docker\\liquibase.properties"""
        def changelog = """C:\\Users\\hiru\\docke\\changelog\\db.changelog-master.xml"""
        def lbpdirectory="/var/jenkins_home/workspace/liquibase-test/"
        def lbcdirectory="/var/jenkins_home/workspace/liquibase-test/"
        def DB_HOST = 'mysql'
        def DB_PORT = '3306'
        def DB_NAME = 'mydb'
        //DB_USERNAME = credentials('mysql-username')
        //DB_PASSWORD = credentials('mysql-password')
    }
    
    stages {
        stage('Initiate') {
            steps {
                script {

                    propertiesPath = env.liquibaseProperties
                    changelogPath = env.changelog
                    //bat "xcopy ${liquibaseProperties} /var/lib/docker/aufs/mnt/${lbpdirectory}"
                   // bat "xcopy ${changelog} /var/lib/docker/aufs/mnt/${lbcdirectory}"
                    //sh "cp ${liquibaseProperties} ${lbpdirectory}"
                    //sh "cp ${changelog} ${lbcdirectory}"

                    sh "scp hiru@192.168.56.1:\"${liquibaseProperties}\" ${lbpdirectory}"
                    sh "scp hiru@192.168.56.1:\"${changelog}\" ${lbcdirectory}"
                }
            }
        }
        stage('Start Docker Container') {
            steps {
                sh '/var/jenkins_home/liquibase/liquibase init project'
                sh '/var/jenkins_home/liquibase/liquibase --changeLogFile=${changelogPath} \
                    --url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
                    --username=root \
                    --password=root \
                    update'
                    
                sh '/var/jenkins_home/liquibase/liquibase update --url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
                    --username=root \
                    --password=root'
                //sh 'liquibase update --url="jdbc:mysql://192.168.32.11:3306/my_app" --changeLogFile=my_app-wrapper.xml --username=root --password=root --driver=com.mysql.cj.jdbc.Driver'

            }
        }
    }
}