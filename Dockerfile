ENV DATABASE "/database/lsfohtmbm-web.db"
ENV PORT "8080"
ENV HOST "127.0.0.1"

FROM amazoncorretto:17-alpine-jdk
COPY ./app/build/distributions/app /app
ENTRYPOINT /app/bin/app --database=${DATABASE} --host=${HOST} --port=${PORT}
