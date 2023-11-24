FROM amazoncorretto:21-alpine-jdk
ARG dist_path
ARG entrypoint_name
ENV entrypoint_name=${entrypoint_name}
COPY ${dist_path} /app
ENTRYPOINT ["/app/bin/${entrypoint_name}"]