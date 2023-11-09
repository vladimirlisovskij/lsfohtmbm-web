FROM amazoncorretto:17-alpine-jdk
COPY ./app/build/distributions/app /app
ENTRYPOINT /app/bin/app
CMD ["--database=/database/lsfohtmbm-web.db", "--host=127.0.0.1", "--port=8080"]
