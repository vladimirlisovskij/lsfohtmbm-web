FROM amazoncorretto:17-alpine-jdk
COPY ./app/build/distributions/app /app
RUN /app/bin/app --database=MOCK_PATH &