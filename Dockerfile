FROM openjdk:17-jdk-slim
COPY ./app/build/distributions/app /app
RUN /app/bin/app --database=MOCK_PATH