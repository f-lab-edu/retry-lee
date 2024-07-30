FROM eclipse-temurin:21

# 컨테이너 내 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/api-admin*.jar api-admin.jar

# 애플리케이션이 사용할 포트를 노출
EXPOSE 8082

# JAR 파일을 실행
ENTRYPOINT ["java", "-jar", "api-admin.jar"]