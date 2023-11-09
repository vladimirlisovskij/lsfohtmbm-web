ENV database = "/database/lsfohtmbm-web.db"
ENV port = "8080"
ENV host = "127.0.0.1"

FROM amazoncorretto:17-alpine-jdk
COPY ./app/build/distributions/app /app
ENTRYPOINT /app/bin/app --database=${database} --host=${host} --port=${port}
