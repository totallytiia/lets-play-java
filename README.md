# READ ME

### Generate a self-signed SSL certificate:

#### - open terminal and navigate to src/main/resources Directory

    keytool -genkeypair -alias letsplay -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore letsplay.p12 -validity 365 -dname "CN=localhost, OU=gritlab, O=gritlab, L=Mariehamn, S=Åland, C=FI" -storepass password -keypass password

- change storepass password and -keypass password
 : These are the passwords for accessing the keystore and the keypair, respectively. Replace password with a strong, unique password. Make sure to remember these passwords, as you'll need them to configure SSL in your Spring Boot application. Change the password in application.properties to the password you decided.


#### - Navigate back to the root of the project

### In terminal run, (you will be provided correct user and password)
    export MONGO_USER=<Username>
    export MONGO_PASSWORD=<Password>


### In terminal  run to perform a clean build and install the project

    ./mvnw clean install

### In terminal run to compile and run the Spring Boot application

    ./mvnw spring-boot:run

### Open Postman to test the project

- Request need to be JSON formatted.
- Check out API-Documentation for API instructions
- Use URL: `https://localhost:8443`


