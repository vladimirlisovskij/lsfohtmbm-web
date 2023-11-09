FROM amazoncorretto:17-alpine-jdk
COPY ./app/build/distributions/app /app
ENTRYPOINT /app/bin/app --database=${DATABASE} --host=${HOST} --port=${PORT}
