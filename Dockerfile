FROM ubuntu:22.04
ADD ./app/build/distributions/app /app
RUN /app/bin/app --database=MOCK_PATH