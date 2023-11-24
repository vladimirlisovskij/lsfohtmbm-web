ARG dist_path
ARG entrypoint_name
FROM amazoncorretto:21-alpine-jdk
COPY ${dist_path} /app
ENTRYPOINT ["/app/bin/${entrypoint_name}"]