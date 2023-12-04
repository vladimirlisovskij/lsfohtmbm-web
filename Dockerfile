FROM amazoncorretto:17
ARG dist_path
ARG entrypoint_name
COPY ${dist_path} /app
RUN mv /app/bin/${entrypoint_name} /app/bin/app
ENTRYPOINT ["/app/bin/app"]