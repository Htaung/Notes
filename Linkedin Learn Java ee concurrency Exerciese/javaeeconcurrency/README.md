# Build
mvn clean package && docker build -t com.mycompany/javaeeconcurrency .

# RUN

docker rm -f javaeeconcurrency || true && docker run -d -p 8080:8080 -p 4848:4848 --name javaeeconcurrency com.mycompany/javaeeconcurrency 