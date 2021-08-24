From openjdk:8
ADD target/atm-simulator-0.0.1-SNAPSHOT.jar atm-simulator.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev
CMD ["java","-Dspring.profiles.active=dev","-jar","atm-simulator.jar"]