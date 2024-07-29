FROM eclipse-temurin:21

# 업데이트 및 xargs를 위한 findutils 설치
#RUN apt-get update && apt-get install -y findutils

# 애플리케이션 디렉토리 생성
RUN mkdir -p /app

# 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 빌드를 위한 필요한 파일들 복사
COPY gradlew /app/
COPY gradle /app/gradle
COPY build.gradle /app/
COPY settings.gradle /app/


# api-user, storage, support 모듈의 소스 코드를 복사
COPY api-user /app/api-user
COPY storage /app/storage
COPY support /app/support

# Gradle 래퍼를 실행 가능하게 설정
RUN chmod +x /app/gradlew

# 프로젝트 빌드
RUN ./gradlew :api-user:build

# 빌드된 jar 파일을 애플리케이션 디렉토리로 복사
# 실제 jar 파일 이름 패턴과 일치하게 설정
RUN cp api-user/build/libs/api-user*.jar /app/api-user.jar

# 애플리케이션이 실행되는 포트를 노출
EXPOSE 8081

# 애플리케이션을 실행하기 위한 기본 명령어 설정
CMD ["java", "-jar", "/app/api-user.jar"]