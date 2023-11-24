FROM amazoncorretto:21-alpine-jdk
ARG dist_path
ARG entrypoint_name
COPY ${entrypoint_name} /app
RUN mv /app/bin/${entrypoint_name} /app/bin/app
ENTRYPOINT ["/app/bin/app"]