FROM ubuntu:22.04
COPY ./app/build/distributions/app /app
RUN /app/bin/app --database=MOCK_PATH