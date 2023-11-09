FROM ubuntu:22.04
COPY ./app/build/distributions/app /app
RUN /app/app