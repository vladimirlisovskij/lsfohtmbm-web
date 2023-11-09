FROM ubuntu:22.04
ADD ./app/build/distributions/app.tar /app
RUN /app/bin/app