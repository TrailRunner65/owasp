Running on docker locally
-
Build image
- docker build --tag=owasp:latest .

Run container
- docker run -p8090:8080 owasp:latest
or
- docker-compose up

Pushing to AWS ECRNot 
-
not needed for elastic-beanstalk

Push image to AWS ECR
- aws ecr get-login-password --region ap-southeast-2 | docker login --username AWS --password-stdin 848765744232.dkr.ecr.ap-southeast-2.amazonaws.com
- aws ecr create-repository --repository-name owasp
- docker tag f5355636a414 848765744232.dkr.ecr.ap-southeast-2.amazonaws.com/owasp:1.0
- docker push 848765744232.dkr.ecr.ap-southeast-2.amazonaws.com/owasp:1.0

Including a Dockerrun.aws.json and docker-compose.yaml will fails in eb create

eb init
eb create owasp
