FROM amazoncorretto:21-alpine-jdk
COPY ./app/build/distributions/app /app
ENTRYPOINT ["/app/bin/app"]